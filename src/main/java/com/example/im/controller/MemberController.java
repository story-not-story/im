package com.example.im.controller;

import com.example.im.entity.Member;
import com.example.im.result.MemberResult;
import com.example.im.result.Result;
import com.example.im.service.MemberService;
import com.example.im.service.UserService;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author HuJun
 * @date 2020/3/26 8:45 下午
 */
@Api(tags = "群成员接口")
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "群成员列表", httpMethod = "GET")
    @ApiImplicitParam(name = "groupId", value = "群聊id", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true)
    @GetMapping
    public Result list(@RequestParam String groupId){
        List<Member> memberList = memberService.findByGroupId(groupId);
        List<MemberResult> userList = memberList.stream().map(m -> DO2VO.convert(m)).collect(Collectors.toList());
        return ResultUtil.success(userList);
    }

    @ApiOperation(value = "判断是否是群成员", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "groupId", value = "群聊id", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/isMember")
    public Result isMember(@RequestParam String userId, @RequestParam String groupId) {
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("isMember", memberService.isMember(groupId, userId));
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "删除群成员", httpMethod = "DELETE")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "groupId", value = "群聊id", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
            }
    )
    @DeleteMapping
    public Result remove(@RequestParam String groupId, @RequestParam String userId){
        memberService.remove(groupId, userId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "修改群昵称", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "groupId", value = "群聊id", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "name", value = "群昵称", defaultValue = "朵朵", dataTypeClass = String.class, required = true)
            }
    )
    @PutMapping("/name")
    public Result name(@RequestParam String groupId, @RequestParam String userId, @RequestParam String name){
        memberService.updateName(groupId, userId, name);
        return ResultUtil.success();
    }

    @ApiOperation(value = "修改群成员等级", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "groupId", value = "群聊id", defaultValue = "1586487898500340241", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "grade", value = "群成员等级", defaultValue = "1", dataTypeClass = Byte.class, required = true)
            }
    )
    @PutMapping("/grade")
    public Result grade(@RequestParam String groupId, @RequestParam String userId, @RequestParam Byte grade){
        memberService.updateGrade(groupId, userId, grade);
        return ResultUtil.success();
    }
}
