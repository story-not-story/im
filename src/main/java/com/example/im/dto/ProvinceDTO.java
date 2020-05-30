package com.example.im.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HuJun
 * @date 2020/5/20 2:59 下午
 */
@Data
public class ProvinceDTO {
    private Integer provinceId;

    private String provinceName;

    private Map<Integer, CityDTO> cityDTOMap;

    @Data
    public static class CityDTO {
        private Integer cityId;

        private String cityName;

        private Map<Integer, DistrictDTO> districtDTOMap;

        @Data
        public static class DistrictDTO {
            private Integer districtId;

            private String districtName;
        }
    }
}
