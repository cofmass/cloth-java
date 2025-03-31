package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.City;
import com.cofmass.clothRoomBackend.service.CityService;
import com.cofmass.clothRoomBackend.mapper.CityMapper;
import org.springframework.stereotype.Service;

/**
* @author 96307
* @description 针对表【city(全国城市表格)】的数据库操作Service实现
* @createDate 2024-10-28 14:15:55
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService{

}




