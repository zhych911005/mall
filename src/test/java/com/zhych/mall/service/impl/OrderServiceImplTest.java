package com.zhych.mall.service.impl;

import com.zhych.mall.MallApplicationTests;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.service.IOrderService;
import com.zhych.mall.vo.OrderVo;
import com.zhych.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By Superman
 * Date: 2020/3/9
 * Time: 23:38
 * Description:
 */

@Slf4j
public class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;

    private Integer uid = 1;

    private Integer shippingId = 6;

    @Test
    public void create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("responseVo={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}