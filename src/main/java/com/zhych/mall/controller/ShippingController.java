package com.zhych.mall.controller;

import com.zhych.mall.consts.MallConst;
import com.zhych.mall.form.ShippingForm;
import com.zhych.mall.pojo.User;
import com.zhych.mall.service.impl.ShippingServiceImpl;
import com.zhych.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created By Superman
 * Date: 2020/3/8
 * Time: 23:25
 * Description:
 */

@RestController
public class ShippingController {

    @Autowired
    private ShippingServiceImpl shippingService;

    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestParam ShippingForm form, HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.add(user.getId(), form);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId, HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.delete(user.getId(), shippingId);
    }


    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId, @Valid @RequestParam ShippingForm form, HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.update(user.getId(), shippingId, form);
    }

    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}