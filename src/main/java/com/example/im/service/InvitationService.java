package com.example.im.service;

import com.example.im.entity.Invitation;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
public interface InvitationService {
    Invitation findById(String invitationId);
    List<Invitation> findByReceiverId(String receiverId);
    Invitation accept(String invitationId);
    Invitation reject(String invitationId);
    Invitation create(Invitation invitation);
}
