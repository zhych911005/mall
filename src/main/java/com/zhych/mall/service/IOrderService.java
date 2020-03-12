package com.zhych.mall.service;

import com.github.pagehelper.PageInfo;
import com.zhych.mall.vo.OrderVo;
import com.zhych.mall.vo.ResponseVo;

/**
 * Created By Superman
 * Date: 2020/3/8
 * Time: 23:53
 * Description:
 */


public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid, Long OrderNo);
}