package com.example.im.form;

import com.example.im.validate.Phone;
import com.example.im.validate.TextRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HuJun
 * @date 2020/5/20 7:13 下午
 */
@ApiModel(value = "用户信息")
@Data
public class UserForm {
    @ApiModelProperty(name = "id", value = "用户ID", dataType = "String", required = true, example = "1586969516508397974")
    @TextRange(min = 19, max = 19)
    private String id;
    @ApiModelProperty(name = "name", value = "昵称", dataType = "String", example = "jojo")
    @TextRange(canNull = true)
    private String name;
    @ApiModelProperty(name = "sex", value = "性别", dataType = "Boolean", example = "0")
    private Boolean sex;
    @ApiModelProperty(name = "birthdate", value = "出生日期", dataType = "Date", example = "2019年-12月-23日")
    private String birthdate;
    @ApiModelProperty(name = "districtId", value = "区号", dataType = "Integer", example = "500")
    private Integer districtId;
    @ApiModelProperty(name = "signature", value = "个性签名", dataType = "String", example = "天天向上")
    @TextRange(canNull = true, max = 80)
    private String signature;
    @ApiModelProperty(name = "phone", value = "电话", dataType = "String", example = "13745933421")
    @Phone(canNull = true)
    private String phone;
}
