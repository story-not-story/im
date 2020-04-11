package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author HuJun
 * @date 2020/4/2 5:04 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Login {
    @Id
    private String id;
    private String ip;
    private Integer port;
    private Boolean status;
    private String userId;
}
