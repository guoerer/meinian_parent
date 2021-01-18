package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**用户通过手机号块速登录系统
 * @author lijian
 * @create 2021-01-14 12:56
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 用户登录功能的实现，通过手机号和验证码进行登录。
     * 30天免登录，session存贮数据
     *
     * @return
     */
    @RequestMapping("/check")
    public Result test(@RequestBody Map map, HttpServletResponse response){
        //1、获取用户的手机号 和验证码，验证码和redis中验证码进行比较
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //2、获取redis数据库中的验证码
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (redisCode==null||!redisCode.equals(validateCode)){
            //redis 数据库中没有验证码或验证码不符合。返回验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //3、根据id查询该用户是否已经是会员吗  不是注册成会员调用service层向数据库中添加数据
        Member member=memberService.findByTelephone(telephone);
        if (member==null){
            member=new Member();
            member.setPhoneNumber(telephone);//电话号
            member.setRegTime(new Date());//注册时间
            memberService.addMember(member);

        }
        //4、登录成功，写入session数据。实现免登录
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");//设置路径
        cookie.setMaxAge(60*60*24*30);//设置30天免登录
        response.addCookie(cookie);//添加浏览中添加cookie数据
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
