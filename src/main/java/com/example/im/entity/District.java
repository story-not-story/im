package com.example.im.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author HuJun
 * @date 2020/5/20 1:04 下午
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class District {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer pid;

    private String districtName;

    private Integer type;
}
