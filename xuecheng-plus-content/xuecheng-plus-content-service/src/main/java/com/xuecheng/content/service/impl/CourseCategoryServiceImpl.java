package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.ICourseCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author RShL
 * @since 2023-08-07
 */
@Service
public class CourseCategoryServiceImpl implements ICourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryDto> queryTreeNodes(String id) {
        List<CourseCategoryDto> categoryDtoList = courseCategoryMapper.selectTreeNodes();
        List<CourseCategoryDto> result = new ArrayList<>();
        Map<String, CourseCategoryDto> map = new HashMap<>();
        categoryDtoList.forEach(item -> {
            if (item.getParentid().equals(id)) {
                map.put(item.getId(), item);
                result.add(item);
            }
            String parentId = item.getParentid();
            CourseCategoryDto parentNode = map.get(parentId);
            if (parentNode != null) {
                List<CourseCategoryDto> childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null) {
                    parentNode.setChildrenTreeNodes(new ArrayList<CourseCategoryDto>());
                }
                parentNode.getChildrenTreeNodes().add(item);
            }
        });
        return result;
    }
}
