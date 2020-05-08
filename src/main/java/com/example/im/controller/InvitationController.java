package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Invitation;
import com.example.im.entity.Label;
import com.example.im.entity.User;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.LabelException;
import com.example.im.result.InvitationResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.InvitationService;
import com.example.im.service.LabelService;
import com.example.im.service.UserService;
import com.example.im.util.BeanUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.StringUtil;
import com.example.im.util.converter.DO2VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/3/23 4:08 下午
 */
@Api(tags = "加好友申请接口")
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
    @Autowired
    private UserService userService;

    @ApiOperation(value = "创建加好友申请", httpMethod = "POST")
    @ApiImplicitParam(name = "invitation", value = "加好友申请具体参数", dataTypeClass = Invitation.class, required = true)
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

    @ApiOperation(value = "加好友申请详情", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "id", value = "加好友申请详情id", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/detail")
    public Result detail(@RequestParam String id, @RequestParam String userId){
        Invitation invitation = invitationService.findById(id);
        InvitationResult invitationResult = DO2VO.convert(invitation, userId);
        return ResultUtil.success(invitationResult);
    }

    @ApiOperation(value = "加好友申请列表", httpMethod = "GET", notes = "我加别人、别人加我的好友申请")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping()
    public Result list(@RequestParam String userId){
        List<Invitation> invitationList = invitationService.findByUserId(userId);
        List<InvitationResult> invitationResultList = new ArrayList<>();
        List<InvitationResult> todoList = new ArrayList<>();
        invitationList.stream().forEach(invitation -> {
            InvitationResult invitationResult = new InvitationResult();
            BeanUtil.copyProperties(invitation, invitationResult);
            User user = null;
            if (userId.equals(invitation.getSenderId())) {
                user = userService.findById(invitation.getReceiverId());
                invitationResult.setAvatar(user.getAvatar());
                if (StringUtil.isNullOrEmpty(invitationResult.getRemark())) {
                    invitationResult.setRemark(user.getName());
                }
                invitationResultList.add(invitationResult);
            } else {
                user = userService.findById(invitation.getSenderId());
                invitationResult.setAvatar(user.getAvatar());
                if (StringUtil.isNullOrEmpty(invitationResult.getRemark())) {
                    invitationResult.setRemark(user.getName());
                }
                todoList.add(invitationResult);
            }
        });
        Map<String, List<InvitationResult>> map = new HashMap<>(2);
        map.put("invitationList", invitationResultList);
        map.put("todoList", todoList);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "拒绝加好友申请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "加好友申请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/reject")
    public Result reject(@RequestParam String id){
        Invitation invitation = invitationService.reject(id);
        return ResultUtil.success(invitation);
    }

    @ApiOperation(value = "同意加好友申请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "加好友申请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
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
