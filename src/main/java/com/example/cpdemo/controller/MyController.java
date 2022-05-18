package com.example.cpdemo.controller;

import com.example.cpdemo.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    TestMapper testMapper;
    @GetMapping("/test")
    String getById(){
        return testMapper.selectById("104").toString();
    }
}
