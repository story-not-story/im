package com.example.im.service.impl;

import com.example.im.dao.GroupInvitationDao;
import com.example.im.entity.GroupInvitation;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.GroupException;
import com.example.im.service.FriendService;
import com.example.im.service.GroupInvitationService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
@Service
@Slf4j
public class GroupInvitationServiceImpl implements GroupInvitationService {
    @Autowired
    private GroupInvitationDao invitationDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FriendService friendService;
    @Override
    public GroupInvitation findById(String id) {
        Optional<GroupInvitation> groupInvitation = invitationDao.findById(id);
        if (groupInvitation.isPresent()) {
            return groupInvitation.get();
        } else {
            log.error("【根据id查找群邀请】群邀请不存在");
            throw new GroupException(ErrorCode.INVITATION_NOT_FOUND);
        }
    }

    @Override
    public List<GroupInvitation> findByReceiverId(String receiverId) {
        return invitationDao.findByReceiverId(receiverId);
    }

    @Override
    public GroupInvitation accept(String id) {
        GroupInvitation invitation = findById(id);
        if (invitation.getIsAccepted() != null){
            log.error("【接受好友添加申请】该好友添加申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
        }
        invitation.setIsAccepted(true);
        return invitationDao.save(invitation);
    }

    @Override
    public GroupInvitation reject(String id) {
        GroupInvitation invitation = findById(id);
        if (invitation.getIsAccepted() != null){
            log.error("【拒绝好友添加申请】该好友添加申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
        }
        invitation.setIsAccepted(false);
        return invitationDao.save(invitation);
    }

    @Override
    public GroupInvitation create(GroupInvitation invitation) {
        if (!memberService.isMember(invitation.getGroupId(), invitation.getSenderId())) {
            log.error("【邀请好友加入群聊】你不是该群成员");
            throw new GroupException(ErrorCode.MEMBER_NOT_EXISTS);
        } else if (!friendService.isFriend(invitation.getSenderId(), invitation.getReceiverId())) {
            log.error("【邀请好友加入群聊】该好友不存在");
            throw new FriendException(ErrorCode.FRIEND_NOT_EXISTS);
        } else {
            invitation.setId(KeyUtil.getUniqueKey());
            return invitationDao.save(invitation);
        }
    }
}
