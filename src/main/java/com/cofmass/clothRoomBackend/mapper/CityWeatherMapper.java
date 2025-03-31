package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.CityWeather;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cofmass.clothRoomBackend.vo.CityListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 96307
* @description 针对表【city_weather(城市天气关联表)】的数据库操作Mapper
* @createDate 2024-10-29 09:45:27
* @Entity com.cofmass.clothRoomBackend.entity.CityWeather
*/
@Mapper
public interface CityWeatherMapper extends BaseMapper<CityWeather> {
    @Select("select `city` as text from `city_weather`")
    List<CityListVo> getCityList();
}




