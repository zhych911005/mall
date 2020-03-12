package com.zhych.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private Integer shippingId = 5;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void createTest() {
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    private ResponseVo<OrderVo> create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("result={}", gson.toJson(responseVo));
        return responseVo;
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = orderService.list(uid, 1, 4);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid, vo.getData().getOrderNo());
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}