package com.xuecheng.system;

import com.xuecheng.system.dao.po.DictionaryPO;
import com.xuecheng.system.service.DictionaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CustomTest {

    @Autowired
    private DictionaryService dictionaryService;

    @Test
    void dicTest(){
        List<DictionaryPO> dictionaryPOS = dictionaryService.selectDictionaryAll();

    }
}
