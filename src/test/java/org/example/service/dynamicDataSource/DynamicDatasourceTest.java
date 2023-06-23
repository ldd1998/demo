package org.example.service.dynamicDataSource;

import org.example.DemoApplicationForTest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DynamicDatasourceTest {

    @Autowired
    DynamicDatasource dynamicDatasource;
    @Test
    public void masterDataSource() throws SQLException {
        boolean b = dynamicDatasource.masterDataSource();
        Assert.assertTrue(b);
    }
}