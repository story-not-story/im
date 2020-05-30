package com.example.im.dao;

import com.example.im.entity.Message;
import org.springframework.data.domain.Page;
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
     * 根据groupId分页查找非撤回状态的消息（6个月有效期）
     * @param groupId
     * @return
     */
    @Query(value = "select * from message where is_group = true and receiver_id = ?1 and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            countQuery = "select count(*) from message where is_group = true and receiver_id = ?1 and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    Page<Message> findByGroupId(String groupId, Pageable pageable);

    /**
     * 根据groupId分页查找非撤回状态的消息（6个月有效期）
     * @param groupId
     * @return
     */
    @Query(value = "select * from message where is_group = true and receiver_id = ?1 and content like %?2% and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            countQuery = "select count(*) from message where is_group = true and receiver_id = ?1 and content like %?2% and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    Page<Message> findByContentLike(String groupId, String content, Pageable pageable);

    /**
     * 根据groupId查找非撤回状态的消息（6个月有效期）
     * @param groupId
     * @return
     */
    @Query(value = "select * from message where is_group = true and receiver_id = ?1 and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByGroupId(String groupId);

    /**
     * 根据userId查找6个月有效期内的最新的20条消息（包括1v1和群消息列表）
     * @param userId
     * @param groupIdList
     * @return
     */
    @Query(value = "(select * from message m where m.is_group = true and m.receiver_id in (:groupIdList) and m.gmt_create = " +
            "(select max(gmt_create) from message where is_group = true and receiver_id = m.receiver_id and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)) " +
            "order by m.gmt_create desc limit 20) " +
            " union " +
            "(select * from message m where m.is_group = false and (m.receiver_id = :userId or m.sender_id = :userId) and m.gmt_create = " +
            "(select max(gmt_create) from message where is_group = false and (receiver_id = m.receiver_id and sender_id = m.sender_id or receiver_id = m.sender_id and sender_id = m.receiver_id) and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)) " +
            "order by m.gmt_create desc limit 20) order by gmt_create desc limit 20",nativeQuery = true)
    List<Message> findTop(@Param("userId") String userId, @Param("groupIdList") List<String> groupIdList);
    /**
     * 根据userId分页查找非撤回状态的消息（6个月有效期）
     * @param userId
     * @param friendId
     * @return
     */
    @Query(value = "select * from message where is_group = false and (receiver_id = ?1 and sender_id = ?2 or receiver_id = ?2 and sender_id = ?1) and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            countQuery = "select count(*) from message where is_group = false and (receiver_id = ?1 and sender_id = ?2 or receiver_id = ?2 and sender_id = ?1) and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    Page<Message> findByFriend(String userId, String friendId, Pageable pageable);

    /**
     * 根据userId分页查找非撤回状态的消息（6个月有效期）
     * @param userId
     * @param friendId
     * @return
     */
    @Query(value = "select * from message where is_group = false and (receiver_id = ?1 and sender_id = ?2 or receiver_id = ?2 and sender_id = ?1) and content like %?3% and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            countQuery = "select count(*) from message where is_group = false and (receiver_id = ?1 and sender_id = ?2 or receiver_id = ?2 and sender_id = ?1) and content like %?3% and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)",
            nativeQuery = true)
    Page<Message> findByContentLike(String userId, String friendId, String content, Pageable pageable);

    /**
     * 根据userId查找非撤回状态的消息（6个月有效期）
     * @param userId
     * @param friendId
     * @return
     */
    @Query(value = "select * from message where is_group = false and (receiver_id = ?1 and sender_id = ?2 or receiver_id = ?2 and sender_id = ?1) and status != 1 and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByFriend(String userId, String friendId);

    /**
     * 根据userId查找消息内容模糊匹配content的非撤回状态的1v1和群消息列表（6个月有效期）
     * @param userId
     * @param content
     * @param groupIdList
     * @return
     */
    @Query(value = "select * from message where (is_group = false and (receiver_id = ?1 or sender_id = ?1) or is_group = true and receiver_id in ?3) and status != 1 and content like %?2% and gmt_create > date_sub(current_timestamp(),INTERVAL 6 MONTH)", nativeQuery = true)
    List<Message> findByContentLike(String userId, String content, List<String> groupIdList);
}
