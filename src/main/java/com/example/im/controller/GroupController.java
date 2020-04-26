package com.example.im.controller;

import com.example.im.entity.Group;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MemberGrade;
import com.example.im.exception.GroupException;
import com.example.im.result.FriendResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.GroupService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.Code2Enum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/3/26 7:54 下午
 */
@RestController
@RequestMapping("/group")
@Slf4j
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FriendService friendService;
    @PostMapping
    public Result create(@RequestParam String userId, @RequestParam String name, @RequestParam String signature, @RequestParam(value = "userIdList[]") List<String> userIdList){
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
        userIdList.stream().filter(o -> friendService.isFriend(userId, o)).forEach(o -> {
            Member m = new Member();
            m.setId(KeyUtil.getUniqueKey());
            m.setGroupId(groupResult.getId());
            m.setUserId(o);
            m.setGrade(MemberGrade.NORMAL.getCode());
            Member mR = memberService.add(m);
        });
        Map<String, String> map = new HashMap<>(2);
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
        group.setNotice(notice);
        groupService.save(group);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    public Result list(@RequestParam String text){
        if (Pattern.matches("\\d+", text)) {
            try {
                Group group = groupService.findById(text);
                return ResultUtil.success(group);
            } catch (GroupException e) {}
        }
        List<Group> groupList = groupService.findByName(text);
        if (CollectionUtils.isEmpty(groupList)) {
            return ResultUtil.error(ErrorCode.GROUP_NOT_EXISTS);
        }
        return ResultUtil.success(groupList);
    }

    @GetMapping("/info")
    public Result findOne(@RequestParam String groupId){
        Group group = groupService.findById(groupId);
        return ResultUtil.success(group);
    }

    @GetMapping
    public Result findAll(@RequestParam String userId){
        Map<String, List<Group>> map = new HashMap<>(3);
        List<Group> create = new ArrayList<>();
        List<Group> manage = new ArrayList<>();
        List<Group> join = new ArrayList<>();
        List<Member> memberList = memberService.findByUserId(userId);
        List<Group> groupList = memberList.stream().map(m -> {
            try {
                return groupService.findById(m.getGroupId());
            } catch (GroupException e) {
                return null;
            }
        }).collect(Collectors.toList());
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

    @GetMapping("/search")
    public Result search(@RequestParam String userId, @RequestParam String text){
        Map<String, List<Group>> map = new HashMap<>(3);
        List<Group> create = new ArrayList<>();
        List<Group> manage = new ArrayList<>();
        List<Group> join = new ArrayList<>();
        List<Member> memberList = memberService.findByUserId(userId);
        List<Group> groupList = memberList.stream().map(m -> {
            try {
                return groupService.findById(m.getGroupId());
            } catch (GroupException e) {
                return null;
            }
        }).collect(Collectors.toList());
        if (Pattern.matches("\\d+", text)) {
            List<Group> resultList = groupList.stream().filter(o -> text.equals(o.getId())).collect(Collectors.toList());
            if (resultList.size() == 1) {
                return ResultUtil.success(resultList);
            }
        }
        List<Group> resultList = groupList.stream().filter(o -> o.getName().contains(text) || o.getId().contains(text)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultList)) {
            return ResultUtil.error(ErrorCode.GROUP_NOT_EXISTS);
        }
        return ResultUtil.success(resultList);
    }

}
