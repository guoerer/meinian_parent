package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * spring security:spring安全框架。用户实现层
 *
 * @author lijian
 * @create 2021-01-15 19:52
 */
@Component      //普通层注解通过spring配置文件管理该bean对象。
public class SpringSecurityUserService implements UserDetailsService {
    @Reference      //注意：此处要通过dubbo远程调用用户服务
    private UserService userService;

    //根据用户名查询用户信息，设置用户权限。根据不同的用户表设计不同的权限。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1、调用用户服务，根据用户名从数据库查询用户信息。根据用户的角色判断用户的权限。
        User user = userService.findUserByUsername(username);
        if (user == null) {//用户名不存在，抛出异常信息交给框架处理
            return null;
        }
        //2、获得权限集合向中添加用户的权限
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        Set<Role> roles = user.getRoles();//一个用户可以有多个角色。获得用户表中用户所有的角色，sql中两表联查
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();//一个角色可以有多个权限，获得某个角色拥护的所有权限
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));//向用于权限集合中添加权限
            }
        }
        /**
         * User(),返回用户给框架进行管理
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限  */
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }
}
