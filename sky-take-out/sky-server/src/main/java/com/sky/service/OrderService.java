package com.sky.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    void cancelOrder(Long id);
}
