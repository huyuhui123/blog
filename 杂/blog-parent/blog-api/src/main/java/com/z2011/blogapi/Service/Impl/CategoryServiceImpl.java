package com.z2011.blogapi.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.z2011.blogapi.Service.CategoryService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.dao.mapper.CategoryMapper;
import com.z2011.blogapi.dao.pojo.Category;
import com.z2011.blogapi.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result getAllCategoriesVo() {
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(categoryWrapper);
        List<CategoryVo> categoryVo=copyList(categories);
        return Result.SUCCESS(categoryVo);
    }
    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(categoryWrapper);
        List<CategoryVo> categoryVo=copyList(categories);
        return Result.SUCCESS(categoryVo);
    }

    @Override
    public Result findCategoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.SUCCESS(copy(category));
    }



    private List<CategoryVo> copyList(List<Category> categories) {
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categories) {
            categoryVos.add(copy(category));
        }
        return categoryVos;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(category.getId().toString());
        return categoryVo;
    }



}
