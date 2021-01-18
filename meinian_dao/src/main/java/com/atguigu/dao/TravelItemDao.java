package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-04 15:08
 */
public interface TravelItemDao {
    //添加自由行项目
    public void add(TravelItem travelItem);
    //分页查询自由行项目
    Page<TravelItem> findPage(String queryString);
    //无条件删除自由行（v 1.0）
    void delete(Integer id);
    //根据id查询自由行数据
    TravelItem findById(Integer id);
    //修改功能实现
    void edit(TravelItem travelItem);
    //删除功能前查询该数组是否存在关联数据
    int findCountByTravelItemId(Integer id);
    //查询所有自由行数据
    List<TravelItem> findAll();
    //根据团体游的id值进行关联查询自由行数据
    List<TravelItem>  findTravelItemListById();
}

