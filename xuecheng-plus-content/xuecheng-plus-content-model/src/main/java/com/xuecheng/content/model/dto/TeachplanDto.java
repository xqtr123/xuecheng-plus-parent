package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.TeachplanMediaPO;
import com.xuecheng.content.model.po.TeachplanPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeachplanDto extends TeachplanPO {

    private TeachplanMediaPO teachplanMediaPO;

    private List<TeachplanDto> teachplanTreeNodes;

}
