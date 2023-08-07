package com.xuecheng.system.controller;


import com.xuecheng.system.dao.po.DictionaryPO;
import com.xuecheng.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "查询系统字典接口")
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/dictionary/all")
    @ApiOperation("字典查询接口")
    public List<DictionaryPO> GetDictionAll(){
        return dictionaryService.selectDictionaryAll();
    }

    @GetMapping("dictionary/code/{code}")
    @ApiOperation("根据字典代码查询字典信息")
    public DictionaryPO GetDictionaryByCode(@PathVariable String code){
        return dictionaryService.selectDictionaryById(code);
    }

}
