package com.example.im.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "用户信息")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class User {
    @ApiModelProperty(name = "id", value = "用户ID", dataType = "String", required = true, example = "1586969516508397974")
    @Id
    @NotNull
    private String id;
    @ApiModelProperty(name = "password", value = "密码", dataType = "String", example = "jsldj3445")
    private String password;
    @ApiModelProperty(name = "name", value = "昵称", dataType = "String", example = "jojo")
    private String name;
    @ApiModelProperty(name = "avatar", value = "头像", dataType = "String", example = "1.jpg")
    private String avatar;
    @ApiModelProperty(name = "sex", value = "性别", dataType = "Boolean", example = "0")
    private Boolean sex;
    @ApiModelProperty(name = "birthdate", value = "出生日期", dataType = "Date", example = "2019年-12月-23日")
    private Date birthdate;
    @ApiModelProperty(name = "cityId", value = "城市ID", dataType = "Integer", example = "1")
    private Integer cityId;
    @ApiModelProperty(name = "cityName", value = "城市名称", dataType = "String", example = "北京")
    private String cityName;
    @ApiModelProperty(name = "signature", value = "个性签名", dataType = "String", example = "天天向上")
    private String signature;
    @ApiModelProperty(name = "phone", value = "电话", dataType = "String", example = "13745933421")
    private String phone;
}
