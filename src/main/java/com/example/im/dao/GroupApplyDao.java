package com.example.im.dao;

import com.example.im.entity.GroupApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface GroupApplyDao extends JpaRepository<GroupApply, String> {
    /**
     * 根据groupId查找加群申请（30天有效期）
     * @param groupId
     * @return
     */
    @Query(value = "select * from group_apply g where g.group_id = :groupId and g.gmt_create = (select max(gmt_create) from group_apply where user_id = g.user_id and group_id = g.group_id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY))", nativeQuery = true)
    List<GroupApply> findByGroupId(@Param("groupId") String groupId);

    /**
     * 根据userId查找加群申请（30天有效期）
     * @param userId
     * @return
     */
    @Query(value = "select * from group_apply g where g.user_id = :userId and g.gmt_create = (select max(gmt_create) from group_apply where user_id = g.user_id and group_id = g.group_id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY))", nativeQuery = true)
    List<GroupApply> findByUserId(@Param("userId") String userId);

    /**
     * 根据id查找加群申请（30天有效期）
     * @param id
     * @return
     */
    @Override
    @Query(value = "select * from group_apply where id = :id and gmt_create > date_sub(current_timestamp(),INTERVAL 30 DAY)", nativeQuery = true)
    Optional<GroupApply> findById(@Param("id") String id);
}
