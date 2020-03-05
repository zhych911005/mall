package com.zhych.mall.controller;


import com.zhych.mall.consts.MallConst;
import com.zhych.mall.form.UserLoginForm;
import com.zhych.mall.form.UserRegisterForm;
import com.zhych.mall.pojo.User;
import com.zhych.mall.service.IUserService;
import com.zhych.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo register(@Valid @RequestBody UserRegisterForm userRegisterForm) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session) {
        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        //设置session
        session.setAttribute(MallConst.CURRENT_USER, userResponseVo.getData());
        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session) {
        log.info("/user/logout sessionId={}",session.getId());
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }
}