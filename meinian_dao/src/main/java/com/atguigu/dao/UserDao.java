package com.atguigu.dao;

import com.atguigu.pojo.User;

/**
 * @author lijian
 * @create 2021-01-15 20:39
 */
public interface UserDao {
    User findUserByUsername(String username);
}
