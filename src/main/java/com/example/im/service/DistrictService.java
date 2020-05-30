package com.example.im.service;

import com.example.im.dto.DistrictDTO;
import com.example.im.dto.ProvinceDTO;

import java.util.Map;

/**
 * @author HuJun
 * @date 2020/5/20 2:55 下午
 */
public interface DistrictService {
    /**
     * 列出所有省市区的关系
     * @return
     */
    Map<Integer, ProvinceDTO> findDistrict();

    /**
     * 根据区号查找所处省市
     * @param districtId
     * @return
     */
    DistrictDTO findByDistrictId(Integer districtId);
}
