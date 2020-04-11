package com.example.im.handler;

import com.example.im.config.UrlConfig;
import com.example.im.exception.*;
import com.example.im.result.Result;
import com.example.im.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author HuJun
 * @date 2020/4/9 10:16 下午
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @Autowired
    private UrlConfig urlConfig;
    @ExceptionHandler(UserException.class)
    public Result userExceptionHandler(UserException e){
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(FriendException.class)
    public Result friendExceptionHandler(FriendException e){
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(GroupException.class)
    public Result groupExceptionHandler(GroupException e){
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(LabelException.class)
    public Result labelExceptionHandler(LabelException e){
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MessageException.class)
    public Result messageExceptionHandler(MessageException e){
        return ResultUtil.error(e.getCode(), e.getMessage());
    }
}
