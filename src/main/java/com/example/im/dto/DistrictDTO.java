package com.example.im.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HuJun
 * @date 2020/5/20 1:22 下午
 */
@Data
@AllArgsConstructor
public class DistrictDTO {
    private Integer provinceId;

    private String provinceName;

    private Integer cityId;

    private String cityName;

    private Integer districtId;

    private String districtName;
}
