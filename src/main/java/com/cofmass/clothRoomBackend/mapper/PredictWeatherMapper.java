package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.PredictWeather;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 96307
* @description 针对表【predict_weather(城市天气预测表)】的数据库操作Mapper
* @createDate 2024-10-29 10:34:42
* @Entity com.cofmass.clothRoomBackend.entity.PredictWeather
*/
@Mapper
public interface PredictWeatherMapper extends BaseMapper<PredictWeather> {

}




