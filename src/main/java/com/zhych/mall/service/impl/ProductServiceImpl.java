package com.zhych.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhych.mall.dao.ProductMapper;
import com.zhych.mall.enums.ProductStatusEnum;
import com.zhych.mall.enums.ResponseEnum;
import com.zhych.mall.pojo.Product;
import com.zhych.mall.service.ICategoryService;
import com.zhych.mall.service.IProductService;
import com.zhych.mall.vo.ProductDetailVo;
import com.zhych.mall.vo.ProductVo;
import com.zhych.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                }).collect(Collectors.toList());

        /*List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(productList, productVo);
        List<ProductVo> productVoList = new ArrayList<>();
        productVoList.add(productVo);*/

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //对确定性条件的判断
        if (product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode()) || product.getStatus().equals(ProductStatusEnum.DELETE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        //敏感数据的处理
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return ResponseVo.success(productDetailVo);
    }
}