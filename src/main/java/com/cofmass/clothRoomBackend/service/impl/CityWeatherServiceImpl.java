package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.CityWeather;
import com.cofmass.clothRoomBackend.service.CityWeatherService;
import com.cofmass.clothRoomBackend.mapper.CityWeatherMapper;
import org.springframework.stereotype.Service;

/**
* @author 96307
* @description 针对表【city_weather(城市天气关联表)】的数据库操作Service实现
* @createDate 2024-10-29 09:45:27
*/
@Service
public class CityWeatherServiceImpl extends ServiceImpl<CityWeatherMapper, CityWeather>
    implements CityWeatherService{

}




