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
import com.example.im.service.UserService;
import com.example.im.util.ResultUtil;
import com.example.im.util.converter.DO2VO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author HuJun
 * @date 2020/3/26 8:12 下午
 */
@RestController
@RequestMapping("/label")
@Slf4j
public class LabelController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
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
        Map<String, Integer> map = new HashMap<>(1);
        map.put("id", labelResult.getId());
        return ResultUtil.success(map);
    }

    @PutMapping
    public Result update(@RequestParam Integer id, @RequestParam String name){
        Label label = labelService.findById(id);
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

    @GetMapping("/friendlist")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        List<Label> labelList = labelService.findByUserId(userId);
        List<FriendResult> defaultLabelList = new ArrayList<FriendResult>();
        List<LabelResult> labelResultList = new ArrayList<>(labelList.size());
        List<FriendResult> friendResultList = friendList.stream().map(o -> DO2VO.convert(o, userId)).collect(Collectors.toList());
        LabelResult defaultLabel = new LabelResult();
        defaultLabel.setId(0);
        defaultLabel.setName("默认分组");
        defaultLabel.setUserId(userId);
        if (labelList == null || labelList.isEmpty()) {
            defaultLabel.setFriendList(friendResultList);
            labelResultList.add(defaultLabel);
        } else {
            labelList.forEach(new Consumer<Label>() {
                @Override
                public void accept(Label label) {
                    LabelResult labelResult = new LabelResult();
                    BeanUtils.copyProperties(label, labelResult);
                    List<FriendResult> friendList1 = new ArrayList<FriendResult>();
                    friendResultList.forEach(new Consumer<FriendResult>() {
                        @Override
                        public void accept(FriendResult friend) {
                            if (friend.getLabelId() == null) {
                                defaultLabelList.add(friend);
                            } else if (label.getId().equals(friend.getLabelId())){
                                friendList1.add(friend);
                            }
                        }
                    });
                    labelResult.setFriendList(friendList1);
                    labelResultList.add(labelResult);
                }
            });
            if (defaultLabelList.size() > 0) {
                defaultLabel.setFriendList(defaultLabelList);
                labelResultList.add(defaultLabel);
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("labelList", labelResultList);
        return ResultUtil.success(map);
    }
}
