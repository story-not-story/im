package com.example.im.controller;

import com.example.im.config.UrlConfig;
import com.example.im.entity.Invitation;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.result.Result;
import com.example.im.service.InvitationService;
import com.example.im.util.KeyUtil;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuJun
 * @date 2020/3/23 4:08 下午
 */
@Controller
@Slf4j
@RequestMapping("/invite")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UrlConfig urlConfig;
    @PostMapping("/create")
    @ResponseBody
    public Result create(@Valid Invitation invitation, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【用户注册】参数错误");
            throw new FriendException(ErrorCode.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Invitation invitationResult = invitationService.create(invitation);
        Map<String, String> map = new HashMap<>();
        map.put("id", invitationResult.getId());
        return ResultUtil.success(map);
    }
    @GetMapping("/list")
    @ResponseBody
    public Result list(@RequestParam String userId){
        List<Invitation> invitationList = invitationService.findByReceiverId(userId);
        return ResultUtil.success(invitationList);
    }

    @GetMapping("/reject")
    @ResponseBody
    public Result reject(@RequestParam String invitationId){
        Invitation invitation = invitationService.reject(invitationId);
        return ResultUtil.success(invitation);
    }

    @GetMapping("/accept")
    public String accept(@RequestParam String invitationId){
        Invitation invitation = invitationService.accept(invitationId);
        return "redirect:" + urlConfig.getHost() + "/friend/add?invitationId=" + invitationId;
    }
}
