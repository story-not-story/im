package com.example.im.service;

import com.example.im.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/4/3 1:54 下午
 */
public interface MessageService {
    /**
     * 查找最新的20条消息
     * @param userId
     * @return
     */
    List<Message> findTop(String userId);
    /**
     * 根据groupId查找群消息
     * @param groupId
     * @return
     */
    List<Message> findByGroupId(String userId, String groupId, Pageable pageable);

    /**
     * 根据好友关系查找消息
     * @param userId
     * @param friendId
     * @return
     */
    List<Message> findByFriend(String userId, String friendId, Pageable pageable);

    /**
     * 根据groupId删除群消息
     * @param groupId
     * @return
     */
    void deleteByGroupId(String userId, String groupId);

    /**
     * 根据好友关系删除消息
     * @param userId
     * @param friendId
     * @return
     */
    void deleteByFriend(String userId, String friendId);
    /**
     * 根据id查找消息
     * @param id
     * @return
     */
    Message findById(String id);

    /**
     * 保存消息
     * @param message
     * @return
     */
    Message save(Message message);

    /**
     * 撤回消息
     * @param id
     * @return
     */
    Message cancel(String id);

    /**
     * 删除消息
     * @param id
     * @return
     */
    Message delete(String id, String userId);

    /**
     * 根据userId查找模糊匹配content的消息列表
     * @param userId
     * @param content
     * @return
     */
    List<Message> findByContentLike(String userId, String content);

    /**
     * 根据groupId查找模糊匹配content的群消息
     * @param groupId
     * @return
     */
    List<Message> findByGroupContent(String userId, String groupId, String content, Pageable pageable);

    /**
     * 根据好友关系查找模糊匹配content的消息
     * @param userId
     * @param friendId
     * @return
     */
    List<Message> findByFriendContent(String userId, String friendId, String content, Pageable pageable);
}
