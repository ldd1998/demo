package org.example.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.mapper.dto.HBaseDto;
import org.example.mapper.vo.CommonResultVo;
import org.example.service.hbase.HbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
import java.io.IOException;
import java.util.List;
 
/**
 * @author ：jerry
 * @date ：Created in 2022/11/7 10:39
 * @description：
 * @version: V1.1
 */
@Api(tags = "habse接口测试")
@RestController
@RequestMapping("/hbase")
public class HbaseController {
    @Autowired
    private HbaseService hbaseService;
 
    /**
     * 创建表
     * tableName:表名
     * columnFamilies:列族
     */
    @ApiOperation(value = "创建表")
    @GetMapping("/createTable")
    public CommonResultVo createTable(String tableName, String[] columnFamilies) throws IOException {
        return hbaseService.createTable(tableName, columnFamilies);
    }
 
    @ApiOperation(value = "新增/修改")
    @PostMapping("/saveOrUpdate")
    public CommonResultVo saveOrUpdate(@RequestBody List<HBaseDto> list) throws IOException {
        for (HBaseDto hBaseDto : list) {
            hbaseService.saveOrUpdate(hBaseDto);
        }
        return CommonResultVo.success();
    }
 
    @ApiOperation(value = "删除表/行/列族/列")
    @PostMapping("/deleteTable")
    public CommonResultVo deleteTable(@RequestBody HBaseDto hBaseDto) throws IOException {
        return hbaseService.deleteTable(hBaseDto);
    }
 
 
//    @ApiOperation(value = "判断表是否已经存在")
//    @GetMapping("/tableExists")
//    public CommonResultVo tableExists(String tableName) throws IOException {
//        boolean flag = hbaseService.tableExists(tableName);
//        return CommonResultVo.success(flag);
//    }
 
    @ApiOperation(value = "高级条件查询")
    @PostMapping("/scanRowData")
    public CommonResultVo scanRowData(@RequestBody HBaseDto hBaseDto) throws IOException {
        return hbaseService.scanRowData(hBaseDto);
    }
 
    @ApiOperation(value = "分页查询")
    @PostMapping("/scanPageRow")
    public CommonResultVo scanPageRow(@RequestBody HBaseDto hBaseDto) throws IOException {
        return hbaseService.scanPageRow(hBaseDto);
    }
//
//    @ApiOperation(value = "总行数")
//    @PostMapping("/countRow")
//    public CommonResultVo countRow(@RequestBody HBaseDto hBaseDto) throws IOException {
//        return CommonResultVo.success(hbaseService.countRow(hBaseDto));
//    }
 
 
}