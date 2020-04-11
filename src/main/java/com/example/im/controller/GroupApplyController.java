package com.example.im.controller;


import com.example.im.entity.GroupApply;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.Result;
import com.example.im.service.GroupApplyService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.im.enums.MemberGrade.NORMAL;

/**
 * @author HuJun
 * @date 2020/4/11 7:58 下午
 */
@RestController
@Slf4j
@RequestMapping("/apply/group")
public class GroupApplyController {
    @Autowired
    private GroupApplyService applyService;
    @Autowired
    private MemberService memberService;
    @PostMapping
    public Result create(@Valid GroupApply apply, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        GroupApply applyResult = applyService.create(apply);
        Map<String, String> map = new HashMap<>(1);
        map.put("id", applyResult.getId());
        return ResultUtil.success(map);
    }
    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<GroupApply> applyList = applyService.findByUserId(userId);
        List<GroupApply> toDoList = new ArrayList<>();
        memberService.findByUserId(userId).stream()
                .filter(member -> !NORMAL.getCode().equals(member.getGrade()))
                .forEach(member -> toDoList.addAll(applyService.findByGroupId(member.getGroupId())));
        Map<String, List<GroupApply>> map = new HashMap<>(2);
        map.put("applyList", applyList);
        map.put("toDoList", toDoList);
        return ResultUtil.success(map);
    }

    @GetMapping("/reject")
    public Result reject(@RequestParam String applyId){
        GroupApply apply = applyService.reject(applyId);
        return ResultUtil.success(apply);
    }

    @GetMapping("/accept")
    public Result accept(@RequestParam String applyId){
        GroupApply apply = applyService.accept(applyId);
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
