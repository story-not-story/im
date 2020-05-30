package com.example.im.service;

import com.example.im.entity.GroupApply;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 3:34 下午
 */
public interface GroupApplyService {
    /**
     * 根据id查找加群申请
     * @param id
     * @return
     */
    GroupApply findById(String id);

    /**
     * 根据userId查找加群申请
     * @param userId
     * @return
     */
    List<GroupApply> findByUserId(String userId);

    /**
     * 根据groupId查找加群申请
     * @param groupId
     * @return
     */
    List<GroupApply> findByGroupId(String groupId);

    /**
     * 同意加群申请
     * @param id
     * @return
     */
    GroupApply accept(String id);

    /**
     * 拒绝加群申请
     * @param id
     * @return
     */
    GroupApply reject(String id);

    /**
     * 保存加群申请
     * @param invitation
     * @return
     */
    GroupApply create(GroupApply invitation);
}
