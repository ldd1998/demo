package org.example.check;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 检查实体类和表结构是否一致，支持DM和MySQl
 * @author ldd
 */
@Component
@Slf4j
public class EntityTableChecker {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void checkEntityTableConsistency() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        List<String> tables = getTables(metaData);

        for (String tableName : tables) {
            List<String> columns = getColumns(jdbcTemplate,metaData, tableName);
            Class<?> entityClass = getEntityClass(tableName);

            if (entityClass == null) {
                log.warn("表："+tableName+" 无实体类");
                continue;
            }

            // 对@TableField(exist = false)的字段进行过滤
            Field[] fields = entityClass.getDeclaredFields();
            List<Field> fieldList = new ArrayList<>(Arrays.asList(fields));
            for (int i = fieldList.size() - 1; i >= 0; i--) {
                Field field = fieldList.get(i);
                TableField fieldAnnotation = field.getAnnotation(TableField.class);
                if (fieldAnnotation != null && fieldAnnotation.exist() == false) {
                    log.info("表："+tableName+"的字段："+field.getName()+" 不计入判断");
                    fieldList.remove(i);
                }
            }

            for (Field field : fieldList) {
                String fieldName = field.getName();
                if (!columns.contains(StrUtil.toUnderlineCase(fieldName).toLowerCase())) {
                    log.error("表："+tableName+" 缺失字段："+fieldName);
                }
            }

            List<String> fieldNames = new ArrayList<>();
            for (Field field : fieldList) {
                String fieldName = field.getName();
                fieldNames.add(StrUtil.toUnderlineCase(fieldName).toLowerCase());
            }
            for (String column : columns) {
                if(!fieldNames.contains(column)){
                    log.warn("实体类："+entityClass.getSimpleName()+" 缺失字段："+column);
                }
            }
        }
    }

    /**
     * 获取所有表名
     * @param metaData
     * @return
     * @throws SQLException
     */
    public List<String> getTables(DatabaseMetaData metaData) throws SQLException {
        List<String> tables = new ArrayList<>();
        String catalog = metaData.getConnection().getCatalog();
        String schema = metaData.getConnection().getSchema();
        ResultSet resultSet = metaData.getTables(catalog, schema, null, new String[]{"TABLE"});

        while (resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }

        return tables;
    }


    /**
     * 获取表字段
     * @param jdbcTemplate
     * @param metaData
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<String> getColumns(JdbcTemplate jdbcTemplate,DatabaseMetaData metaData, String tableName) throws SQLException {
        String databaseProductName = metaData.getDatabaseProductName();
        String databaseName = metaData.getURL().split("/")[3].split("\\?")[0];
        List<String> columns = new ArrayList<>();

        if(databaseProductName.equals("MySQL")){
            // 如果是MySQL数据库
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("show columns FROM " + databaseName+".`"+ tableName + "`");
            for (Map<String, Object> map : maps) {
                String columnName = map.get("Field").toString();
                columns.add(columnName);
            }
        }else if (databaseProductName.equals("DM DBMS")){
            // 如果是DM数据库
            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select column_name from all_tab_columns where table_name='"+tableName+"' and owner = '"+databaseName+"'");
            for (Map<String, Object> map : maps) {
                String columnName = map.get("COLUMN_NAME").toString();
                columns.add(columnName);
            }
        }
        return columns;
    }

    /**
     * 根据表名在指定包内获取对应实体类
     * @param tableName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Map<String,Class<?>> classMap = new HashMap<>();
    public Class<?> getEntityClass(String tableName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = ClassFinderInProject.findClassesWithAnnotationInPackage("", TableName.class);;
        if(classMap.keySet().size() == 0){
            classMap = classesToMap(classes);
        }
        return classMap.get(tableName.toLowerCase());
    }

    private Map<String, Class<?>> classesToMap(List<Class<?>> classes) {
        for (int i = 0; i < classes.size(); i++) {
            String simpleClassName = classes.get(i).getSimpleName();
            String lowerCaseClassName = StrUtil.toUnderlineCase(simpleClassName).toLowerCase();
            classMap.put(lowerCaseClassName,classes.get(i));
        }
        return classMap;
    }
}
