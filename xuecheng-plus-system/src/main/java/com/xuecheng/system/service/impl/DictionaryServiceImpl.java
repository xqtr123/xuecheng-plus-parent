package com.xuecheng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.system.dao.mapper.DictionaryMapper;
import com.xuecheng.system.dao.po.DictionaryPO;
import com.xuecheng.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public List<DictionaryPO> selectDictionaryAll() {

        return dictionaryMapper.selectList(null);
    }

    @Override
    public DictionaryPO selectDictionaryById(String code) {
        LambdaQueryWrapper<DictionaryPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryPO::getCode, code);
        return dictionaryMapper.selectOne(queryWrapper);
    }
}
