package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 该类模拟nginx转发post请求问题// TODO
 * 未完成完整模拟
 */
@RestController()
public class NginxController {
    /**
     * 普通的get请求，可以通过nginx
     * @return
     */
    @GetMapping("/get1")
    public String get1(){
        return "请求成功";
    }
    /**
     * 不带参数普通post请求测试，可以通过nginx
     * @return
     */
    @PostMapping("/post1")
    public String post1(){
        return "请求成功";
    }
    /**
     * 带restfull参数请求，没有问题
     */
    @PostMapping("/post2/")
    public String post2(){
        return "请求成功:";
    }
    /**
     * 带restfull参数请求的get请求
     * 最终发现为ResponseEntity的问题
     */
    @PostMapping("/post3")
    public ResponseEntity<String> post3(){
        ResponseEntity<String> forEntity = new ResponseEntity<>(HttpStatus.OK);
        return forEntity;
    }

    /**
     * 最终解决办法
     */
    @PostMapping("/post4")
    public Object post4(){
        ResponseEntity<String> forEntity = new ResponseEntity<>("{\"a\":\"b\"}",HttpStatus.OK);
        return JSONObject.parseObject(forEntity.getBody());
    }
}
