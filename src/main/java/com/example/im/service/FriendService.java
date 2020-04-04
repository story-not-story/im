package com.example.im.service;

import com.example.im.entity.Friend;
import com.example.im.entity.Label;
import com.example.im.entity.User;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 9:17 下午
 */
public interface FriendService {
    Friend add(Friend friend);
    void remove(String userId, String friendId);
    Friend blacklist(String userId, String friendId);
    Friend unblacklist(String userId, String friendId);
    Friend save(Friend friend);
    Friend move(String userId, String friendId, int labelId);
    Friend remark(String userId, String friendId, String remark);
    List<Friend> findAll(String userId);
}
