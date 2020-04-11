package com.example.im.service;

import com.example.im.entity.Invitation;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
public interface InvitationService {
    /**
     * 根据id查找添加好友申请
     * @param id
     * @return
     */
    Invitation findById(String id);

    /**
     * 根据userId查找添加好友申请列表（发送者和接受者）
     * @param userId
     * @return
     */
    List<Invitation> findByUserId(String userId);

    /**
     * 接收添加好友申请
     * @param id
     * @return
     */
    Invitation accept(String id);

    /**
     * 拒绝添加好友申请
     * @param id
     * @return
     */
    Invitation reject(String id);

    /**
     * 保存添加好友申请
     * @param invitation
     * @return
     */
    Invitation create(Invitation invitation);
}
