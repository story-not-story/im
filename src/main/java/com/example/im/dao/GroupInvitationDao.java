package com.example.im.dao;

import com.example.im.entity.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface GroupInvitationDao extends JpaRepository<GroupInvitation, String> {
    /**
     * 根据被邀请者查找邀好友加入群聊的邀请列表（30天有效期）
     * @param receiverId
     * @return
     */
    @Query(value = "select * from group_invitation where receiver_id = :receiverId and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY)", nativeQuery = true)
    List<GroupInvitation> findByReceiverId(@Param("receiverId") String receiverId);

    /**
     * 根据id查找邀好友加入群聊的邀请（30天有效期）
     * @param id
     * @return
     */
    @Override
    @Query(value = "select * from group_invitation where id = :id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY)", nativeQuery = true)
    Optional<GroupInvitation> findById(@Param("id") String id);
}
