package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResultUtil.success(friendList);
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
