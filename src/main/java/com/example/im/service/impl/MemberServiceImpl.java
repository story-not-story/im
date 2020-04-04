package com.example.im.service.impl;

import com.example.im.dao.MemberDao;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.GroupException;
import com.example.im.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 1:01 下午
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member add(Member member) {
        return memberDao.save(member);
    }

    @Override
    public void remove(String groupId, String userId) {
        Member member = memberDao.find(groupId, userId);
        if (member == null){
            log.error("【退出群成员】该群没有该成员");
            throw new GroupException(ErrorCode.MEMBER_NOT_EXISTS);
        }
        memberDao.deleteById(member.getId());
    }

    @Override
    public Member updateName(String groupId, String userId, String name) {
        Member member = memberDao.find(groupId, userId);
        if (member == null){
            log.error("【退出群成员】该群没有该成员");
            throw new GroupException(ErrorCode.MEMBER_NOT_EXISTS);
        }
        member.setName(name);
        return memberDao.save(member);
    }

    @Override
    public Member updateGrade(String groupId, String userId, byte grade) {
        Member member = memberDao.find(groupId, userId);
        if (member == null){
            log.error("【退出群成员】该群没有该成员");
            throw new GroupException(ErrorCode.MEMBER_NOT_EXISTS);
        }
        member.setGrade(grade);
        return memberDao.save(member);
    }

    @Override
    public List<Member> findByUserId(String userId) {
        return memberDao.findByUserId(userId);
    }

    @Override
    public List<Member> findByGroupId(String groupId) {
        return memberDao.findByGroupId(groupId);
    }

}
