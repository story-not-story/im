package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Invitation;
import com.example.im.entity.Label;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.FriendResult;
import com.example.im.result.LabelResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.InvitationService;
import com.example.im.service.LabelService;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author HuJun
 * @date 2020/3/23 2:53 下午
 */
@Controller
@ResponseBody
@RequestMapping("/friend")
@Slf4j
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private InvitationService invitationService;

    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        return ResultUtil.success(friendList);
    }
    @GetMapping
    public Result add(@RequestParam String invitationId){
        Invitation invitation = invitationService.findById(invitationId);
        if (invitation == null){
            log.error("【添加好友】好友添加申请不存在");
            throw new FriendException(ErrorCode.INVITATION_NOT_FOUND);
        }
        Friend friend = new Friend();
        friend.setUserId(invitation.getSenderId());
        friend.setFriendId(invitation.getReceiverId());
        friend.setURemark(invitation.getRemark());
        friend.setULabelId(invitation.getLabelId());
        if (invitation.getLabelId() != null){
            Label label = labelService.findById(invitation.getLabelId());
            if (label == null){
                friend.setULabelId(null);
            }
        }
        Friend friendResult = friendService.add(friend);
        Map<String, String> map = new HashMap<>();
        map.put("id", friendResult.getId());
        return ResultUtil.success(map);
    }

    @DeleteMapping
    public Result delete(@RequestParam String userId, @RequestParam String friendId){
        friendService.remove(userId, friendId);
        return ResultUtil.success();
    }

    @GetMapping("/blacklist")
    public Result blacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.blacklist(userId, friendId);
        return ResultUtil.success();
    }

    @GetMapping("/unblacklist")
    public Result unblacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.unblacklist(userId, friendId);
        return ResultUtil.success();
    }

    @PutMapping("/move")
    public Result move(@RequestParam String userId, @RequestParam String friendId, @RequestParam Integer labelId){
        friendService.move(userId, friendId, labelId);
        return ResultUtil.success();
    }

    @PutMapping("/remark")
    public Result remark(@RequestParam String userId, @RequestParam String friendId, @RequestParam String remark){
        friendService.remark(userId, friendId, remark);
        return ResultUtil.success();
    }
}
