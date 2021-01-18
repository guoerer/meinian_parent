package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-13 19:11
 */
public interface OrderService {
    Result submitOrder(Map map) throws Exception;

    Map findById4Detils(Integer id) throws Exception;
}
