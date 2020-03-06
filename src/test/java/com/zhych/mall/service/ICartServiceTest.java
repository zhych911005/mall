package com.zhych.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhych.mall.MallApplicationTests;
import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created By Superman
 * Date: 2020/3/6
 * Time: 0:32
 * Description:
 */

@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(29);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, form);
        log.info("responseVo={}", gson.toJson(responseVo));
    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}", gson.toJson(list));
    }
}