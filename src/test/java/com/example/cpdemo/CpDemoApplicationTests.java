package com.example.cpdemo;

import com.example.cpdemo.mapper.TestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CpDemoApplicationTests {
    @Autowired
    TestMapper testMapper;
    @Test
    void contextLoads()  {

    }
    @Test
    void test1(){
      testMapper.selectById("103");
    }
}
