package com.example.im.dao;

import com.example.im.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/21 7:46 下午
 */
public interface MessageDao extends JpaRepository<Message, String> {
    @Query("select m from Message m where m.status = 0 and m.gmtCreate > date_sub(current_timestamp(),180)")
    List<Message> find();
}
