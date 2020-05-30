package com.example.im.service.impl;

import com.example.im.dao.DistrictDao;
import com.example.im.dto.DistrictDTO;
import com.example.im.dto.ProvinceDTO;
import com.example.im.service.DistrictService;
import com.example.im.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author HuJun
 * @date 2020/5/20 3:13 下午
 */
@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictDao districtDao;
    @Override
    public Map<Integer, ProvinceDTO> findDistrict() {
        List<DistrictDTO> districtDTOList =  districtDao.findDistrict();
        Map<Integer, ProvinceDTO> provinceDTOMap = new HashMap<>();
        for (DistrictDTO item:
                districtDTOList) {
            Integer provinceId = item.getProvinceId();
            ProvinceDTO.CityDTO.DistrictDTO districtDTO = new ProvinceDTO.CityDTO.DistrictDTO();
            BeanUtil.copyProperties(item, districtDTO);
            if (provinceDTOMap.containsKey(provinceId)) {
                Integer cityId = item.getCityId();
                if (provinceDTOMap.get(provinceId).getCityDTOMap().containsKey(cityId)) {
                    provinceDTOMap.get(provinceId).getCityDTOMap().get(cityId).getDistrictDTOMap().put(item.getDistrictId(), districtDTO);
                } else {
                    ProvinceDTO.CityDTO cityDTO = new ProvinceDTO.CityDTO();
                    BeanUtil.copyProperties(item, cityDTO);
                    Map<Integer, ProvinceDTO.CityDTO.DistrictDTO> districtDTOMap = new HashMap<>();
                    districtDTOMap.put(item.getDistrictId(), districtDTO);
                    cityDTO.setDistrictDTOMap(districtDTOMap);
                    provinceDTOMap.get(provinceId).getCityDTOMap().put(item.getCityId(), cityDTO);
                }
            } else {
                ProvinceDTO.CityDTO cityDTO = new ProvinceDTO.CityDTO();
                BeanUtil.copyProperties(item, cityDTO);
                Map<Integer, ProvinceDTO.CityDTO.DistrictDTO> districtDTOMap = new HashMap<>();
                districtDTOMap.put(item.getDistrictId(), districtDTO);
                cityDTO.setDistrictDTOMap(districtDTOMap);
                ProvinceDTO provinceDTO = new ProvinceDTO();
                BeanUtil.copyProperties(item, provinceDTO);
                Map<Integer, ProvinceDTO.CityDTO> cityDTOMap = new HashMap<>();
                cityDTOMap.put(item.getCityId(), cityDTO);
                provinceDTO.setCityDTOMap(cityDTOMap);
                provinceDTOMap.put(provinceId, provinceDTO);
            }
        }
        return provinceDTOMap;
    }

    @Override
    public DistrictDTO findByDistrictId(Integer districtId) {
        return districtDao.findByDistrictId(districtId);
    }
}
