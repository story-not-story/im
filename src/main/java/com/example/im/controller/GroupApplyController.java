package com.example.im.controller;


import com.example.im.entity.GroupApply;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.GroupApplyResult;
import com.example.im.result.Result;
import com.example.im.service.GroupApplyService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
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
        List<GroupApplyResult> applyResultList = applyList.stream().map(o -> DO2VO.convert(o)).collect(Collectors.toList());
        List<GroupApplyResult> toDoResultList = toDoList.stream().map(o -> DO2VO.convert(o)).collect(Collectors.toList());
        Map<String, List<GroupApplyResult>> map = new HashMap<>(2);
        map.put("applyList", applyResultList);
        map.put("toDoList", toDoResultList);
        return ResultUtil.success(map);
    }
    @GetMapping("/detail")
    public Result detail(@RequestParam String id) {
        GroupApply apply = applyService.findById(id);
        GroupApplyResult result = DO2VO.convert(apply);
        return ResultUtil.success(result);
    }

    @GetMapping("/reject")
    public Result reject(@RequestParam String id){
        GroupApply apply = applyService.reject(id);
        return ResultUtil.success(apply);
    }

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
