package com.example.cpdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_order")
public class Test {
    @TableId("id")
   private String id;
   private String userId;
   private String name;
   private String price;
   private String num;
}
