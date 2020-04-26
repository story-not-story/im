package com.example.im.service;

import com.example.im.entity.Member;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 12:59 下午
 */
public interface MemberService {
    /**
     *  添加群成员
     * @param member
     * @return
     */
    Member add(Member member);

    /**
     * 删除群成员
     * @param groupId
     * @param userId
     */
    void remove(String groupId, String userId);

    /**
     * 判断是否是群成员
     * @param groupId
     * @param userId
     * @return
     */
    boolean isMember(String groupId, String userId);

    /**
     * 根据groupId,userId查找群成员
     * @param groupId
     * @param userId
     * @return
     */
    Member findOne(String groupId, String userId);

    /**
     * 修改群昵称
     * @param groupId
     * @param userId
     * @param name
     * @return
     */
    Member updateName(String groupId, String userId, String name);

    /**
     * 修改群成员权限
     * @param groupId
     * @param userId
     * @param grade
     * @return
     */
    Member updateGrade(String groupId, String userId, Byte grade);

    /**
     * 根据userId查找所在群聊
     * @param userId
     * @return
     */
    List<Member> findByUserId(String userId);

    /**
     * 根据groupId查找群成员
     * @param groupId
     * @return
     */
    List<Member> findByGroupId(String groupId);
}
