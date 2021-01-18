package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrdersettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-11 19:03
 */
@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {
    //spring的自动装配
    @Autowired
    private OrdersettingDao ordersettingDao;

    /**
     * 1、service层。遍历数据添加数据
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        //1、遍历list<orderSetting>集合
        for (OrderSetting orderSetting : orderSettings) {
            //1、查询日期是否已经被添加到数据库中。添加过编辑操作、没有新增数据
            int count = ordersettingDao.selectByDate(orderSetting.getOrderDate());
            if (count>0){   //编辑操作
                ordersettingDao.edit(orderSetting);
            }else {     //新增数据
                ordersettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 查询某个月的所有的预约信息
     * @param date 指定的某年某月  ：如2021-1
     * @return list<map> 返回封装好的date月的所有的数据。
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //1| sql语句中实现范围查询，查询从当前月1号到31号的所有数据
        //查询条件
        String dateBegin=date+"-1";//2021-1-1
        String dateEnd=date+"-31";//2021-1-31
        Map<String, Object> map = new HashMap<>();
        map.put("dateBegin",dateBegin); //每月一号
        map.put("dateEnd",dateEnd); //每月最后一号
        //2、查询当前月预约设置，返回一个orderSetting的list集合
        List<OrderSetting>  orderList=ordersettingDao.getOrderSettingByMonth(map);
        //2.1  map的list集合用于存贮map集合使用
        List<Map> data = new ArrayList<>();
        //3、将orderSetting的list集合改写成map的list集合返回给前端页面
        for (OrderSetting orderSetting : orderList) {
            //map，键值对形式：每一个map就是一个orderSetting 对象
            Map<String, Object>  orderMapList= new HashMap<>();
            orderMapList.put("date",orderSetting.getOrderDate().getDate());    //获取精确到天的日期
            orderMapList.put("number",orderSetting.getNumber());     //获取某天的可预约人数
            orderMapList.put("reservations",orderSetting.getReservations()); //获取某天的已预约人数
            data.add(orderMapList); //向map的list集合中添加数据
        }

        return data;    //返回date日期当前月的所有预约设置
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1、查询日期是否已经被添加到数据库中。添加过编辑操作、没有新增数据
        int count = ordersettingDao.selectByDate(orderSetting.getOrderDate());
        if (count>0){   //编辑操作
            ordersettingDao.edit(orderSetting);
        }else {     //新增数据
            ordersettingDao.add(orderSetting);
        }
    }
}
