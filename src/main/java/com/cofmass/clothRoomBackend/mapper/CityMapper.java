package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.City;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cofmass.clothRoomBackend.vo.CityListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 96307
* @description 针对表【city(全国城市表格)】的数据库操作Mapper
* @createDate 2024-10-28 14:15:55
* @Entity com.cofmass.clothRoomBackend.entity.City
*/
@Mapper
public interface CityMapper extends BaseMapper<City> {

}




