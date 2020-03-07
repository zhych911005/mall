package com.zhych.mall.form;

import lombok.Data;

/**
 * Created By Superman
 * Date: 2020/3/7
 * Time: 14:51
 * Description: 购物车更新的表单
 */

@Data
public class CartUpdateForm {

    private Integer quantity;

    private Boolean selected;
}
