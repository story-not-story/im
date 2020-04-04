package com.example.im.service.impl;

import com.example.im.dao.InvitationDao;
import com.example.im.entity.Invitation;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.service.InvitationService;
import com.example.im.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private InvitationDao invitationDao;

    @Override
    public Invitation findById(String invitationId) {
        return invitationDao.findById(invitationId).orElse(null);
    }

    @Override
    public List<Invitation> findByReceiverId(String receiverId) {
        return invitationDao.findByReceiverId(receiverId);
    }

    @Override
    public Invitation accept(String invitationId) {
        Invitation invitation = findById(invitationId);
        if (invitation == null){
            log.error("【接受好友添加申请】邀请不存在");
            throw new FriendException(ErrorCode.INVITATION_NOT_FOUND);
        }
        if (invitation.getIsAccepted()){
            log.error("【接受好友添加申请】已接受好友添加申请");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_ACCEPT);
        }
        invitation.setIsAccepted(true);
        return invitationDao.save(invitation);
    }

    @Override
    public Invitation reject(String invitationId) {
        Invitation invitation = findById(invitationId);
        if (invitation == null){
            log.error("【拒绝好友添加申请】邀请不存在");
            throw new FriendException(ErrorCode.INVITATION_NOT_FOUND);
        }
        if (!invitation.getIsAccepted()){
            log.error("【拒绝好友添加申请】已拒绝好友添加申请");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_REJECT);
        }
        invitation.setIsAccepted(false);
        return invitationDao.save(invitation);
    }

    @Override
    public Invitation create(Invitation invitation) {
        invitation.setId(KeyUtil.getUniqueKey());
        return invitationDao.save(invitation);
    }
}
