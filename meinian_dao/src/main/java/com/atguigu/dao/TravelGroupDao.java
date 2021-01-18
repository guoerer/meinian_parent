package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-05 20:01
 */
public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(Map<String, Integer> map);

    Page<TravelGroup> findPage(String queryString);

    TravelGroup findById(Integer id);

    void edit(TravelGroup travelGroup);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    void delteTravelItemByGroupId(Integer id);

    void delteById(Integer id);

    List<TravelGroup> findAll();
    //根据套餐的id查询，关联的团体游数据的集合
    List<TravelGroup> findTravelGroupListById(String SetmealId);

}
