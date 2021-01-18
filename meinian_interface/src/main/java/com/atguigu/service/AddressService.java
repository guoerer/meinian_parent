package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-17 14:28
 */
public interface AddressService {
    public List<Address> findAllMaps() ;

    PageResult findPage(QueryPageBean queryPageBean);

    void addAddress(Address address);

    void deletById(Integer id);
}
