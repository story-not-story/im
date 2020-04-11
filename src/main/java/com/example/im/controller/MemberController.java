package com.example.im.controller;

import com.example.im.entity.Member;
import com.example.im.entity.User;
import com.example.im.result.Result;
import com.example.im.service.*;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author HuJun
 * @date 2020/3/26 8:45 下午
 */
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;

    @GetMapping
    public Result list(@RequestParam String groupId){
        List<Member> memberList = memberService.findByGroupId(groupId);
        List<User> userList = memberList.stream().map(m -> {
            User user = userService.findById(m.getUserId());
            user.setPassword(null);
            return user;
        }).collect(Collectors.toList());
        return ResultUtil.success(userList);
    }

    @DeleteMapping
    public Result remove(@RequestParam String groupId, @RequestParam String userId){
        memberService.remove(groupId, userId);
        return ResultUtil.success();
    }

    @PutMapping("/name")
    public Result name(@RequestParam String groupId, @RequestParam String userId, @RequestParam String name){
        memberService.updateName(groupId, userId, name);
        return ResultUtil.success();
    }

    @PutMapping("/grade")
    public Result grade(@RequestParam String groupId, @RequestParam String userId, @RequestParam Byte grade){
        memberService.updateGrade(groupId, userId, grade);
        return ResultUtil.success();
    }
}
