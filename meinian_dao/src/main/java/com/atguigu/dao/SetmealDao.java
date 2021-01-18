package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-06 22:41
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> getSetmealAll();

    Setmeal findDetailsById(String id);

    Setmeal getSetmealById(String id);

    List<Map<String, Object>> findSetmealCount();
}
