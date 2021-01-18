package com.atguigu.dao;

import com.atguigu.pojo.Member;

/**
 * @author lijian
 * @create 2021-01-13 19:58
 */
public interface MemberDao {
    Member selectByPhone(String telephone);

    void addMember(Member member);

    Integer findMemberCountBeforeDate(String regTime);
    //当天新增的会员
    Integer getTodayNewMember(String today);

    Integer getTotalMember(String today);

    Integer getThisWeekAndMonthNewMember(String weekMonday);
}
