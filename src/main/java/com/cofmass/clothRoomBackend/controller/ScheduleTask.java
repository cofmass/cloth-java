package com.cofmass.clothRoomBackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cofmass.clothRoomBackend.entity.CityWeather;
import com.cofmass.clothRoomBackend.entity.PredictWeather;
import com.cofmass.clothRoomBackend.mapper.CityMapper;
import com.cofmass.clothRoomBackend.service.CityWeatherService;
import com.cofmass.clothRoomBackend.service.PredictWeatherService;
import com.cofmass.clothRoomBackend.utils.高德地图Key;
import com.cofmass.clothRoomBackend.vo.CityListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private PredictWeatherService predictWeatherService;

    @Autowired
    private CityWeatherService cityWeatherService;

    // 每天上午8:30执行
    @Scheduled(cron = "0 30 8 * * ?")
    public void scheduledTaskMorning() throws JSONException {
        predictWeatherTask();
    }

    // 每天上午11:30执行
    @Scheduled(cron = "0 30 11 * * ?")
    public void scheduledTaskNoon() throws JSONException {
        predictWeatherTask();
    }

    // 每天下午18:30执行
    @Scheduled(cron = "0 30 18 * * ?")
    public void scheduledTaskEvening() throws JSONException {
        predictWeatherTask();
    }

    // 定时获取预测信息
    public void predictWeatherTask() throws JSONException {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo";
        List<CityWeather> list = cityWeatherService.list();
        for (CityWeather c : list) {
            // 构建URL并添加参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("city", c.getCity())
                    .queryParam("extensions", "all")
                    .queryParam("key", 高德地图Key.key);
                URI uri = builder.build().encode().toUri();
                // 发送GET请求
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                 // 输出响应体
                JSONObject jsonObject = new JSONObject(response.getBody());
                JSONArray forecasts = jsonObject.getJSONArray("forecasts");
                Object o = forecasts.get(0);
                JSONObject forecast = new JSONObject(o.toString());
                JSONArray casts = forecast.getJSONArray("casts");
//                这里的casts会获取当天明天后天三天的预测
                PredictWeather predictWeather = new PredictWeather();
//                    只要当天的因为我们每天定时刷新
                    JSONObject cast = new JSONObject(casts.get(0).toString());
    //                添加预测表
                    predictWeather.setPredictId(c.getPredictId());
                    LocalDate PredictDate = LocalDate.parse(cast.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    predictWeather.setPredictDate(PredictDate);
                    predictWeather.setDayWeather(cast.getString("dayweather"));
                    predictWeather.setNightWeather(cast.getString("nightweather"));
                    predictWeather.setDayTemp(cast.getString("daytemp"));
                    predictWeather.setNightTemp(cast.getString("nighttemp"));
                    predictWeather.setDayWind(cast.getString("daywind"));
                    predictWeather.setNightWind(cast.getString("nightwind"));
                    predictWeather.setDayPower(cast.getString("daypower"));
                    predictWeather.setNightPower(cast.getString("nightpower"));

                PredictWeather one = predictWeatherService.getOne(new LambdaQueryWrapper<PredictWeather>().eq(PredictWeather::getPredictId, predictWeather.getPredictId()));
                    if (one == null) {
                        predictWeatherService.save(predictWeather);
                    }else {
                        try {
                            predictWeatherService.update(predictWeather,new LambdaQueryWrapper<PredictWeather>()
                                .eq(PredictWeather::getPredictId,predictWeather.getPredictId()));
                        } catch (Exception e) {
                            System.out.println(e);
                            System.out.println(predictWeather);
                        }
                    }
        }
    }



}
