package com.example.im.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * Result class
 *
 * @author hujun
 * @date 2020/02/05
 */
@ApiModel(value = "统一返回结果格式")
@Data
public class Result<T> {
    private static final long serialVersionUID = 4714655899358191585L;
    @ApiModelProperty(name = "code", value = "错误码", dataType = "int", example = "0", required = true)
    private Integer code;
    @ApiModelProperty(name = "msg", value = "提示信息", dataType = "String", example = "success", required = true)
    private String msg;
    @ApiModelProperty(name = "data", value = "内容", dataType = "T")
    private T data;
}
