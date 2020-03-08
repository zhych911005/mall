package com.zhych.mall.service;

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
}