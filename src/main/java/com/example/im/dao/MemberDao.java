package com.example.im.dao;

import com.example.im.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface MemberDao extends JpaRepository<Member, String> {
    List<Member> findByUserId(String userId);
    List<Member> findByGroupId(String groupId);
    @Query("select m from Member m where m.groupId = :groupId and m.userId = :userId")
    Member find(@Param("groupId") String groupId, @Param("userId") String userId);
}
