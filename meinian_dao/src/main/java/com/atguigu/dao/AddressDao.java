package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-17 14:46
 */
public interface AddressDao {
    List<Address> findAllMaps();

    Page<Address> findPage(String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
