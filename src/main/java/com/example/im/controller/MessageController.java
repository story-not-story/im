package com.example.im.controller;

import com.example.im.entity.Message;
import com.example.im.result.MessageResult;
import com.example.im.result.Result;
import com.example.im.service.MessageService;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/4/13 10:41 下午
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @GetMapping("/list")
    public Result list(@RequestParam String userId) {
        List<Message> messageList = messageService.findTop(userId);
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, false);
        return ResultUtil.success(messageResultList);
    }

    @GetMapping("/search")
    public Result search(@RequestParam String userId, @RequestParam String text) {
        List<Message> messageList = messageService.findByContentLike(userId, text);
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, true);
        return ResultUtil.success(messageResultList);
    }

    @GetMapping("/detail")
    public Result listGroup(@RequestParam boolean isGroup, @RequestParam String otherId, @RequestParam String userId, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "gmt_create"));
        List<Message> messageList = null;
        if (isGroup) {
            messageList = messageService.findByGroupId(otherId, pageable);//staus删除不适用群聊
        } else {
            messageList = messageService.findByFriend(userId, otherId, pageable);
        }
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, true);
        Collections.reverse(messageResultList);
        return ResultUtil.success(messageResultList);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam String id, @RequestParam String userId){
        messageService.delete(id, userId);
        return ResultUtil.success();
    }
}
