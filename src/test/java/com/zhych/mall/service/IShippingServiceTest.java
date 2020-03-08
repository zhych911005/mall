package com.zhych.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhych.mall.MallApplicationTests;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.form.ShippingForm;
import com.zhych.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Slf4j
public class IShippingServiceTest extends MallApplicationTests {
    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private ShippingForm form;

    private Integer shippingId = 7;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void before() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("张建");
        form.setReceiverAddress("大学路康复前街");
        form.setReceiverCity("郑州");
        form.setReceiverMobile("188");
        form.setReceiverPhone("0371");
        form.setReceiverZip("450000");
        form.setReceiverDistrict("二七区");
        form.setReceiverProvince("河南");
        this.form = form;
    }

    @Test
    public void add() {
        log.info("form={}", form);
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("responseVo={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void delete() {
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("responseVo={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("商丘");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("responseVo={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid, 1, 10);
        log.info("responseVo={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}