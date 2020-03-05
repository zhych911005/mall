package com.zhych.mall.service;

import com.github.pagehelper.PageInfo;

import com.zhych.mall.vo.ProductDetailVo;
import com.zhych.mall.vo.ResponseVo;

public interface IProductService {

    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail( Integer productId);
}
