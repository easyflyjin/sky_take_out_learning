package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺营业状态")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation(value = "setShopStatus")
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("setShopStatus:{}",status == 1?"Working":"Resting");
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }


    @GetMapping("/status")
    @ApiOperation("getShopStatus")
    public Result<Integer> getShopStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("getShopStatus:{}", shopStatus);
        return Result.success(shopStatus);
    }

}
