package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-05 19:53
 */
public interface TravelGroupService {
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelgroupId(Integer id);

    void edit(TravelGroup travelGroup, Integer[] travelItemIds);

    void delete(Integer id);

    List<TravelGroup> findAll();
}
