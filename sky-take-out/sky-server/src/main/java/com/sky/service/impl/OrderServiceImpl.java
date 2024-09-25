package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    private static final String USER_CANCEL_INFO = "用户取消";
    @Override
    public void cancelOrder(Long id) throws Exception{
        //查询ID是否存在
        Orders orders = orderMapper.findOrderById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //'订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款'
        //查询订单状态，2-4退款，1不退款，5，6不可取消，7已退款，无需再退款
        //如果状态是6则执行退款操作
        if(orders.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if(orders.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            weChatPayUtil.refund(orders.getNumber(),orders.getNumber(),new BigDecimal(0.01),new BigDecimal(0.01));

        }
        orders.setStatus(Orders.REFUND);
        orders.setCancelReason(USER_CANCEL_INFO);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder();
    }


}
