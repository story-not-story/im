package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author HuJun
 * @date 2020/3/21 7:34 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Invitation {
    @Id
    private String id;
    @NotNull
    private String senderId;
    @NotNull
    private String receiverId;
    private Integer labelId;
    private String message;
    private Boolean isAccepted;
    private String remark;
    private Timestamp gmtCreate;
}
