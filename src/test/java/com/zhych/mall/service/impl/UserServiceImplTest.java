package com.zhych.mall.service.impl;

import com.zhych.mall.MallApplicationTests;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.enums.RoleEnum;
import com.zhych.mall.pojo.User;
import com.zhych.mall.service.IUserService;
import com.zhych.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    private static final String USERNAME = "lucye";

    private static final String PASSWORD = "123456";

    @Autowired
    private IUserService userService;

    @Before
    public void register() {
        User user = new User(USERNAME, PASSWORD, "lucy1@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }

    @Test
    public void login() {
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}