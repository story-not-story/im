package com.example.im.handler;

import com.example.im.config.UrlConfig;
import com.example.im.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private UrlConfig urlConfig;
    @ExceptionHandler(UserException.class)
    public String userExceptionHandler(){
        return "redirect:" + urlConfig.getHost() + "/login";
    }
}
