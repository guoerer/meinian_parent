package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**套餐接口
 * @author lijian
 * @create 2021-01-06 21:45
 */
public interface SetmealService {

    void add(Setmeal setmeal, Integer[] travelgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> getSetmealAll();

    Setmeal findDetailsById(String id);

    Setmeal getSetmealById(String id);

    List<Map<String, Object>> findSetmealCount();
}
