package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author HuJun
 * @date 2020/3/21 7:20 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Friend {
    @Id
    private String id;
    private String userId;
    private String friendId;
    private Boolean isUBlacklisted;
    private Boolean isFBlacklisted;
    private String uRemark;
    private String fRemark;
    private Integer uLabelId;
    private Integer fLabelId;
}
