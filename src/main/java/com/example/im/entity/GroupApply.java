package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/4/11 8:00 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class GroupApply {
    @Id
    private String id;
    @NotNull
    private String userId;
    @NotNull
    private String groupId;
    private String message;
    private Boolean isAccepted;
}
