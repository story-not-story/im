package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.FriendResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.util.PinYinUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/3/23 2:53 下午
 */
@Api(tags = "好友联系接口")
@RestController
@RequestMapping("/friend")
@Slf4j
public class FriendController {
    @Autowired
    private FriendService friendService;

    @ApiOperation(value = "查找好友列表", notes = "按字母排序", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        List<FriendResult> friendResultList = friendList.stream().map(o -> DO2VO.convert(o, userId)).collect(Collectors.toList());
        Map<Character, List<FriendResult>> letterMap = new TreeMap<>();
        for (FriendResult friendResult:
             friendResultList) {
            Character letter = PinYinUtil.getFirstChar(friendResult.getRemark());
            if (!letterMap.containsKey(letter)) {
                letterMap.put(letter, new ArrayList<FriendResult>());
            }
            letterMap.get(letter).add(friendResult);
        }
        return ResultUtil.success(letterMap);
    }

    @ApiOperation(value = "判断是否是好友", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/isFriend")
    public Result isFriend(@RequestParam String userId, @RequestParam String friendId) {
        Map<String, Object> map = new HashMap<>(1);
        try {
            Friend friend = friendService.findOne(userId, friendId);
            map.put("isFriend", true);
            map.put("name", DO2VO.convert(friend, userId).getRemark());
        } catch (FriendException e) {
            map.put("isFriend", false);
        }
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "模糊匹配搜索内容的好友列表", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "text", value = "搜索内容", defaultValue = "jojo", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/search")
    public Result search(@RequestParam String userId, @RequestParam String text) {
        List<Friend> friendList = friendService.findAll(userId);
        List<FriendResult> friendResultList = friendList.stream().map(o -> DO2VO.convert(o, userId)).collect(Collectors.toList());
        if (Pattern.matches("\\d+", text)) {
            List<FriendResult> resultList = friendResultList.stream().filter(o -> text.equals(o.getFriendId())).collect(Collectors.toList());
            if (resultList.size() == 1) {
                return ResultUtil.success(resultList);
            }
        }
        List<FriendResult> resultList = friendResultList.stream().filter(o -> o.getRemark().contains(text) || o.getName().contains(text) || o.getFriendId().contains(text)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultList)) {
            return ResultUtil.error(ErrorCode.FRIEND_NOT_EXISTS);
        }
        return ResultUtil.success(resultList);
    }

    @ApiOperation(value = "删除好友", httpMethod = "DELETE")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true)
            }
    )
    @DeleteMapping
    public Result delete(@RequestParam String userId, @RequestParam String friendId){
        friendService.remove(userId, friendId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "拉黑好友", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/blacklist")
    public Result blacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.blacklist(userId, friendId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "解除拉黑好友", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/unblacklist")
    public Result unblacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.unblacklist(userId, friendId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "移动好友分组", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "labelId", value = "新分组号", defaultValue = "1", dataTypeClass = Integer.class, required = true, allowEmptyValue = true)
            }
    )
    @PutMapping("/move")
    public Result move(@RequestParam String userId, @RequestParam String friendId, @RequestParam Integer labelId){
        friendService.move(userId, friendId, labelId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "修改好友备注", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "friendId", value = "好友ID", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "remark", value = "备注", defaultValue = "玲珑", dataTypeClass = String.class, required = true)
            }
    )
    @PutMapping("/remark")
    public Result remark(@RequestParam String userId, @RequestParam String friendId, @RequestParam String remark){
        friendService.remark(userId, friendId, remark);
        return ResultUtil.success();
    }
}
