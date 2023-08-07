package com.xuecheng.system.service;


import com.xuecheng.system.dao.po.DictionaryPO;

import java.util.List;

public interface DictionaryService {

    List<DictionaryPO> selectDictionaryAll();

    DictionaryPO selectDictionaryById(String code);
}
