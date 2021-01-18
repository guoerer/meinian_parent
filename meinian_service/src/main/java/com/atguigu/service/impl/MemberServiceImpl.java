package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijian
 * @create 2021-01-14 13:17
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;

    //通过手机号查询用户是否存在
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.selectByPhone(telephone);
    }

    //向会员表中添加会员数据
    @Override
    public void addMember(Member member) {
        memberDao.addMember(member);
    }
    //遍历日期集合，通过日期查询某个日期节点的会员数据
    @Override
    public List<Integer> findMemberByDate(List<String> list) {
        List<Integer> memberCountList = new ArrayList<>();

        for (String months : list) {
           //1、获得月份的最后一天
            String regTime = DateUtils.getLastDayOfMonth(months);
            Integer memberCount=memberDao.findMemberCountBeforeDate(regTime);
            memberCountList.add(memberCount);
        }
        return memberCountList;
    }
}
