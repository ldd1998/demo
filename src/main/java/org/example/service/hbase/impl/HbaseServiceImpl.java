package org.example.service.hbase.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.example.config.HbaseConfig;
import org.example.mapper.dto.HBaseDto;
import org.example.mapper.vo.CommonResultVo;
import org.example.service.hbase.HbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
 
/**
 * @author ：jerry
 * @date ：Created in 2022/11/7 09:58
 * @description：
 * @version: V1.1
 */
@Service
@Slf4j
public class HbaseServiceImpl implements HbaseService {
    @Autowired
    private HbaseConfig config;
 
    private static Connection connection = null;
    private static Admin admin = null;
 
    @PostConstruct
    private void init() {
        if (connection != null) {
            return;
        }
        try {
            connection = ConnectionFactory.createConnection(config.configuration());
            admin = connection.getAdmin();
        } catch (IOException e) {
            log.error("HBase create connection failed:", e);
        }
    }
 
    /**
     * 根据表名/列族创建表
     *
     * @param tableName      表名
     * @param columnFamilies 列族名
     * @throws IOException 异常
     */
    @Override
    public CommonResultVo createTable(String tableName, String... columnFamilies) throws IOException {
        try {
            TableName name = TableName.valueOf(tableName);
            boolean isExists = this.tableExists(tableName);
            if (isExists) {
                throw new TableExistsException(tableName + "is exists!");
            }
            TableDescriptorBuilder descriptorBuilder = TableDescriptorBuilder.newBuilder(name);
            List<ColumnFamilyDescriptor> columnFamilyList = new ArrayList<>();
            for (String columnFamily : columnFamilies) {
                ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder
                        .newBuilder(columnFamily.getBytes()).build();
                columnFamilyList.add(columnFamilyDescriptor);
            }
            descriptorBuilder.setColumnFamilies(columnFamilyList);
            TableDescriptor tableDescriptor = descriptorBuilder.build();
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return CommonResultVo.success();
    }
 
 
    /**
     * 保存修改
     */
    @Override
    public void saveOrUpdate(HBaseDto dto) throws IOException {
        String tableName = dto.getTableName();  //表名
        String rowKey = dto.getRow();       //行主键
        String columnFamily = dto.getColumnFamily();        //列族
        String columns = dto.getColumn();        //列
        String value = dto.getValue();
        Long msgtimeUtime = dto.getMsgtimeUtime();
 
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columns), Bytes.toBytes(value));
        //设置时间戳
        put.setTimestamp(msgtimeUtime != null ? msgtimeUtime : System.currentTimeMillis());
        table.put(put);
    }
 
 
    /**
     * 删除
     * tableName    表名
     * rowKey       行主键
     * columnFamily 列族
     * column       列
     */
    @Override
    public CommonResultVo deleteTable(HBaseDto hBaseDto) throws IOException {
        boolean isExists = this.tableExists(hBaseDto.getTableName());
        if (!isExists) {
            return CommonResultVo.failed("表" + hBaseDto.getTableName() + "不存在");
        }
        String tableName = hBaseDto.getTableName();
        Table table = connection.getTable(TableName.valueOf(tableName));
 
 
        //删除列
        String columnFamily = hBaseDto.getColumnFamily();   //列族
        String row = hBaseDto.getRow();     //行主键
        String column = hBaseDto.getColumn();       //列
        if (StringUtils.isNotBlank(column) && StringUtils.isNotBlank(row)
                && StringUtils.isNotBlank(columnFamily) && StringUtils.isNotBlank(column)) {
            Delete delete = new Delete(row.getBytes());
            delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
            table.delete(delete);
            return CommonResultVo.success("列:" + column + "删除成功");
        }
 
        //删除列族
        if (StringUtils.isNotBlank(columnFamily) && StringUtils.isNotBlank(row)) {
            Delete delete = new Delete(row.getBytes());
            delete.addFamily(Bytes.toBytes(columnFamily));
            table.delete(delete);
            return CommonResultVo.success("列族:" + columnFamily + "删除成功");
        }
 
        //删除行主键
        if (StringUtils.isNotBlank(row)) {
            Delete delete = new Delete(row.getBytes());
            table.delete(delete);
        }
 
        //删除表
        if (StringUtils.isNotBlank(hBaseDto.getTableName())) {
            TableName name = TableName.valueOf(hBaseDto.getTableName());
            admin.disableTable(name);
            admin.deleteTable(name);
            return CommonResultVo.success("表:" + tableName + "删除成功");
        }
 
        return CommonResultVo.success();
    }
 
    /**
     * 判断表是否已经存在，这里使用间接的方式来实现
     *
     * @param tableName 表名
     * @return 真or假
     * @throws IOException 异常
     */
    @Override
    public boolean tableExists(String tableName) throws IOException {
        TableName[] tableNames = admin.listTableNames();
        if (tableNames != null && tableNames.length > 0) {
            for (int i = 0; i < tableNames.length; i++) {
                if (tableName.equals(tableNames[i].getNameAsString())) {
                    return true;
                }
            }
        }
        return false;
    }
 
 
    /**
     * 扫描指定列在指定行键范围的值
     */
    public CommonResultVo scanRowData(HBaseDto hBaseDto) throws IOException {
        List<Map<String, Object>> lis = new ArrayList<>();
 
        if (StringUtils.isBlank(hBaseDto.getTableName())) {
            CommonResultVo.failed("表名不能为null");
        }
        boolean flagStu = this.tableExists(hBaseDto.getTableName());
        if (!flagStu) {
            CommonResultVo.failed("表" + hBaseDto.getTableName() + "不存在");
        }
        Table table = connection.getTable(TableName.valueOf(hBaseDto.getTableName()));
        //指定起始行键和结束行键
        Scan scan = new Scan();
        //根据列族查询
        if (StringUtils.isNotBlank(hBaseDto.getColumnFamily())) {
            scan.addFamily(Bytes.toBytes(hBaseDto.getColumnFamily()));
        }
        //起始or结束行
        if (StringUtils.isNotBlank(hBaseDto.getStartRow()) || StringUtils.isNotBlank(hBaseDto.getStopRow())) {
            new Scan(Bytes.toBytes(hBaseDto.getStartRow()), Bytes.toBytes(hBaseDto.getStopRow()));
        }
        //指定行
        if (StringUtils.isNotBlank(hBaseDto.getRow())) {
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(hBaseDto.getRow()));
            scan.setFilter(rowFilter);
        }
        //扫描指定的列
        if (StringUtils.isNotBlank(hBaseDto.getColumn())) {
            scan.addColumn(Bytes.toBytes(hBaseDto.getColumnFamily()), Bytes.toBytes(hBaseDto.getColumn()));
        }
 
        //时间戳精确查询
        if (!Objects.isNull(hBaseDto.getMsgtimeUtime())) {
            scan.setTimestamp(hBaseDto.getMsgtimeUtime());
        }
 
        //时间戳区间查询
        if ((!Objects.isNull(hBaseDto.getStartTime())) && (!Objects.isNull(hBaseDto.getEndTime()))) {
            scan.setTimeRange(hBaseDto.getStartTime(), hBaseDto.getEndTime());
        }
        ResultScanner resultScanner = table.getScanner(scan);
 
        for (Result result : resultScanner) {
 
            Map<String, Object> map = new HashMap<>();
            String flag = "0";
            for (Cell cell : result.rawCells()) {
                String rowVal = Bytes.toString(CellUtil.cloneRow(cell));
                if (!flag.equals(rowVal)) {
                    map = new HashMap<>();
                }
                flag = rowVal;
                String columns = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                map.put(columns, value);
                map.put("row", rowVal);
                if (flag.equals(rowVal)) {
                    lis.remove(map);
                }
                lis.add(map);
//                System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)) +
//                        ", 列簇:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
//                        ", 列:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
//                        ", 值:" + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        return CommonResultVo.success(lis);
    }
 
    /**
     * 分页查询
     */
    @Override
    public CommonResultVo scanPageRow(HBaseDto hBaseDto) throws IOException {
 
 
        List<Map<String, Object>> lis = new ArrayList<>();
 
        if (StringUtils.isBlank(hBaseDto.getTableName())) {
            CommonResultVo.failed("表名不能为null");
        }
        boolean flagStu = this.tableExists(hBaseDto.getTableName());
        if (!flagStu) {
            CommonResultVo.failed("表" + hBaseDto.getTableName() + "不存在");
        }
        Table table = connection.getTable(TableName.valueOf(hBaseDto.getTableName()));
        //指定起始行键和结束行键
        Scan scan = new Scan();
        //根据列族查询
        if (StringUtils.isNotBlank(hBaseDto.getColumnFamily())) {
            scan.addFamily(Bytes.toBytes(hBaseDto.getColumnFamily()));
        }
        //起始or结束行
        if (StringUtils.isNotBlank(hBaseDto.getStartRow()) || StringUtils.isNotBlank(hBaseDto.getStopRow())) {
            new Scan(Bytes.toBytes(hBaseDto.getStartRow()), Bytes.toBytes(hBaseDto.getStopRow()));
        }
        //指定行
        if (StringUtils.isNotBlank(hBaseDto.getRow())) {
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(hBaseDto.getRow()));
            scan.setFilter(rowFilter);
        }
        //扫描指定的列
        if (StringUtils.isNotBlank(hBaseDto.getColumn())) {
            scan.addColumn(Bytes.toBytes(hBaseDto.getColumnFamily()), Bytes.toBytes(hBaseDto.getColumn()));
        }
 
        //时间戳精确查询
        if (!Objects.isNull(hBaseDto.getMsgtimeUtime())) {
            scan.setTimestamp(hBaseDto.getMsgtimeUtime());
        }
 
        //时间戳区间查询
        if ((!Objects.isNull(hBaseDto.getStartTime())) && (!Objects.isNull(hBaseDto.getEndTime()))) {
            scan.setTimeRange(hBaseDto.getStartTime(), hBaseDto.getEndTime());
        }
 
        //current 当前页     pageSize:条数
        scan.setCaching(hBaseDto.getPageSize() * hBaseDto.getCurrent() > 6000 ? 6000 : hBaseDto.getPageSize() * hBaseDto.getCurrent());
 
 
        ResultScanner resultScanner = table.getScanner(scan);
        Result[] results;
        int pageCount = 0;
        while ((results = resultScanner.next(hBaseDto.getPageSize())).length != 0) {
            pageCount++;
            if (pageCount < hBaseDto.getCurrent()) {
                continue;
            }
            for (Result rs : results) {
                //在此处解析获取数据
//                alls.add(rs);
                Map<String, Object> map = new HashMap<>();
                String flag = "0";
                for (Cell cell : rs.rawCells()) {
                    String rowVal = Bytes.toString(CellUtil.cloneRow(cell));
                    if (!flag.equals(rowVal)) {
                        map = new HashMap<>();
                    }
                    flag = rowVal;
                    String columns = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    map.put(columns, value);
                    map.put("row", rowVal);
                    if (flag.equals(rowVal)) {
                        lis.remove(map);
                    }
                    lis.add(map);
//                System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)) +
//                        ", 列簇:" + Bytes.toString(CellUtil.cloneFamily(cell)) +
//                        ", 列:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
//                        ", 值:" + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            break;
        }
        return CommonResultVo.success(lis);
    }
 
    /**
     * 获取HBase的总行数
     */
    @Override
    public long countRow(HBaseDto hBaseDto) throws IOException {
 
        Table table = connection.getTable(TableName.valueOf(hBaseDto.getTableName()));
        Scan scan = new Scan();
        scan.setCaching(20);
        scan.addFamily(Bytes.toBytes(hBaseDto.getColumnFamily()));
        ResultScanner scanner = table.getScanner(scan);
        long rowCount = 0;
 
        Result[] results;
        while ((results = scanner.next(hBaseDto.getPageSize())).length != 0) {
            rowCount+=results.length;
        }
        return rowCount;
    }
 
}