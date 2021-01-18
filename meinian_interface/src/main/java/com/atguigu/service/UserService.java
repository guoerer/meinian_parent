package com.atguigu.service;

import com.atguigu.pojo.User;

/**
 * @author lijian
 * @create 2021-01-15 19:55
 */
public interface UserService {
    User findUserByUsername(String username);
}
