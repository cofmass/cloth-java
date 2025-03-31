package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.service.WorkService;
import com.cofmass.clothRoomBackend.mapper.WorkMapper;
import com.cofmass.clothRoomBackend.vo.WorkSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

/**
* @author zlj
* @description 针对表【work】的数据库操作Service实现
* @createDate 2024-08-28 14:12:56
*/
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work>
    implements WorkService{

    @Autowired
    WorkMapper workMapper;

    @Override
    public List<String> getImagesById(String workId) {
        List<String> imagesUrl = workMapper.getImagesById(workId);
        List<String> image64 = new ArrayList<>();
        for (String s :imagesUrl) {
            String base64 = convertFileToBase64(s);
            image64.add(base64);
        }
        return image64;
    }

    @Override
    public Page<Work> pageWork(int currentPage, int pageSize, WorkSearchVo param) {
        //        这里会出现报错，是关键字报错
        Page<Work> page = new Page<>(currentPage, pageSize);
//        关闭就不报错了
        page.setOptimizeCountSql(false);
        if (param != null){
            Page<Work> pageWork = page(page, new LambdaQueryWrapper<Work>()
                    .like(Work::getWorkTitle, param.getWorkTitle())
                    .like(Work::getWorkContent, param.getWorkContent())
                    .like(Work::getUserName, param.getUserName()));
            return pageWork;
        }else {
            Page<Work> pageWork = page(page);
            return pageWork;
        }
    }
}




