package com.example.cpdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cpdemo.entity.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {

}
