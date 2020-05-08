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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "分组接口")
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
    @ApiOperation(value = "创建分组 ", httpMethod = "POST")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true),
                    @ApiImplicitParam(name = "name", value = "分组名称", defaultValue = "大学", dataTypeClass = String.class, required = true)
            }
    )
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

    @ApiOperation(value = "修改分组", httpMethod = "PUT")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "id", value = "分组id", defaultValue = "1", dataTypeClass = Integer.class, required = true),
                    @ApiImplicitParam(name = "name", value = "分组名称", defaultValue = "老师", dataTypeClass = String.class, required = true)
            }
    )
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

    @ApiOperation(value = "删除分组", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "分组id", defaultValue = "1", dataTypeClass = Integer.class, required = true)
    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        Label label = labelService.findById(id);
        String userId = label.getUserId();
        List<Friend> friendList = friendService.findAll(userId).stream().filter(o -> {
            if (userId.equals(o.getUserId())) {
                return id.equals(o.getULabelId());
            } else {
                return id.equals(o.getFLabelId());
            }
        }).collect(Collectors.toList());
        friendService.save(friendList);
        labelService.deleteById(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "分组列表", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping
    public Result findAll(@RequestParam String userId){
        List<Label> labelList = labelService.findByUserId(userId);
        return ResultUtil.success(labelList);
    }

    @ApiOperation(value = "分组列表（含好友）", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "1586969516508397974", dataTypeClass = String.class, required = true)
    @GetMapping("/friendlist")
    public Result list(@RequestParam String userId){
        List<Friend> friendList = friendService.findAll(userId);
        List<Label> labelList = labelService.findByUserId(userId);
        Set<FriendResult> defaultLabelSet = new HashSet<>();
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
                                defaultLabelSet.add(friend);
                            } else if (label.getId().equals(friend.getLabelId())){
                                friendList1.add(friend);
                            }
                        }
                    });
                    labelResult.setFriendList(friendList1);
                    labelResultList.add(labelResult);
                }
            });
            if (defaultLabelSet.size() > 0) {
                defaultLabel.setFriendList(defaultLabelSet.stream().collect(Collectors.toList()));
                labelResultList.add(defaultLabel);
            }
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("labelList", labelResultList);
        return ResultUtil.success(map);
    }
}
