package com.example.im.controller;

import com.example.im.entity.Friend;
import com.example.im.entity.Label;
import com.example.im.entity.User;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.LabelException;
import com.example.im.exception.UserException;
import com.example.im.result.FriendResult;
import com.example.im.result.LabelResult;
import com.example.im.result.Result;
import com.example.im.service.FriendService;
import com.example.im.service.LabelService;
import com.example.im.service.UserService;
import com.example.im.util.CharUtil;
import com.example.im.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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

    @GetMapping("/friendlist")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        Map<Character, List<FriendResult>> letterMap = new TreeMap<>();
        Map<String, Object> map = new HashMap<>(3);
        char other = '#';
        List<FriendResult> friendResultList = friendList.stream().map(new Function<Friend, FriendResult>() {
            @Override
            public FriendResult apply(Friend friend) {
                FriendResult friendResult = new FriendResult();
                if (userId.equals(friend.getUserId())){
                    friendResult.setFriendId(friend.getFriendId());
                    friendResult.setIsBlacklisted(friend.getIsUserBlacklisted());
                    friendResult.setLabelId(friend.getULabelId());
                    friendResult.setRemark(friend.getURemark());
                } else {
                    friendResult.setFriendId(friend.getUserId());
                    friendResult.setIsBlacklisted(friend.getIsFriendBlacklisted());
                    friendResult.setLabelId(friend.getFLabelId());
                    friendResult.setRemark(friend.getFRemark());
                }
                String remark = friendResult.getRemark();
                if (remark == null) {
                    User user =userService.findById(friendResult.getFriendId());
                    if (user == null) {
                        log.error("【查找分组好友列表】好友未注册");
                        throw new UserException(ErrorCode.USER_NOT_EXISTS);
                    }
                    remark = user.getName();
                    friendResult.setRemark(remark);
                }
                char letter = CharUtil.toUpper(remark.charAt(0));
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
                return friendResult;
            }
        }).collect(Collectors.toList());
        List<Label> labelList = labelService.findByUserId(userId);
        List<FriendResult> defaultLabelList = new ArrayList<FriendResult>();
        List<LabelResult> labelResultList = new ArrayList<>(labelList.size());
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
        if (labelList == null || labelList.isEmpty()) {
            map.put("defaultLabel", friendResultList);
        } else {
            map.put("defaultLabel", defaultLabelList);
        }
        map.put("labelList", labelResultList);
        map.put("letterMap", letterMap);
        return ResultUtil.success(map);
    }
}
