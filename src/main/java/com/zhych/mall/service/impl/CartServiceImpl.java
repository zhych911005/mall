package com.zhych.mall.service.impl;

import com.google.gson.Gson;
import com.zhych.mall.consts.MallConst;
import com.zhych.mall.dao.ProductMapper;
import com.zhych.mall.enums.ProductStatusEnum;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.form.CartAddForm;
import com.zhych.mall.form.CartUpdateForm;
import com.zhych.mall.pojo.Cart;
import com.zhych.mall.pojo.Product;
import com.zhych.mall.service.ICartService;
import com.zhych.mall.vo.CartProductVo;
import com.zhych.mall.vo.CartVo;
import com.zhych.mall.vo.ProductVo;
import com.zhych.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, uid);
        Cart cart;
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            //没有该商品，新增
            cart = new Cart(product.getId(), quantity, form.getSelected());
        } else {
            //已经存在该商品，数量+1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            //TODO 需要优化，使用mysql里的in
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

                //计算的是选中的总价
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }
            cartTotalQuantity += cart.getQuantity();
        }
        //有一个没有选中，就不叫全选
        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, uid);
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经存在该商品，修改内容
        Cart cart = gson.fromJson(value, Cart.class);
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(MallConst.CART_REDIS_KEY_TEMPLATE, uid);
        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经存在该商品，修改内容
        opsForHash.delete(redisKey, String.valueOf(productId));

        return list(uid);
    }
}