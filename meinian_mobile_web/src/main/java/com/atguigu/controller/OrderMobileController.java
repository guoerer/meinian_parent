package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;


import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 订单控制层，用于处理订单的相关操作
 *
 * @author lijian
 * @create 2021-01-13 19:00
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference      //提交预约表单的逻辑验
    OrderService orderService;

    @Autowired  //用于校验验证码
    JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        try {
            //1、validateCode 前端验证码的属性值.获取用户信息，查看验证码是否正确
            String validateCode = (String) map.get("validateCode");//前端输入的验证码
            String telephone = map.get("telephone").toString();//前端输入的手机号
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);//redis数据库中存入的验证码
            if (redisCode == null || !redisCode.equals(validateCode)) {//redis中验证码过期，或者redis验证码和输入的验证码不符合
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);//验证码错误，重新输入验证码
            }
            map.put("orderType",Order.ORDERTYPE_WEIXIN);
            //2、验证码校验通过，调用service层进行逻辑校验
            Result result = orderService.submitOrder(map);//返回预约的Order id供预约成功页面调用
            return result;//返回订单添加结果
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);//验证码错误，重新输入验证码

        }
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = null;
            map = orderService.findById4Detils(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);

        }


    }
}
