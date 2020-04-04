package com.example.im.util;

import com.example.im.enums.ErrorCode;
import com.example.im.result.Result;

/**
 * @author: HuJun
 * @date: 2020/3/21 6:55 下午
 */
public class ResultUtil {
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static Result success(){
        return success(null);
    }

    public static Result error(ErrorCode errorCode){
        Result result = new Result();
        result.setCode(errorCode.getCode());
        result.setMsg(errorCode.getMsg());
        return result;
    }

    public static Result error(Integer code, String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
