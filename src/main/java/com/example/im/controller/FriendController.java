package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Group;
import com.example.im.enums.ErrorCode;
import com.example.im.result.FriendResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.util.CharUtil;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
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
@RestController
@RequestMapping("/friend")
@Slf4j
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        List<FriendResult> friendResultList = friendList.stream().map(o -> DO2VO.convert(o, userId)).collect(Collectors.toList());
        Map<Character, List<FriendResult>> letterMap = new TreeMap<>();
        char other = '#';
        for (FriendResult friendResult:
             friendResultList) {
            char letter = CharUtil.toUpper(friendResult.getRemark().charAt(0));
            if (CharUtil.isLetter(letter)) {
                if (!letterMap.containsKey(letter)) {
                    letterMap.put(letter, new ArrayList<FriendResult>());
                }
                letterMap.get(letter).add(friendResult);
            } else {
                if (!letterMap.containsKey(other)) {
                    letterMap.put(other, new ArrayList<>());
                }
                letterMap.get(other).add(friendResult);
            }
        }

        return ResultUtil.success(letterMap);
    }

    @GetMapping("/isFriend")
    public Result isFriend(@RequestParam String userId, @RequestParam String friendId) {
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("isFriend", friendService.isFriend(userId, friendId));
        return ResultUtil.success(map);
    }

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

    @DeleteMapping
    public Result delete(@RequestParam String userId, @RequestParam String friendId){
        friendService.remove(userId, friendId);
        return ResultUtil.success();
    }

    @GetMapping("/blacklist")
    public Result blacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.blacklist(userId, friendId);
        return ResultUtil.success();
    }

    @GetMapping("/unblacklist")
    public Result unblacklist(@RequestParam String userId, @RequestParam String friendId){
        Friend friend = friendService.unblacklist(userId, friendId);
        return ResultUtil.success();
    }

    @PutMapping("/move")
    public Result move(@RequestParam String userId, @RequestParam String friendId, @RequestParam Integer labelId){
        friendService.move(userId, friendId, labelId);
        return ResultUtil.success();
    }

    @PutMapping("/remark")
    public Result remark(@RequestParam String userId, @RequestParam String friendId, @RequestParam String remark){
        friendService.remark(userId, friendId, remark);
        return ResultUtil.success();
    }
}
