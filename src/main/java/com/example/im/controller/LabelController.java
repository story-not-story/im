package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Label;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.LabelException;
import com.example.im.result.FriendResult;
import com.example.im.result.LabelResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.LabelService;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author HuJun
 * @date 2020/3/26 8:12 下午
 */
@Controller
@ResponseBody
@RequestMapping("/label")
@Slf4j
public class LabelController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private FriendService friendService;
    @PostMapping
    public Result create(@RequestParam String userId, @RequestParam String name){
        List<Label> labelList = labelService.findByUserId(userId);
        for (Label label:
             labelList) {
            if (name.equals(label.getName())){
                log.error("【添加分组】分组名不能重复");
                throw new LabelException(ErrorCode.LABEL_MUST_DIFF);
            }
        }
        Label label = new Label();
        label.setName(name);
        label.setUserId(userId);
        Label labelResult  = labelService.save(label);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", labelResult.getId());
        return ResultUtil.success(map);
    }

    @PutMapping
    public Result update(@RequestParam Integer id, @RequestParam String name){
        Label label = labelService.findById(id);
        if (label == null){
            log.error("【修改分组】分组不存在");
            throw new LabelException(ErrorCode.LABEL_NOT_EXISTS);
        }
        List<Label> labelList = labelService.findByUserId(label.getUserId());
        for (Label item:
                labelList) {
            if (label.getId().equals(item.getId()) && name.equals(item.getName())){
                log.error("【修改分组】分组名不能重复");
                throw new LabelException(ErrorCode.LABEL_MUST_DIFF);
            }
        }
        label.setName(name);
        labelService.save(label);
        return ResultUtil.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        labelService.deleteById(id);
        return ResultUtil.success();
    }

    @GetMapping
    public Result findAll(@RequestParam String userId){
        List<Label> labelList = labelService.findByUserId(userId);
        return ResultUtil.success(labelList);
    }

    @GetMapping("/list")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        List<Label> labelList = labelService.findByUserId(userId);
        List<LabelResult> labelResultList = new ArrayList<>(labelList.size());
        labelList.forEach(new Consumer<Label>() {
            @Override
            public void accept(Label label) {
                LabelResult labelResult = new LabelResult();
                BeanUtils.copyProperties(label, labelResult);
                List<FriendResult> friendList1 = new ArrayList<FriendResult>();
                friendList.forEach(new Consumer<Friend>() {
                    @Override
                    public void accept(Friend friend) {
                        if (label.getId().equals(friend.getULabelId())){
                            FriendResult friendResult = new FriendResult();
                            friendResult.setFriendId(friend.getFriendId());
                            friendResult.setBlacklisted(friend.getIsUBlacklisted());
                            friendResult.setLabelId(friend.getULabelId());
                            friendResult.setRemark(friend.getURemark());
                            friendList1.add(friendResult);
                        } else if (label.getId().equals(friend.getFLabelId())){
                            FriendResult friendResult = new FriendResult();
                            friendResult.setFriendId(friend.getUserId());
                            friendResult.setBlacklisted(friend.getIsFBlacklisted());
                            friendResult.setLabelId(friend.getFLabelId());
                            friendResult.setRemark(friend.getFRemark());
                            friendList1.add(friendResult);
                        }
                    }
                });
                labelResult.setFriendList(friendList1);
                labelResultList.add(labelResult);
            }
        });
        return ResultUtil.success(labelResultList);
    }
}
