package com.zhych.mall.enums;

import lombok.Getter;

/**
 * Created By Superman
 * Date: 2020/3/10
 * Time: 0:57
 * Description:
 */

@Getter
public enum PaymentTypeEnum {

    PAY_ONLINE(1),
    ;

    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}