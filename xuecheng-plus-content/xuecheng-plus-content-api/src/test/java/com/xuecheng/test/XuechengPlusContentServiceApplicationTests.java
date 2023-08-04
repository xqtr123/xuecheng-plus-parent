package com.xuecheng.test;


import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.po.CourseBasePO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class XuechengPlusContentServiceApplicationTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    void contextLoads(){
        CourseBasePO courseBasePO = courseBaseMapper.selectById(22);
        log.info("查询到数据：{}", courseBasePO);
        Assertions.assertNotNull(courseBasePO);
    }
}
