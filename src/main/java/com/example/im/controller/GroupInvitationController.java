package com.example.im.controller;


import com.example.im.entity.GroupInvitation;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.Result;
import com.example.im.service.GroupInvitationService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.im.enums.MemberGrade.NORMAL;

/**
 * @author HuJun
 * @date 2020/3/23 4:08 下午
 */
@RestController
@Slf4j
@RequestMapping("/invite/group")
public class GroupInvitationController {

    @Autowired
    private GroupInvitationService invitationService;
    @Autowired
    private MemberService memberService;
    @PostMapping
    public Result create(@Valid GroupInvitation invitation, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        GroupInvitation invitationResult = invitationService.create(invitation);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", invitationResult.getId());
        return ResultUtil.success(map);
    }
    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<GroupInvitation> invitationList = invitationService.findByReceiverId(userId);
        return ResultUtil.success(invitationList);
    }

    @GetMapping("/reject")
    public Result reject(@RequestParam String invitationId){
        GroupInvitation invitation = invitationService.reject(invitationId);
        return ResultUtil.success(invitation);
    }

    @GetMapping("/accept")
    public Result accept(@RequestParam String invitationId){
        GroupInvitation invitation = invitationService.accept(invitationId);
        Member member = new Member();
        member.setId(KeyUtil.getUniqueKey());
        member.setGrade(NORMAL.getCode().byteValue());
        member.setUserId(invitation.getReceiverId());
        member.setGroupId(invitation.getGroupId());
        Member memberResult = memberService.add(member);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", memberResult.getId());
        return ResultUtil.success(map);
    }
}
