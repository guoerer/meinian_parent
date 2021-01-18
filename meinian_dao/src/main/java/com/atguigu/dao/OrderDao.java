package com.atguigu.dao;

import com.atguigu.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-13 20:53
 */
public interface OrderDao {


    List<Order> findOrder(Order order);

    void add(Order order);

    Map findByIdDetils(Integer id);

    int getTodayVisitsNumber(String today);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    List<Map<String, Object>> findHotSetmeal();

    int getTodayOrderNumber(String today);
}
