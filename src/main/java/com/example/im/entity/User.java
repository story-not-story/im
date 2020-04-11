package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String id;
    private String password;
    private String name;
    private String avatar;
    private Boolean sex;
    private Date birthdate;
    private Integer cityId;
    private String cityName;
    private String signature;
    private String phone;
}
