package com.zhych.mall.service;

import com.zhych.mall.MallApplicationTests;
import com.zhych.mall.form.CartAddForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By Superman
 * Date: 2020/3/6
 * Time: 0:32
 * Description:
 */


public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        cartService.add(1, form);
    }
}