package com.atguigu.dao;

import com.atguigu.pojo.Role;

import java.util.Set;

/**
 * @author lijian
 * @create 2021-01-15 20:54
 */
public interface RoleDao {
    Set<Role> findRolesByUserId(Integer id);
}
