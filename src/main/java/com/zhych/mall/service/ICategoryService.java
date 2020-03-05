package com.zhych.mall.service;

import com.zhych.mall.vo.CategoryVo;
import com.zhych.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    ResponseVo<List<CategoryVo>> selectAll();

    void  findSubCategoryId(Integer id, Set<Integer> resultSet);
}