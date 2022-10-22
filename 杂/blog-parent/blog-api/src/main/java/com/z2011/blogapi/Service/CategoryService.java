package com.z2011.blogapi.Service;

import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.vo.CategoryVo;

public interface CategoryService {
    /**
     *
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 返回所有分类信息，表中的id和name就可以
     * @return
     */
    Result getAllCategoriesVo();

    /**
     * 返回所有分类信息，包括表中的所有字段
     * @return
     */
    Result findAllDetail();

    Result findCategoryDetailById(Long id);
}
