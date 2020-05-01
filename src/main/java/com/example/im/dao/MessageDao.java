package com.example.im.dao;

import com.example.im.entity.Message;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "select * from message where is_group = true and receiver_id = ?1 and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH) order by ?#{#pageable}",
            countQuery = "select count(*) from message where is_group = true and receiver_id = ?1 and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    List<Message> findByGroupId(String groupId, Pageable pageable);

    @Query(value = "(select * from message m where m.is_group = true and m.receiver_id in (:groupIdList) and m.gmt_create = " +
            "(select max(gmt_create) from message where is_group = true and receiver_id = m.receiver_id and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)) " +
            "order by m.gmt_create desc limit 20) " +
            " union " +
            "(select * from message m where m.is_group = false and (m.receiver_id = :userId or m.sender_id = :userId) and m.gmt_create = " +
            "(select max(gmt_create) from message where is_group = false and (receiver_id = m.receiver_id and sender_id = m.sender_id or receiver_id = m.sender_id and sender_id = m.receiver_id) and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)) " +
            "order by m.gmt_create desc limit 20) order by gmt_create desc limit 20",nativeQuery = true)
    List<Message> findTop(@Param("userId") String userId,@Param("groupIdList") List<String> groupIdList);
    /**
     * 根据userId查找正常状态的消息（6个月有效期）
     * @param userId
     * @param friendId
     * @return
     */
    @Query(value = "select * from message where is_group = false and (receiver_id = ?1 and status in (0, 3) && sender_id = ?2 or receiver_id = ?2 && sender_id = ?1 and status in (0, 2)) and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH) order by ?#{#pageable}",
            countQuery = "select count(*) from message where is_group = false and (receiver_id = ?1 and status in (0, 3) && sender_id = ?2 or receiver_id = ?2 && sender_id = ?1 and status in (0, 2)) and status = 0 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    List<Message> findByFriend(String userId, String friendId, Pageable pageable);
    @Query(value = "select * from message where (is_group = false and (receiver_id = ?1 and status in (0, 3) or sender_id = ?1 and status in (0, 2)) or is_group = true and sender_id = ?1 and status in (0, 2)) and content like %?2% and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByContentLike(String userId, String content);
}
