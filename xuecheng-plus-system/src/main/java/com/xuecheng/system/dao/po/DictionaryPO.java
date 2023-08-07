package com.xuecheng.system.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dictionary")
public class DictionaryPO implements Serializable {

    private static final long serializableId = 1L;

    /**
     * id标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典代码
     */
    private String code;

    /**
     * 数据字典项--json格式
     */
    private String itemValues;
}
