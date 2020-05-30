package com.example.im.dao;


import com.example.im.dto.DistrictDTO;
import com.example.im.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/5/20 1:14 下午
 */
public interface DistrictDao extends JpaRepository<District, Integer> {
    /**
     * 列出所有省市区的关系
     * @return
     */
    @Query(value = "select new com.example.im.dto.DistrictDTO(p.id, p.districtName, c.id, c.districtName, d.id, d.districtName)  from District d, District c, District p where d.pid = c.id and c.pid = p.id and d.type = 3")
    List<DistrictDTO> findDistrict();

    /**
     * 根据区号查找所处省市
     * @param districtId
     * @return
     */
    @Query(value = "select new com.example.im.dto.DistrictDTO(p.id, p.districtName, c.id, c.districtName, d.id, d.districtName)  from District d, District c, District p where d.pid = c.id and c.pid = p.id and d.id = ?1")
    DistrictDTO findByDistrictId(Integer districtId);
}
