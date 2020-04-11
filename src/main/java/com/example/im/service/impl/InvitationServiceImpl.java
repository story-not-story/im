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

import java.util.List;

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
    public Invitation findById(String id) {
        return invitationDao.findById(id).orElse(null);
    }

    @Override
    public List<Invitation> findByUserId(String userId) {
        return invitationDao.findByUserId(userId);
    }

    @Override
    public Invitation accept(String id) {
        Invitation invitation = findById(id);
        if (invitation == null){
            log.error("【接受好友添加申请】邀请不存在");
            throw new FriendException(ErrorCode.INVITATION_NOT_FOUND);
        }
        if (invitation.getIsAccepted() != null){
            log.error("【接受好友添加申请】该好友添加申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
        }
        invitation.setIsAccepted(true);
        return invitationDao.save(invitation);
    }

    @Override
    public Invitation reject(String id) {
        Invitation invitation = findById(id);
        if (invitation == null){
            log.error("【拒绝好友添加申请】邀请不存在");
            throw new FriendException(ErrorCode.INVITATION_NOT_FOUND);
        }
        if (invitation.getIsAccepted() != null){
            log.error("【拒绝好友添加申请】该好友添加申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
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
