package com.example.im.service.impl;

import com.example.im.dao.GroupApplyDao;
import com.example.im.entity.GroupApply;
import com.example.im.entity.GroupApply;
import com.example.im.enums.ErrorCode;
import com.example.im.exception.FriendException;
import com.example.im.exception.GroupException;
import com.example.im.service.GroupApplyService;
import com.example.im.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/4/11 8:45 下午
 */
@Service
@Slf4j
public class GroupApplyServiceImpl implements GroupApplyService {
    @Autowired
    private GroupApplyDao groupApplyDao;

    @Override
    public GroupApply findById(String id) {
        Optional<GroupApply> groupApply = groupApplyDao.findById(id);
        if (groupApply.isPresent()) {
            return groupApply.get();
        } else {
            log.error("【根据id查找加群申请】加群申请不存在");
            throw new GroupException(ErrorCode.INVITATION_NOT_FOUND);
        }
    }

    @Override
    public List<GroupApply> findByUserId(String userId) {
        return groupApplyDao.findByUserId(userId);
    }

    @Override
    public List<GroupApply> findByGroupId(String groupId) {
        return groupApplyDao.findByGroupId(groupId);
    }

    @Override
    public GroupApply accept(String id) {
        GroupApply apply = findById(id);
        if (apply.getIsAccepted() != null){
            log.error("【申请加群】该加群申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
        }
        apply.setIsAccepted(true);
        return groupApplyDao.save(apply);
    }

    @Override
    public GroupApply reject(String id) {
        GroupApply apply = findById(id);
        if (apply.getIsAccepted() != null){
            log.error("【拒绝好友添加申请】该加群申请已处理");
            throw new FriendException(ErrorCode.INVITATION_ALREADY_HANDLE);
        }
        apply.setIsAccepted(false);
        return groupApplyDao.save(apply);
    }

    @Override
    public GroupApply create(GroupApply apply) {
        apply.setId(KeyUtil.getUniqueKey());
        return groupApplyDao.save(apply);
    }
}
