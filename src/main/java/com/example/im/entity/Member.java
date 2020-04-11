package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author HuJun
 * @date 2020/3/21 7:23 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Member {
    @Id
    private String id;
    private String groupId;
    private String userId;
    private Byte grade = 0;
    private String name;
}
