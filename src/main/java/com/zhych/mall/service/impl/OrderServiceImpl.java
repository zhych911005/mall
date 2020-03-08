package com.zhych.mall.service.impl;

import com.zhych.mall.service.IOrderService;
import com.zhych.mall.vo.OrderVo;
import com.zhych.mall.vo.ResponseVo;
import org.springframework.stereotype.Service;

/**
 * Created By Superman
 * Date: 2020/3/8
 * Time: 23:54
 * Description:
 */

@Service
public class OrderServiceImpl implements IOrderService {
    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收货地址的校验(总之要查出来的)

        //获取购物车, 校验(是否有商品)

        //计算总价, 只计算选中的商品

        //生成订单, 入库：order和order_item, 事务

        //减库存

        //更新购物车(选中的商品)

        //构造orderVo
        return null;
    }
}
