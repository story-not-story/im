package com.example.im.service;

import com.example.im.entity.Member;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 12:59 下午
 */
public interface MemberService {
    Member add(Member member);
    void remove(String groupId, String userId);
    Member updateName(String groupId, String userId, String name);
    Member updateGrade(String groupId, String userId, byte grade);
    List<Member> findByUserId(String userId);
    List<Member> findByGroupId(String groupId);
}
