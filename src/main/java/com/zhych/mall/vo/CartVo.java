package com.zhych.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By Superman
 * Date: 2020/3/3
 * Time: 22:54
 * Description:
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
