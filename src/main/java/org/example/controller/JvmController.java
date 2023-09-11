package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/jvm")
@Slf4j
public class JvmController {
    List<User> userArrayList = new ArrayList<>();
    @GetMapping("/newObject")
    public String newObject(int count){
        for (int i = 0; i < count; i++) {
            log.info("创建user对象："+i);
            userArrayList.add(new User());
        }
        return "success";
    }
}
