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
    @Query("select i from Invitation i where i.receiverId = :receiverId and i.gmtCreate > date_sub(current_timestamp(),30)")
    List<Invitation> findByReceiverId(@Param("receiverId") String receiverId);
    @Override
    @Query("select i from Invitation i where i.id = :id and i.gmtCreate > date_sub(current_timestamp(),30)")
    Optional<Invitation> findById(@Param("id") String id);
}
