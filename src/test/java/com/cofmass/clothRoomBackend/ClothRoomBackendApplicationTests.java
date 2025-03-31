package com.cofmass.clothRoomBackend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.*;
import com.cofmass.clothRoomBackend.mapper.AdminMapper;
import com.cofmass.clothRoomBackend.mapper.CityMapper;
import com.cofmass.clothRoomBackend.mapper.WorkMapper;
import com.cofmass.clothRoomBackend.service.AdminService;
import com.cofmass.clothRoomBackend.service.CityWeatherService;
import com.cofmass.clothRoomBackend.service.PredictWeatherService;
import com.cofmass.clothRoomBackend.service.WorkService;
import com.cofmass.clothRoomBackend.vo.WorkListVo;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@SpringBootTest
class ClothRoomBackendApplicationTests {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private PredictWeatherService predictWeatherService;

    @Autowired
    private CityWeatherService cityWeatherService;
    @Autowired
    private WorkService workService;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AdminService adminService;

    @Autowired
    private WorkMapper workMapper;

    @Test
    void contextLoads() throws JSONException {
        String s = convertFileToBase64("E:\\VSCodeStore\\cloth-room\\src\\static\\images\\humen\\m2_out.jpeg");
        System.out.println(s);
    }

    @Test
    void test2()  {
        adminMapper.selectList(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId,1));
    }


    @Test
    void test3() {
        List<WorkData> workList = workMapper.getWorkList();
        WorkListVo workListVo = new WorkListVo();
        List<Columns> columns = new ArrayList<>();
        Columns fistC = new Columns("封面图片", "indexImg", "indexImg");
        fistC.setScopedSlots(new ScopedSlots("indexImg"));
        columns.add(fistC);
        columns.add(new Columns("作者头像","avatarImg","avatarImg"));
        columns.add(new Columns("作者名称","userName","userName"));
        columns.add(new Columns("作品标题","workTitle","workTitle"));
        columns.add(new Columns("作品内容","workContent","workContent"));
        columns.add(new Columns("创建时间","createTime","createTime"));
        workListVo.setColumns(columns);
        workListVo.setData(workList);

    }

    @Test
    void test4(){
        System.out.println(workMapper.getNewWorkNum());
    }

}
