package com.example.im.service;

import com.example.im.entity.Friend;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 9:17 下午
 */
public interface FriendService {
    /**
     * 添加好友
     * @param friend
     * @return
     */
    Friend add(Friend friend);

    /**
     * 删除好友
     * @param userId
     * @param friendId
     */
    void remove(String userId, String friendId);

    /**
     * 判断是否是好友关系
     * @param userId
     * @param friendId
     * @return
     */
    boolean isFriend(String userId, String friendId);

    /**
     * 拉黑好友
     * @param userId
     * @param friendId
     * @return
     */
    Friend blacklist(String userId, String friendId);

    /**
     * 解除拉黑好友
     * @param userId
     * @param friendId
     * @return
     */
    Friend unblacklist(String userId, String friendId);

    /**
     * 移动好友所在分组
     * @param userId
     * @param friendId
     * @param labelId
     * @return
     */
    Friend move(String userId, String friendId, Integer labelId);

    /**
     * 修改好友备注
     * @param userId
     * @param friendId
     * @param remark
     * @return
     */
    Friend remark(String userId, String friendId, String remark);

    /**
     * 根据userId查询好友列表
     * @param userId
     * @return
     */
    List<Friend> findAll(String userId);
}
