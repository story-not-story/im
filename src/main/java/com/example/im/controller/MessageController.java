package com.example.im.controller;

import com.example.im.entity.Message;
import com.example.im.result.MessageResult;
import com.example.im.result.Result;
import com.example.im.service.MessageService;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/4/13 10:41 下午
 */
@Api(tags = "消息接口")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @ApiOperation(value = "消息列表", httpMethod = "GET", notes = "最新的20条消息（包括群消息和1v1消息）")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping("/list")
    public Result list(@RequestParam String userId) {
        List<Message> messageList = messageService.findTop(userId);
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, false);
        return ResultUtil.success(messageResultList);
    }

    @ApiOperation(value = "模糊匹配搜索内容的消息列表", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "text", value = "搜索内容", defaultValue = "jojo", dataTypeClass = String.class, required = true)
            }
    )
    @GetMapping("/search")
    public Result search(@RequestParam String userId, @RequestParam String text) {
        List<Message> messageList = messageService.findByContentLike(userId, text);
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, true);
        return ResultUtil.success(messageResultList);
    }

    @ApiOperation(value = "聊天界面消息", httpMethod = "GET")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "isGroup", value = "是否是群消息", defaultValue = "false", dataTypeClass = Boolean.class, required = true),
                    @ApiImplicitParam(name = "otherId", value = "对方ID（用户或者群）", defaultValue = "1586969360085913084", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "pageNo", value = "当前页号", defaultValue = "1", dataTypeClass = Integer.class),
                    @ApiImplicitParam(name = "pageSize", value = "一页消息条数", defaultValue = "20", dataTypeClass = Integer.class)
            }
    )
    @GetMapping("/detail")
    public Result listGroup(@RequestParam Boolean isGroup, @RequestParam String otherId, @RequestParam String userId, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "gmt_create"));
        List<Message> messageList = null;
        if (isGroup) {
            messageList = messageService.findByGroupId(userId, otherId, pageable);//staus删除不适用群聊
        } else {
            messageList = messageService.findByFriend(userId, otherId, pageable);
        }
        List<MessageResult> messageResultList = DO2VO.convert(messageList, userId, true);
        Collections.reverse(messageResultList);
        return ResultUtil.success(messageResultList);
    }

    @ApiOperation(value = "删除消息", httpMethod = "DELETE")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "id", value = "消息ID", defaultValue = "1588345300216875469", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
            }
    )
    @DeleteMapping
    public Result delete(@RequestParam String id, @RequestParam String userId){
        messageService.delete(id, userId);
        return ResultUtil.success();
    }
}
