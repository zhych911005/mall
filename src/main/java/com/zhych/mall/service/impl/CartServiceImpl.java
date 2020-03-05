package com.zhych.mall.service.impl;

import com.google.gson.Gson;
import com.zhych.mall.consts.MallConst;
import com.zhych.mall.dao.ProductMapper;
import com.zhych.mall.enums.ProductStatusEnum;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.pojo.Cart;
import com.zhych.mall.pojo.Product;
import com.zhych.mall.service.ICartService;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ResponseVo;
import org.apache.catalina.authenticator.SingleSignOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created By Superman
 * Date: 2020/3/5
 * Time: 23:46
 * Description:
 */

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {

        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(form.getProductId());
        //商品是否正常存在
        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //商品是否是正常的在售状态
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品的库存是否充足
        if (product.getStock() <= 0) {
            ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        //写入到redis KEY: cart_1
        redisTemplate.opsForValue().set(String.format(MallConst.CART_REDIS_KEY_TEMPLATE, uid),
                gson.toJson(new Cart(product.getId(), quantity, form.getSelected())));
        return null;
    }
}
