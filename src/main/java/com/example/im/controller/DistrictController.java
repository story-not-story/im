package com.example.im.controller;

import com.example.im.result.Result;
import com.example.im.service.DistrictService;
import com.example.im.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuJun
 * @date 2020/5/20 3:57 下午
 */
@Api(tags = "地区接口")
@RestController
@RequestMapping("/district")
@Slf4j
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @ApiOperation(value = "查找省市区列表或者根据区号查找省市", httpMethod = "GET")
    @ApiImplicitParam(name = "districtId", value = "区号", defaultValue = "500", dataTypeClass = Integer.class, required = false)
    @GetMapping
    public Result findDistrict(@RequestParam(value = "districtId", required = false) Integer districtId) {
        if (districtId == null) {
            return ResultUtil.success(districtService.findDistrict());
        } else {
            return ResultUtil.success(districtService.findByDistrictId(districtId));
        }
    }
}
