package com.sky.service.impl;

import com.sky.mapper.OrderMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void cancelOrder(Long id) {
        //查询ID是否存在
        HttpClientUtil httpClientUtil = new HttpClientUtil();


        //根据

        //退款
    }
}
