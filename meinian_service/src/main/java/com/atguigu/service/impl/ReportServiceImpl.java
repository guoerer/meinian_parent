package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.service.ReportService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @create 2021-01-16 20:33
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //1、获得当前日期
        String today= DateUtils.parseDate2String(DateUtils.getToday());
        resultMap.put("reportDate",today);
        //2、获得这一天新增的会员数据
        Integer todayMember=memberDao.getTodayNewMember(today);
        resultMap.put("todayNewMember",todayMember);
        //3、获得总会员数
        Integer totalMember=memberDao.getTotalMember(today);
        resultMap.put("totalMember",totalMember);
        //4、本周新增会员数
        String weekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//周一
        String weekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());//周日
        Integer thisWeekNewMember=memberDao.getThisWeekAndMonthNewMember(weekMonday);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        //5、本月新增会员数
        String monthFirst = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//1号
        String monthLast = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());//每月最后一号
        int thisMonthNewMember = memberDao.getThisWeekAndMonthNewMember(monthFirst);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        //6、今日预约数
        int todayOrderNumber = orderDao.getTodayOrderNumber(today);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        //今日出游数
        int todayVisitsNumber = orderDao.getTodayVisitsNumber(today);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        // （7）本周预约数
        Map<String,Object> paramWeek = new HashMap<String,Object>();
        paramWeek.put("begin",weekMonday);
        paramWeek.put("end",weekSunday);
        int thisWeekOrderNumber = orderDao.getThisWeekAndMonthOrderNumber(paramWeek);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        // （9）本月预约数
        Map<String,Object> paramMonth = new HashMap<String,Object>();
        paramMonth.put("begin",monthFirst);
        paramMonth.put("end",monthLast);
        int thisMonthOrderNumber = orderDao.getThisWeekAndMonthOrderNumber(paramMonth);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        // （8）本周出游数
        Map<String,Object> paramWeekVisit = new HashMap<String,Object>();
        paramWeekVisit.put("begin",weekMonday);
        paramWeekVisit.put("end",weekSunday);
        int thisWeekVisitsNumber = orderDao.getThisWeekAndMonthVisitsNumber(paramWeekVisit);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        // （10）本月出游数
        Map<String,Object> paramMonthVisit = new HashMap<String,Object>();
        paramMonthVisit.put("begin",monthFirst);
        paramMonthVisit.put("end",monthLast);
        int thisMonthVisitsNumber = orderDao.getThisWeekAndMonthVisitsNumber(paramMonthVisit);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        // （11）热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();
        resultMap.put("hotSetmeal",hotSetmeal);


        return resultMap;
    }
}
