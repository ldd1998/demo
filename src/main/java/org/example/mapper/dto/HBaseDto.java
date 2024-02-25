package org.example.mapper.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
/**
 * @author ：jerry
 * @date ：Created in 2022/11/7 15:50
 * @description：结果集封装
 * @version: V1.1
 */
@Data
public class HBaseDto {
    /**
     * @param tableName    表名
     * @param rowKey       行主键
     * @param columnFamily 列族
     * @param column       列
     * @param value        值
     * @throws
     */
    @ApiModelProperty("行")
    private String row;
 
    @ApiModelProperty("列族")
    private String columnFamily;
 
    @ApiModelProperty("列")
    private String column;
 
    @ApiModelProperty("列值")
    private String value;
 
    @ApiModelProperty("表名")
    private String tableName;
 
    @ApiModelProperty("开始行")
    private String startRow;
 
    @ApiModelProperty("结束行")
    private String stopRow;
 
    @ApiModelProperty("报文时间")
    private Long msgtimeUtime;
 
    @ApiModelProperty("开始时间")
    private Long startTime;
 
    @ApiModelProperty("结束时间")
    private Long endTime;
 
    @ApiModelProperty("页码")
    private Integer current;
 
    @ApiModelProperty("条数")
    private Integer pageSize;
}