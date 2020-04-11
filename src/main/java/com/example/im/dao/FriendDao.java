package com.example.im.dao;

import com.example.im.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:45 下午
 */
public interface FriendDao extends JpaRepository<Friend, String> {
    /**
     * 查找两人之间是否是好友关系
     * @param userId
     * @param friendId
     * @return
     */
    @Query("select f from Friend f where f.userId = :userId and f.friendId = :friendId or f.userId = :friendId and f.friendId = :userId")
    Friend find(@Param("userId") String userId, @Param("friendId") String friendId);

    /**
     * 根据userId查找所有好友
     * @param userId
     * @return
     */
    @Query("select f from Friend f where f.userId = :userId or f.friendId = :userId")
    List<Friend> findAll(@Param("userId") String userId);
}
