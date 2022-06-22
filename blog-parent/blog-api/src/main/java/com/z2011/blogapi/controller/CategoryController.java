package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.CategoryService;
import com.z2011.blogapi.dao.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result getAllCategories(){
        return categoryService.getAllCategoriesVo();
    }
    @GetMapping("detail")
    public Result categoryDetail(){
      return   categoryService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable Long id){
        return   categoryService.findCategoryDetailById(id);
    }

}
