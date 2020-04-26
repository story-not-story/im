package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Invitation;
import com.example.im.entity.Label;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.LabelException;
import com.example.im.result.InvitationResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.InvitationService;
import com.example.im.service.LabelService;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuJun
 * @date 2020/3/23 4:08 下午
 */
@RestController
@Slf4j
@RequestMapping("/invite/friend")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private FriendService friendService;
    @PostMapping
    public Result create(@Valid Invitation invitation, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Invitation invitationResult = invitationService.create(invitation);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", invitationResult.getId());
        return ResultUtil.success(map);
    }
    @GetMapping("/detail")
    public Result detail(@RequestParam String id, @RequestParam String userId){
        Invitation invitation = invitationService.findById(id);
        InvitationResult invitationResult = DO2VO.convert(invitation, userId);
        return ResultUtil.success(invitationResult);
    }

    @GetMapping()
    public Result list(@RequestParam String userId){
        List<Invitation> invitationList = invitationService.findByUserId(userId);
        List<InvitationResult> invitationResultList = DO2VO.convert(invitationList, userId);
        return ResultUtil.success(invitationResultList);
    }

    @GetMapping("/reject")
    public Result reject(@RequestParam String id){
        Invitation invitation = invitationService.reject(id);
        return ResultUtil.success(invitation);
    }

    @GetMapping("/accept")
    public Result accept(@RequestParam String id){
        Invitation invitation = invitationService.accept(id);
        Friend friend = new Friend();
        friend.setUserId(invitation.getSenderId());
        friend.setFriendId(invitation.getReceiverId());
        friend.setURemark(invitation.getRemark());
        friend.setULabelId(invitation.getLabelId());
        if (invitation.getLabelId() != null){
            try {
                labelService.findById(invitation.getLabelId());
            } catch (LabelException e) {
                friend.setULabelId(null);
            }
        }
        Friend friendResult = friendService.add(friend);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", friendResult.getId());
        return ResultUtil.success(map);
    }
}
