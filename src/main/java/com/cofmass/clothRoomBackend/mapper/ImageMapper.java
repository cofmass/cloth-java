package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zlj
* @description 针对表【image】的数据库操作Mapper
* @createDate 2024-09-13 16:18:15
* @Entity com.cofmass.clothRoomBackend.entity.Image
*/
@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    @Select("select `image_url` from `image` where `image_id` = #{imageId}")
     List<String> listById(String imageId);
}




