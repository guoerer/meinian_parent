package com.atguigu.service;

import com.atguigu.pojo.Member;

import java.util.List;

/**
 * @author lijian
 * @create 2021-01-14 12:58
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void addMember(Member member);

    List<Integer> findMemberByDate(List<String> list);
}
