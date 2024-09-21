package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@Api(tags = "店铺营业状态")
@RequestMapping("/user/shop")
public class ShopController {
    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("getShopStatus")
    public Result<Integer> getShopStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("getShopStatus:{}", shopStatus);
        return Result.success(shopStatus);
    }

//    @ApiOperation(value = "setShopStatus")
//    @PutMapping("/{status}")
//    public Result setShopStatus(@PathVariable Integer status){
//        log.info("setShopStatus:{}",status == 1?"Working":"Resting");
//        redisTemplate.opsForValue().set("SHOP_STATUS",statu   s);
//        return Result.success();
//    }


}
