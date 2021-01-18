package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-11 19:03
 */
public interface OrdersettingService {
    void add(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

     void editNumberByDate(OrderSetting orderSetting);
}
