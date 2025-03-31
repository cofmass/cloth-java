package com.cofmass.clothRoomBackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cofmass.clothRoomBackend.entity.City;
import com.cofmass.clothRoomBackend.mapper.CityMapper;
import com.cofmass.clothRoomBackend.mapper.CityWeatherMapper;
import com.cofmass.clothRoomBackend.service.CityService;
import com.cofmass.clothRoomBackend.service.CityWeatherService;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.CityListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
@Api(tags = "城市管理")
public class CityController {

    @Autowired
    private CityWeatherMapper cityWeatherMapper;

    @ApiOperation("显示所有城市名称")
    @GetMapping("/cityList")
    public R getCityList() {
        List<CityListVo> cityList = cityWeatherMapper.getCityList();
        for (int i = 0; i < cityList.size(); i++) {
            CityListVo cityListVo = cityList.get(i);
            cityListVo.setValue(i);
            cityList.set(i,cityListVo);
        }
        if (cityList.isEmpty()){
            return R.error("查询失败");
        }
        return R.ok("查询成功").put("data",cityList);
    }
}
