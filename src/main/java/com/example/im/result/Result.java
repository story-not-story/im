package com.example.im.result;

import lombok.Data;

import java.io.Serializable;

/**
 * Result class
 *
 * @author hujun
 * @date 2020/02/05
 */
@Data
public class Result<T> {
    private static final long serialVersionUID = 4714655899358191585L;
    private Integer code;
    private String msg;
    private T data;
}
