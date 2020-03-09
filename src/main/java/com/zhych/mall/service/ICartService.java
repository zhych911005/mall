package com.zhych.mall.service;

import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.form.CartUpdateForm;
import com.zhych.mall.pojo.Cart;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ResponseVo;

import java.util.List;

/**
 * Created By Superman
 * Date: 2020/3/5
 * Time: 23:44
 * Description:
 */


public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);
}
