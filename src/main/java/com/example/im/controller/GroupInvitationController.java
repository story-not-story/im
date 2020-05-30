package com.example.im.controller;


import com.example.im.entity.GroupInvitation;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.GroupException;
import com.example.im.result.Result;
import com.example.im.service.GroupInvitationService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "邀请好友进群接口")
@RestController
@Slf4j
@RequestMapping("/invite/group")
public class GroupInvitationController {

    @Autowired
    private GroupInvitationService invitationService;
    @Autowired
    private MemberService memberService;
    @ApiOperation(value = "创建拉好友进群的邀请", httpMethod = "POST")
    @PostMapping
    public Result create(@Valid GroupInvitation invitation, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【邀请好友进群】参数错误");
            throw new GroupException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        if (invitation.getId() != null || invitation.getIsAccepted() != null) {
            log.error("【邀请好友进群】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), "id和isAccepted必须为空");
        }
        if (memberService.isMember(invitation.getGroupId(), invitation.getReceiverId())) {
            log.error("【邀请好友进群】已经是群成员");
            throw new GroupException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
        GroupInvitation invitationResult = invitationService.create(invitation);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", invitationResult.getId());
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "群邀请列表", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<GroupInvitation> invitationList = invitationService.findByReceiverId(userId);
        return ResultUtil.success(invitationList);
    }

    @ApiOperation(value = "拒绝群邀请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "群邀请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/reject")
    public Result reject(@RequestParam String id){
        GroupInvitation invitation = invitationService.reject(id);
        return ResultUtil.success(invitation);
    }

    @ApiOperation(value = "同意群邀请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "群邀请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/accept")
    public Result accept(@RequestParam String id){
        GroupInvitation invitation = invitationService.accept(id);
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
