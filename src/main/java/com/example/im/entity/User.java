package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author HuJun
 * @date 2020/3/21 6:15 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    private String id;

    private String password;

    private String name;

    private String avatar = "favatar.png";

    private Boolean sex;

    private Date birthdate;

    private Integer districtId;

    private String signature;

    private String phone;
}
