package com.leyou.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.client.GoodsClient;
import com.leyou.common.utils.JsonUtils;
import com.leyou.interceptor.LoginInterceptor;
import com.leyou.item.pojo.Sku;
import com.leyou.pojo.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "leyou:cart:userId";

    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // Redis的key
        String key = KEY_PREFIX + user.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = template.boundHashOps(key);
        //查询购物车是否已经存在此商品
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean hasKey = hashOps.hasKey(skuId.toString());
        if (hasKey) {
            // 如果购物车中已经存在此商品，则更改num属性
            String oldCart = Objects.requireNonNull(hashOps.get(skuId.toString())).toString();
            cart = JsonUtils.parse(oldCart, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            // 如果购物车中不存在此商品，则新增
            cart.setUserId(user.getId());

            Sku sku = goodsClient.querySkuBySkuId(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }

        // 将购物车数据写入redis
        hashOps.put(cart.getSkuId().toString(), Objects.requireNonNull(JsonUtils.serialize(cart)));
    }

    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if (!template.hasKey(key)) {
            // 如果不存在购物车，返回null
            return null;
        } else {
            // 在redis中查询购物车数据
            BoundHashOperations<String, Object, Object> hashOps = template.boundHashOps(key);
            List<Object> carts = hashOps.values();
            // 如果购物车为空，返回null
            if (CollectionUtils.isEmpty(carts)) {
                return null;
            }
            // 如果购物车不为空，将查询到的购物车数据转换为Cart对象，并返回
            return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
        }
    }

    public void updateCarts(Cart cart) {
        UserInfo user = LoginInterceptor.getLoginUser();
        Long id = user.getId();

        String key = KEY_PREFIX + id;

        BoundHashOperations<String, Object, Object> hashOps = template.boundHashOps(key);
        String oldCartJson = hashOps.get(cart.getSkuId().toString()).toString();
        Cart oldCart = JsonUtils.parse(oldCartJson, Cart.class);
        oldCart.setNum(cart.getNum());
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(oldCart));
    }

    public void deleteCart(String skuId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();

        BoundHashOperations<String, Object, Object> hashOps = template.boundHashOps(key);
        hashOps.delete(skuId);

    }
}
