package org.example.service.hbase;


 
import org.example.mapper.dto.HBaseDto;
import org.example.mapper.vo.CommonResultVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
 
/**
 * @author ：jerry
 * @date ：Created in 2022/11/7 10:01
 * @description：
 * @version: V1.1
 */
public interface HbaseService {
 
    //创建表
    CommonResultVo createTable(String tableName, String... columnFamilies) throws IOException;
 
    void saveOrUpdate(HBaseDto hBaseDto) throws IOException;
 
    CommonResultVo deleteTable(HBaseDto hBaseDto) throws IOException;
 
    //判断表是否已经存在，这里使用间接的方式来实现
    boolean tableExists(String tableName) throws IOException;
 
    CommonResultVo scanRowData(HBaseDto hBaseDto) throws IOException;
 
    CommonResultVo scanPageRow(HBaseDto hBaseDto) throws IOException;
 
    long countRow(HBaseDto hBaseDto) throws IOException;
 
}