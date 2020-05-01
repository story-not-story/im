package com.example.im.service;

import com.example.im.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

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
    List<Message> findByGroupId(String groupId, Pageable pageable);

    /**
     * 根据好友关系查找消息
     * @param userId
     * @param friendId
     * @return
     */
    List<Message> findByFriend(String userId, String friendId, Pageable pageable);
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
     *
     * @param userId
     * @param content
     * @return
     */
    List<Message> findByContentLike(String userId, String content);
}
