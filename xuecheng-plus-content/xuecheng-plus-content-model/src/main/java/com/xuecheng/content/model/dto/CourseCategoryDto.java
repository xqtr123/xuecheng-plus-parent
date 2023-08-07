package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategoryPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryDto extends CourseCategoryPO {

    private List<CourseCategoryDto> childrenTreeNodes;
}