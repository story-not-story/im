package com.example.im.controller;


import com.example.im.entity.GroupApply;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.GroupException;
import com.example.im.result.GroupApplyResult;
import com.example.im.result.Result;
import com.example.im.service.GroupApplyService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

import static com.example.im.enums.MemberGrade.NORMAL;

/**
 * @author HuJun
 * @date 2020/4/11 7:58 下午
 */
@Api(tags = "加群申请接口")
@RestController
@Slf4j
@RequestMapping("/apply/group")
public class GroupApplyController {
    @Autowired
    private GroupApplyService applyService;
    @Autowired
    private MemberService memberService;
    @ApiOperation(value = "创建加群申请", httpMethod = "POST")
    @PostMapping
    public Result create(@Valid GroupApply apply, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建加群申请】参数错误");
            throw new GroupException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        if (apply.getId() != null || apply.getIsAccepted() != null) {
            log.error("【创建加群申请】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), "id和isAccepted必须为空");
        }
        if (memberService.isMember(apply.getGroupId(), apply.getUserId())) {
            log.error("【创建加群申请】已经是群成员");
            throw new GroupException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }
        GroupApply applyResult = applyService.create(apply);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", applyResult.getId());
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "加群申请列表", httpMethod = "GET", notes = "待我处理的加群申请和我创建的加群申请")
    @ApiImplicitParam(name = "userId", value = "用户ID",defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<GroupApply> applyList = applyService.findByUserId(userId);
        List<GroupApply> toDoList = new ArrayList<>();
        memberService.findByUserId(userId).stream()
                .filter(member -> !NORMAL.getCode().equals(member.getGrade()))
                .forEach(member -> toDoList.addAll(applyService.findByGroupId(member.getGroupId())));
        List<GroupApplyResult> applyResultList = applyList.stream().map(o -> DO2VO.convert(o)).collect(Collectors.toList());
        List<GroupApplyResult> toDoResultList = toDoList.stream().map(o -> DO2VO.convert(o)).collect(Collectors.toList());
        Map<String, List<GroupApplyResult>> map = new HashMap<>(2);
        map.put("applyList", applyResultList);
        map.put("toDoList", toDoResultList);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "加群申请详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "加群申请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/detail")
    public Result detail(@RequestParam String id) {
        GroupApply apply = applyService.findById(id);
        GroupApplyResult result = DO2VO.convert(apply);
        return ResultUtil.success(result);
    }

    @ApiOperation(value = "拒绝加群申请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "加群申请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/reject")
    public Result reject(@RequestParam String id){
        GroupApply apply = applyService.reject(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "同意加群申请", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "加群申请ID",defaultValue = "1588590953614706732", dataTypeClass = String.class, required = true)
    @GetMapping("/accept")
    public Result accept(@RequestParam String id){
        GroupApply apply = applyService.accept(id);
        Member member = new Member();
        member.setId(KeyUtil.getUniqueKey());
        member.setGrade(NORMAL.getCode().byteValue());
        member.setUserId(apply.getUserId());
        member.setGroupId(apply.getGroupId());
        Member memberResult = memberService.add(member);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", memberResult.getId());
        return ResultUtil.success(map);
    }
}
