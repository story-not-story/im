package com.example.im.dao;

import com.example.im.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface InvitationDao extends JpaRepository<Invitation, String> {
    /**
     * 根据userId查找添加好友申请列表（30天有效期）
     * @param userId
     * @return
     */
    @Query(value = "select * from invitation i where (i.receiver_id = :userId or i.sender_id = :userId) and  i.gmt_create = (select max(gmt_create) from invitation where receiver_id = i.receiver_id and sender_id = i.sender_id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY))", nativeQuery = true)
    List<Invitation> findByUserId(@Param("userId") String userId);

    /**
     * 根据id查找添加好友申请（30天有效期）
     * @param id
     * @return
     */
    @Override
    @Query(value = "select * from invitation where id = :id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY)", nativeQuery = true)
    Optional<Invitation> findById(@Param("id") String id);
}
