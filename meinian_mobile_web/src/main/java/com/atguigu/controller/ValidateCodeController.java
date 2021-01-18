package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.utils.SMSUtils;
import com.atguigu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**发送手机验证码控制类,发送验证码。
 * 将验证码存到redis数据库中（有效期5分钟）-进行验证。
 * @author lijian
 * @create 2021-01-12 20:28
 */
@RequestMapping("/validateCode")
@RestController
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;

    /**
     * 向用户的手机号发送验证码
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        System.out.println("用户的手机号："+telephone);
        //1、调用工具类。产生4位随机验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        String codeString = code.toString();
        //2、调用鼎新的短信发送工具类，传入手机号和验证码
        try {
            SMSUtils.sendShortMessage(telephone,codeString);
        } catch (Exception e) {
            //发送短信失败
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //3、向redis服务器中添加对应的电话号（key）和验证码（code）.设置过期时间是为5分钟
        //  SENDTYPE_ORDER = "001";//用于缓存旅游预约时发送的验证码
        //  SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
        //  SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,codeString);
        //4、返回验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 快速注册功能发送验证码给手机
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){

        //1、调用工具类。产生4位随机验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        String codeString = code.toString();
        //2、调用鼎新的短信发送工具类，传入手机号和验证码
        try {
            SMSUtils.sendShortMessage(telephone,codeString);
        } catch (Exception e) {
            //发送短信失败
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //3、向redis服务器中添加对应的电话号（key）和验证码（code）.设置过期时间是为5分钟
        //  SENDTYPE_ORDER = "001";//用于缓存旅游预约时发送的验证码
        //  SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
        //  SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,codeString);
        //4、返回验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
