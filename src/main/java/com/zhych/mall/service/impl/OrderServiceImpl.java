package com.zhych.mall.service.impl;

import com.zhych.mall.dao.OrderItemMapper;
import com.zhych.mall.dao.OrderMapper;
import com.zhych.mall.dao.ProductMapper;
import com.zhych.mall.dao.ShippingMapper;
import com.zhych.mall.enums.OrderStatusEnum;
import com.zhych.mall.enums.PaymentTypeEnum;
import com.zhych.mall.enums.ProductStatusEnum;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.pojo.*;
import com.zhych.mall.service.IOrderService;
import com.zhych.mall.vo.OrderVo;
import com.zhych.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By Superman
 * Date: 2020/3/8
 * Time: 23:54
 * Description:
 */

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;


    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //******收货地址的校验(总之要查出来的)
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //******获取购物车, 校验(是否有商品)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            //根据productId查询数据库
            Product product = map.get(cart.getProductId());
            //是否有商品
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "商品不存在，productId = " + cart.getProductId());
            }

            //商品的上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品不在在售状态" + product.getName());
            }

            //库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, "库存不正确" + product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }
        //计算总价，只计算选中的商品
        //生成订单，入库：order和order_item，事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //todo
        //******更新购物车(选中的商品)

        //******构造orderVo
        return ResponseVo.success();
    }

    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList
    ) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }

    /**
     * 企业级：分布式唯一id/主键
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
