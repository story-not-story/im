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
    /**
     * 根据userId查找所在群
     * @param userId
     * @return
     */
    List<Member> findByUserId(String userId);

    /**
     * 根据groupId查找群聊成员
     * @param groupId
     * @return
     */
    List<Member> findByGroupId(String groupId);

    /**
     * 查找该群是否有该群成员
     * @param groupId
     * @param userId
     * @return
     */
    @Query("select m from Member m where m.groupId = :groupId and m.userId = :userId")
    Member find(@Param("groupId") String groupId, @Param("userId") String userId);
}
