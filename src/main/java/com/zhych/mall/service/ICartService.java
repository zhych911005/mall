package com.zhych.mall.service;

import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ResponseVo;

/**
 * Created By Superman
 * Date: 2020/3/5
 * Time: 23:44
 * Description:
 */


public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);
}
