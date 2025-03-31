package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Image;
import com.cofmass.clothRoomBackend.service.ImageService;
import com.cofmass.clothRoomBackend.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author zlj
* @description 针对表【image】的数据库操作Service实现
* @createDate 2024-09-13 16:18:15
*/
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
    implements ImageService{

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public List<String> listById(String imageId) {
        return imageMapper.listById(imageId);
    }
}




