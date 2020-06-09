package com.example.im.controller;

import com.example.im.config.WebMvcConfig;
import com.example.im.result.Result;
import com.example.im.util.ResultUtil;
import com.example.im.util.UploadUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuJun
 * @date 2020/6/7 2:56 下午
 */
@Api(tags = "文件接口")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Autowired
    private WebMvcConfig webMvcConfig;
    @GetMapping("/isExists")
    public Result isExists(@RequestParam String name) {
        return ResultUtil.success(UploadUtil.isExists(webMvcConfig.getFileUrl(), name));
    }
}
