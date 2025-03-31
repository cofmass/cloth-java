package com.cofmass.clothRoomBackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cofmass.clothRoomBackend.entity.CityWeather;
import com.cofmass.clothRoomBackend.entity.LikeTb;
import com.cofmass.clothRoomBackend.entity.PredictWeather;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.mapper.CityMapper;
import com.cofmass.clothRoomBackend.service.CityWeatherService;
import com.cofmass.clothRoomBackend.service.PredictWeatherService;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.utils.高德地图Key;
import com.cofmass.clothRoomBackend.vo.CityListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/weather")
@Api(tags = "天气管理")
public class WeatherController {

    @Autowired
    private CityWeatherService cityWeatherService;

    @Autowired
    private PredictWeatherService predictWeatherService;

    @Autowired
    private UserService userService;

    @GetMapping("/predict")
    @ApiOperation("获取预测数据")
    public R getPredict(@RequestParam String cityId){
        User loginUser = userService.getLoginUser();
        CityWeather cityWeather = cityWeatherService.getById(cityId);
        String predictId = cityWeather.getPredictId();
        List<PredictWeather> list = predictWeatherService.list(new LambdaQueryWrapper<PredictWeather>().eq(PredictWeather::getPredictId, predictId));
        if (list.isEmpty()){
            return R.error("获取失败");
        }
//        用户设置了哪里的天气他就是哪里人
        loginUser.setAddress(cityWeather.getCity());
        loginUser.setAvatarUrl(null);
        loginUser.setBgUrl(null);
        userService.updateById(loginUser);
        return R.ok("获取成功").put("data",list);
    }

    @GetMapping("/predictList")
    @ApiOperation("获取所有城市的预测数据")
    public R getPredictList(){
        ArrayList<ArrayList> arrayList = new ArrayList();
        // 获取当前时间
        LocalTime now = LocalTime.now();
        // 定义时间范围
        LocalTime startTime = LocalTime.of(8, 30);  // 早上8:30
        LocalTime endTime = LocalTime.of(18, 30);   // 晚上18:30
        boolean isMorning = now.isAfter(startTime) && now.isBefore(endTime);
        List<PredictWeather> predictWeathers = new ArrayList<>();
        List<CityWeather> list = cityWeatherService.list();
        for (CityWeather cityWeather : list) {
            ArrayList<String> tempList = new ArrayList();
            tempList.add(cityWeather.getCity());
            String predictId = cityWeather.getPredictId();
            PredictWeather byId = predictWeatherService.getOne(new LambdaQueryWrapper<PredictWeather>()
                    .eq(PredictWeather::getPredictId, predictId));
            // 判断当前时间是否在指定范围内
            if (isMorning) {
                // 如果当前时间在8:30到18:30之间
                tempList.add(byId.getDayWeather());
                tempList.add(byId.getDayTemp());
                tempList.add(byId.getDayWind()+byId.getDayPower());
            } else {
                // 否则执行代码2
                tempList.add(byId.getNightWeather());
                tempList.add(byId.getNightTemp());
                tempList.add(byId.getNightWind()+byId.getNightPower());
            }
            arrayList.add(tempList);
        }

        return R.ok("获取成功").put("data",arrayList);
    }
}
