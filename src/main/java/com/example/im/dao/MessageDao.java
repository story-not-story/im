package com.example.im.dao;

import com.example.im.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface MessageDao extends JpaRepository<Message, String> {
    /**
     * 根据groupId查找正常状态的消息（6个月有效期）
     * @param groupId
     * @return
     */
    @Query(value = "select * from message where is_group = true and receiver_id = :groupId and status = 0 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByGroupId(@Param("groupId")String groupId);

    /**
     * 根据userId查找正常状态的消息（6个月有效期）
     * @param userId
     * @param friendId
     * @return
     */
    @Query(value = "select * from message where is_group = false and (receiver_id = :userId && sender_id = :friendId or receiver_id = :friendId && sender_id = :userId) and status = 0 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByFriend(@Param("userId")String userId, @Param("friendId")String friendId);
}
