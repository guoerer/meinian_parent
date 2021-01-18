package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrdersettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-13 19:35
 */
@Service(interfaceClass = OrderService.class)
@Transactional      //事务特性
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersettingDao ordersettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Result submitOrder(Map map) throws Exception {
        /*
        判断这天是否可以进行预约：1、这天是否存在。2、存在的话，这天预约的人数是否已满
         */
        //1、判断当前日期是否可以进行预约,将从前台得到的日期转换成date类型（orderSetting表中时日期是date类型），
        //调用orderSetting的dao 层方法查询该表中是否有值。有值可以进行预约，没有该天没有旅游项目，不能进行预约。
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        int count = ordersettingDao.selectByDate(date);
        if (count > 0) {// 这天可以进行旅游预约
            //2、判断预约人数是否已满，已满说明不能预约
            Boolean numberFlag = checkNumber(date);
            if (!numberFlag){
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        } else {//这天没有项目
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

      /*
      判断用户：1、是否存在该用户  2、存在该用户的话，该用户是否已经预约了（用户id,预约时间，预约的套餐）
      没有进行设置预约信息。
       */
        Member member = checkMember((String) map.get("telephone"));
        if (member==null){//不存在该用户，向用户表中添加数据
            member=new Member();
            member.setIdCard((String) map.get("idCard"));//身份证号
            member.setPhoneNumber((String) map.get("telephone"));//手机号
            member.setRegTime(new Date());//注册时间
            member.setSex((String) map.get("sex"));//用户性别
            member.setName((String) map.get("name"));//用户姓名
            memberDao.addMember(member);//向数据库中添加用户信息
        }else {//这条手机号中存在该用户,查询该用户是否是重复下单
            Integer memberId = member.getId();//用户id
            String setmealIds= (String) map.get("setmealId");//套餐id
            int setmealId = Integer.parseInt(setmealIds);
            //向order表中查询是否已经存在该数据
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(date);
            order.setSetmealId(setmealId);
            List<Order> orderList=orderDao.findOrder(order);
            //如果list查询出来的数据不为空，说明用户已经下了订单
            if (orderList!=null&&orderList.size()>0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        /*
        用户能下单了，向order 表中添加数据
                    orderSetting 表中预约数据人数加1
         */
        //向orderSetting中添加数据,已预约人数加1
        int reservationsNumber = ordersettingDao.findReservationsByDate(date);
        int resNumber=reservationsNumber+1;
        ordersettingDao.editReservationsByDate(resNumber,date);
        //向order表中添加数据
        Order order = new Order();
        order.setMemberId(member.getId()); //会员id
        order.setOrderDate(date); // 预约时间
        order.setOrderStatus(Order.ORDERSTATUS_NO); // 预约状态（已出游/未出游）
        order.setOrderType((String)map.get("orderType"));//预约的类型（电话、微信）
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));//预约的套餐id
        orderDao.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据id查询预约信息，包括旅游人信息和套餐信息
     * @param id
     * @return
     */
    @Override
    public Map findById4Detils(Integer id) throws Exception {
        Map map = orderDao.findByIdDetils(id);
        if (map!=null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }

    /**
     * 根据用户手机号检查是否已经注册过,注册过：检查是否已经预约，防止重复预约。
     * 没有注册过：注册用户。
     *
     * @param telephone
     */
    private Member checkMember(String telephone) {
        Member member = memberDao.selectByPhone(telephone);
        return member;
    }

    /**
     * 检查是否当天预约人数已满,已满返回false .未满返回true。     *
     * @param
     * @return
     */
    public Boolean checkNumber(Date date) {
        int number = ordersettingDao.findNumberByDate(date);//总人数
        int reservations = ordersettingDao.findReservationsByDate(date);//已经预约人数
        return number > reservations;//预约没满，可以进行预约
    }
}
