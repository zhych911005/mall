package com.zhych.mall.controller;

import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By Superman
 * Date: 2020/3/3
 * Time: 23:17
 * Description:
 */

@RestController
public class CartController {

    //提交测试
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm) {
        return null;
    }
}
