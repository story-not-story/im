package com.example.im.service;

import com.example.im.entity.GroupInvitation;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
public interface GroupInvitationService {
    /**
     * 根据id查找邀好友加入群聊的邀请
     * @param id
     * @return
     */
    GroupInvitation findById(String id);

    /**
     * 根据receiverId查找邀好友加入群聊的邀请
     * @param receiverId
     * @return
     */
    List<GroupInvitation> findByReceiverId(String receiverId);

    /**
     * 同意邀好友加入群聊的邀请
     * @param id
     * @return
     */
    GroupInvitation accept(String id);

    /**
     * 拒绝邀好友加入群聊的邀请
     * @param id
     * @return
     */
    GroupInvitation reject(String id);

    /**
     * 保存邀好友加入群聊的邀请
     * @param invitation
     * @return
     */
    GroupInvitation create(GroupInvitation invitation);
}
