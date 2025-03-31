package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Work;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cofmass.clothRoomBackend.vo.WorkSearchVo;

import java.util.List;

/**
* @author zlj
* @description 针对表【work】的数据库操作Service
* @createDate 2024-08-28 14:12:56
*/
public interface WorkService extends IService<Work> {
    public List<String> getImagesById(String workId);
     public Page<Work> pageWork(int currentPage, int pageSize, WorkSearchVo param);
}
