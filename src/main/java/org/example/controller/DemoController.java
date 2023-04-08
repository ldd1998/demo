package org.example.controller;

import org.example.mapper.UserMapper;
import org.example.entity.User;
import org.example.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author ldd
 */
@RestController
public class DemoController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommonService commonService;
    @GetMapping("/mybatis")
    public String mybatisTest() {
        User u = new User();
        u.setName("lll");
        u.setAge(12);
        userMapper.insert(u);
        return "操作成功";
    }
    @GetMapping("/threadInsertUser")
    public String threadInsertUser(){
        commonService.threadInsertUser();
        return "操作成功";
    }

    @GetMapping("/insertUser")
    public String insertUser(){
        commonService.insertUser();
        return "操作成功";
    }
    @GetMapping("jdbcInsert")
    public String jdbcInsert(){
        commonService.jdbcInsert();
        return "操作成功";
    }
    @GetMapping("jdbcInsertThread")
    public String jdbcInsertThread(){
        commonService.jdbcInsetThread();
        return "操作成功";
    }
    @GetMapping("jdbcInsetThreadTrans")
    public String jdbcInsetThreadTrans(){
        commonService.jdbcInsetThreadTrans();
        return "操作成功";
    }
    @GetMapping("jdbcBatchInsert")
    public String jdbcBatchInsert(){
        commonService.jdbcBatchInsert();
        return "操作成功";
    }

    @GetMapping("/test1")
    public String test1(){
        return "操作成功1";
    }
    @GetMapping("/testPage")
    public Object testPage(int page,int size){
        List<User> users = commonService.selectUserByPage(page, size);
        return users;
    }
}
