package com.example.im.controller;

import com.example.im.entity.Group;
import com.example.im.entity.Member;
import com.example.im.entity.User;
import com.example.im.enums.MemberGrade;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.GroupService;
import com.example.im.service.MemberService;
import com.example.im.service.UserService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.Code2Enum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.im.enums.MemberGrade.*;

/**
 * @author HuJun
 * @date 2020/3/26 8:45 下午
 */
@Controller
@ResponseBody
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
        List<User> userList = memberList.stream().map(m -> userService.findById(m.getUserId())).collect(Collectors.toList());
        return ResultUtil.success(userList);
    }
    @PostMapping
    public Result add(@RequestParam String groupId, @RequestParam String userId){
        Member member = new Member();
        member.setId(KeyUtil.getUniqueKey());
        member.setGrade(NORMAL.getCode().byteValue());
        member.setUserId(userId);
        member.setGroupId(groupId);
        Member memberResult = memberService.add(member);
        Map<String, String> map = new HashMap<>();
        map.put("id", memberResult.getId());
        return ResultUtil.success(map);
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
    public Result grade(@RequestParam String groupId, @RequestParam String userId, @RequestParam byte grade){
        memberService.updateGrade(groupId, userId, grade);
        return ResultUtil.success();
    }
}
