package com.example.im.util.converter;

import com.example.im.entity.*;
import com.example.im.result.*;
import com.example.im.service.FriendService;
import com.example.im.service.GroupService;
import com.example.im.service.MemberService;
import com.example.im.service.UserService;
import com.example.im.util.BeanUtil;
import com.example.im.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/4/14 9:53 下午
 */
@Slf4j
@Component
public class DO2VO {
    private static MemberService memberService;
    @Autowired
    public void setMemberService(MemberService memberService){
        DO2VO.memberService = memberService;
    }

    private static FriendService friendService;
    @Autowired
    public void setFriendService(FriendService friendService){
        DO2VO.friendService = friendService;
    }

    private static UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        DO2VO.userService = userService;
    }

    private static GroupService groupService;
    @Autowired
    public void setGroupService(GroupService groupService){
        DO2VO.groupService = groupService;
    }

    public static FriendResult convert(Friend friend, String userId) {
        FriendResult friendResult = new FriendResult();
        if (userId.equals(friend.getUserId())){
            friendResult.setFriendId(friend.getFriendId());
            friendResult.setIsBlacklisted(friend.getIsUserBlacklisted());
            friendResult.setLabelId(friend.getULabelId());
            friendResult.setRemark(friend.getURemark());
        } else {
            friendResult.setFriendId(friend.getUserId());
            friendResult.setIsBlacklisted(friend.getIsFriendBlacklisted());
            friendResult.setLabelId(friend.getFLabelId());
            friendResult.setRemark(friend.getFRemark());
        }
        User user =userService.findById(friendResult.getFriendId());
        friendResult.setName(user.getName());
        friendResult.setAvatar(user.getAvatar());
        friendResult.setSignature(user.getSignature());
        if (StringUtil.isNullOrEmpty(friendResult.getRemark())) {
            friendResult.setRemark(user.getName());
        }
        return friendResult;
    }

    public static GroupApplyResult convert(GroupApply groupApply) {
        GroupApplyResult groupApplyResult = new GroupApplyResult();
        BeanUtil.copyProperties(groupApply, groupApplyResult);
        Group group = groupService.findById(groupApply.getGroupId());
        String avatar = group.getAvatar();
        groupApplyResult.setAvatar(avatar);
        return groupApplyResult;
    }

    public static List<InvitationResult> convert(List<Invitation> invitationList, String userId) {
        return invitationList.stream().map(invitation -> {
            InvitationResult invitationResult = new InvitationResult();
            BeanUtil.copyProperties(invitation, invitationResult);
            User user = null;
            if (userId.equals(invitation.getSenderId())) {
                user = userService.findById(invitation.getReceiverId());
            } else {
                user = userService.findById(invitation.getSenderId());
            }
            invitationResult.setAvatar(user.getAvatar());
            if (StringUtil.isNullOrEmpty(invitationResult.getRemark())) {
                invitationResult.setRemark(user.getName());
            }
            return invitationResult;
        }).collect(Collectors.toList());
    }

    public static InvitationResult convert(Invitation invitation, String userId) {
        InvitationResult invitationResult = new InvitationResult();
        BeanUtil.copyProperties(invitation, invitationResult);
        User user = null;
        if (userId.equals(invitation.getSenderId())) {
            user = userService.findById(invitation.getReceiverId());
        } else {
            user = userService.findById(invitation.getSenderId());
        }
        invitationResult.setAvatar(user.getAvatar());
        if (StringUtil.isNullOrEmpty(invitationResult.getRemark())) {
            invitationResult.setRemark(user.getName());
        }
        return invitationResult;
    }

    public static List<MessageResult> convert(List<Message> messageList, String userId, boolean isDetail) {
        return messageList.stream().map(message -> {
            MessageResult messageResult = new MessageResult();
            BeanUtil.copyProperties(message, messageResult);
            String senderId = message.getSenderId();
            String receiverId = message.getReceiverId();
            if (message.getIsGroup()) {
                Group group = null;
                if (isDetail) {
                    Member member = memberService.findOne(receiverId, userId);
                    User user = userService.findById(senderId);
                    String name = null;
                    if (member.getName() != null) {
                        name = member.getName();
                    } else {
                        name = user.getName();
                    }
                    messageResult.setAvatar(user.getAvatar());
                    messageResult.setName(name);
                } else {
                    group = groupService.findById(receiverId);
                    messageResult.setAvatar(group.getAvatar());
                    messageResult.setName(group.getName());
                }
            } else {
                User user = null;
                String name = null;
                if (isDetail) {
                    user = userService.findById(senderId);
                    name = user.getName();
                } else {
                    if (userId.equals(senderId)) {
                        user = userService.findById(receiverId);
                    } else {
                        user = userService.findById(senderId);
                    }
                    Friend friend = friendService.findOne(senderId, receiverId);
                    if (friend == null || (name = convert(friend, userId).getRemark()) == null) {
                        name = user.getName();
                    }
                }
                messageResult.setAvatar(user.getAvatar());
                messageResult.setName(name);
            }
            return messageResult;
        }).collect(Collectors.toList());
    }

    public static MessageResult convert(Message message, String userId, boolean isDetail) {
        MessageResult messageResult = new MessageResult();
        BeanUtil.copyProperties(message, messageResult);
        String senderId = message.getSenderId();
        String receiverId = message.getReceiverId();
        if (message.getIsGroup()) {
            Group group = null;
            if (isDetail) {
                Member member = memberService.findOne(receiverId, senderId);
                User user = userService.findById(senderId);
                String name = null;
                if (member.getName() != null) {
                    name = member.getName();
                } else {
                    name = user.getName();
                }
                messageResult.setAvatar(user.getAvatar());
                messageResult.setName(name);
            } else {
                group = groupService.findById(receiverId);
                messageResult.setAvatar(group.getAvatar());
                messageResult.setName(group.getName());
            }
        } else {
            User user = null;
            String name = null;
            if (isDetail) {
                user = userService.findById(senderId);
                name = user.getName();
            } else {
                if (userId.equals(senderId)) {
                    user = userService.findById(receiverId);
                } else {
                    user = userService.findById(senderId);
                }
                Friend friend = friendService.findOne(senderId, receiverId);
                if (friend == null || (name = convert(friend, userId).getRemark()) == null) {
                    name = user.getName();
                }
            }
            messageResult.setAvatar(user.getAvatar());
            messageResult.setName(name);
        }
        return messageResult;
    }

    public static MemberResult convert(Member member) {
        MemberResult memberResult = new MemberResult();
        BeanUtil.copyProperties(member, memberResult);
        User user = userService.findById(member.getUserId());
        memberResult.setAvatar(user.getAvatar());
        if (StringUtil.isNullOrEmpty(memberResult.getName())) {
            memberResult.setName(user.getName());
        }
        return memberResult;
    }
}
