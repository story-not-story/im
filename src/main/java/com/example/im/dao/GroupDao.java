package com.example.im.dao;

import com.example.im.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:44 下午
 */
public interface GroupDao extends JpaRepository<Group, String> {
    /**
     * 查找名称值为name或者id值为name的群聊列表
     * @param name
     * @return
     */
    @Query("select g from Group g where g.name like %?1% or g.id like %?1%")
    List<Group> findByNameIdLike(String name);
}
