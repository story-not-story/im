package com.example.im.controller;

import com.example.im.entity.Group;
import com.example.im.entity.Member;
import com.example.im.enums.ErrorCode;
import com.example.im.enums.MemberGrade;
import com.example.im.exception.GroupException;
import com.example.im.form.GroupForm;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.GroupService;
import com.example.im.service.MemberService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.Code2Enum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/3/26 7:54 下午
 */
@Api(tags = "群聊接口")
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
    @ApiOperation(value = "创建群聊", httpMethod = "POST", notes = "命名群聊和未命名群聊")
    @PostMapping
    public Result create(@Valid GroupForm groupForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建群聊】参数错误");
            throw new GroupException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Group group = new Group();
        group.setId(KeyUtil.getUniqueKey());
        group.setName(groupForm.getName());
        group.setSignature(groupForm.getSignature());
        Group groupResult = groupService.save(group);
        Member member = new Member();
        member.setId(KeyUtil.getUniqueKey());
        member.setGroupId(groupResult.getId());
        member.setUserId(groupForm.getUserId());
        member.setGrade(MemberGrade.OWNER.getCode());
        Member memberResult = memberService.add(member);
        if (!CollectionUtils.isEmpty(groupForm.getUserIdList())) {
            groupForm.getUserIdList().stream().filter(o -> friendService.isFriend(groupForm.getUserId(), o)).forEach(o -> {
                Member m = new Member();
                m.setId(KeyUtil.getUniqueKey());
                m.setGroupId(groupResult.getId());
                m.setUserId(o);
                m.setGrade(MemberGrade.NORMAL.getCode());
                Member mR = memberService.add(m);
            });
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("groupId", groupResult.getId());
        map.put("ownerId", memberResult.getUserId());
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "解散群聊", httpMethod = "DELETE")
    @ApiImplicitParam(name = "groupId", value = "群聊ID", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true)
    @DeleteMapping
    public Result delete(@RequestParam String groupId){
        groupService.deleteById(groupId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "发布群公告", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "groupId", value = "群聊ID", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "notice", value = "群公告内容", defaultValue = "周五交毕设论文", dataTypeClass = String.class, required = true)
            }
    )
    @PutMapping
    public Result notice(@RequestParam String groupId, @RequestParam String notice){
        Group group = groupService.findById(groupId);
        group.setNotice(notice);
        groupService.save(group);
        return ResultUtil.success();
    }

    @ApiOperation(value = "找群", httpMethod = "GET")
    @ApiImplicitParam(name = "text", value = "搜索内容", defaultValue = "南邮", dataTypeClass = String.class, required = true)
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

    @ApiOperation(value = "群聊资料", httpMethod = "GET")
    @ApiImplicitParam(name = "groupId", value = "群聊ID", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true)
    @GetMapping("/info")
    public Result findOne(@RequestParam String groupId){
        Group group = groupService.findById(groupId);
        return ResultUtil.success(group);
    }

    @ApiOperation(value = "查找群聊列表", httpMethod = "GET", notes = "按照我创建的群聊、我管理的群聊、我加入的群聊分类")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping
    public Result findAll(@RequestParam String userId){
        Map<String, List<Group>> map = new HashMap<>(3);
        List<Group> create = new ArrayList<>();
        List<Group> manage = new ArrayList<>();
        List<Group> join = new ArrayList<>();
        List<Member> memberList = memberService.findByUserId(userId);
        memberList.stream().forEach(o -> {
            try {
                Group group = groupService.findById(o.getGroupId());
                MemberGrade grade = Code2Enum.convert(o.getGrade(), MemberGrade.class);
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
            } catch (GroupException e) {
                log.error("【查找群聊列表】群不存在");
            }
        });
        map.put("我创建的群聊", create);
        map.put("我管理的群聊", manage);
        map.put("我加入的群聊", join);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "模糊匹配搜索内容的群聊列表", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "text", value = "搜索内容", defaultValue = "jojo", dataTypeClass = String.class, required = true)
            }
    )
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
        }).filter(Objects::nonNull).collect(Collectors.toList());
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
