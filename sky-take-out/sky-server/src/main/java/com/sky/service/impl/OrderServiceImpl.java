package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.exception.OrderBusinessException;
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
        Orders orders = orderMapper.findOrderById(id);
        if (orders == null) {
            throw new OrderBusinessException("用户不存在，无法删除");
        }
        //'订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款'
        //查询订单状态，2-4退款，1不退款，5，6不可取消，7已退款，无需再退款
        //如果状态是6则执行退款操作



    }
}
