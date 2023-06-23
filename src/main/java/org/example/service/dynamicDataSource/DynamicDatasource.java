package org.example.service.dynamicDataSource;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ldd
 * 动态数据源测试
 */
@Service
@DS("dm")
@Slf4j
public class DynamicDatasource {
    @Autowired
    DataSource dataSource;

    public boolean masterDataSource() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if(connection.getMetaData().getURL().contains("dm")){
            log.info("检测到dm数据源，多数据源配置成功！");
            return true;
        }
        return false;
    }
}
