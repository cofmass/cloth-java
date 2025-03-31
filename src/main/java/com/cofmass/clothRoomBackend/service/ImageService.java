package com.cofmass.clothRoomBackend.service;

import com.cofmass.clothRoomBackend.entity.Image;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zlj
* @description 针对表【image】的数据库操作Service
* @createDate 2024-09-13 16:18:15
*/
public interface ImageService extends IService<Image> {
    List<String> listById (String imageId);
}
