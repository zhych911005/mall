package com.zhych.mall.pojo;

import lombok.Data;

/**
 * Created By Superman
 * Date: 2020/3/6
 * Time: 0:16
 * Description:
 */

@Data
public class Cart {
    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}