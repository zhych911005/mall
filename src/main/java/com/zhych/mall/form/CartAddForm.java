package com.zhych.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created By Superman
 * Date: 2020/3/3
 * Time: 23:08
 * Description:
 */

@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}