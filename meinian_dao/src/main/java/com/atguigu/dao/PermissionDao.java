package com.atguigu.dao;

import com.atguigu.pojo.Permission;

import java.util.Set;

/**
 * @author lijian
 * @create 2021-01-15 21:02
 */
public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
