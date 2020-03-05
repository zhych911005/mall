package com.zhych.mall.service;

import com.zhych.mall.pojo.User;
import com.zhych.mall.vo.ResponseVo;

public interface IUserService {
    //注册
    ResponseVo<User> register(User user);

    //登录
    ResponseVo<User> login(String username, String password);
}