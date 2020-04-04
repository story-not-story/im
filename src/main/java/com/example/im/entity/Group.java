package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author HuJun
 * @date 2020/3/21 7:06 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Group {
    @Id
    private String id;
    private String name;
    private String signature;
    private String notice;
}
