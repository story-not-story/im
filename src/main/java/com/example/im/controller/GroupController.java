package com.example.im.controller;

import com.example.im.entity.Group;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MemberGrade;
import com.example.im.exception.GroupException;
import com.example.im.result.Result;
import com.example.im.service.GroupService;
import com.example.im.service.MemberService;
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

/**
 * @author HuJun
 * @date 2020/3/26 7:54 下午
 */
@Controller
@ResponseBody
@RequestMapping("/group")
@Slf4j
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private MemberService memberService;
    @PostMapping()
    public Result create(@RequestParam String userId, @RequestParam String name, @RequestParam String signature){
        Group group = new Group();
        group.setId(KeyUtil.getUniqueKey());
        group.setName(name);
        group.setSignature(signature);
        Group groupResult = groupService.save(group);
        Member member = new Member();
        member.setId(KeyUtil.getUniqueKey());
        member.setGroupId(groupResult.getId());
        member.setUserId(userId);
        member.setGrade(MemberGrade.OWNER.getCode());
         Member memberResult = memberService.add(member);
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupResult.getId());
        map.put("ownerId", memberResult.getUserId());
        return ResultUtil.success(map);
    }

    @DeleteMapping
    public Result delete(@RequestParam String groupId){
        groupService.deleteById(groupId);
        return ResultUtil.success();
    }

    @PutMapping
    public Result notice(@RequestParam String groupId, @RequestParam String notice){
        Group group = groupService.findById(groupId);
        if (group == null){
            log.error("【发布群公告】该群不存在");
            throw new GroupException(ErrorCode.GROUP_NOT_EXISTS);
        }
        group.setNotice(notice);
        groupService.save(group);
        return ResultUtil.success();
    }

    @GetMapping
    public Result findAll(@RequestParam String userId){
        Map<String, List<Group>> map = new HashMap<>(3);
        List<Group> create = new ArrayList<>();
        List<Group> manage = new ArrayList<>();
        List<Group> join = new ArrayList<>();
        List<Member> memberList = memberService.findByUserId(userId);
        List<Group> groupList = memberList.stream().map(m -> groupService.findById(m.getGroupId())).collect(Collectors.toList());
        for (int i = 0; i < memberList.size(); i++) {
            MemberGrade grade = Code2Enum.convert(memberList.get(i).getGrade(), MemberGrade.class);
            Group group = groupList.get(i);
            switch (grade){
                case NORMAL:
                    join.add(group);
                    break;
                case MANAGER:
                    manage.add(group);
                    break;
                case OWNER:
                    create.add(group);
                    break;
                default:
            }
        }
        map.put("我创建的群聊", create);
        map.put("我管理的群聊", manage);
        map.put("我加入的群聊", join);
        return ResultUtil.success(map);
    }

}
