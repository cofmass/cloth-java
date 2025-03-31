package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.Clothes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author zlj
* @description 针对表【clothes】的数据库操作Mapper
* @createDate 2024-09-13 16:18:03
* @Entity com.cofmass.clothRoomBackend.entity.Clothes
*/
@Mapper
public interface ClothesMapper extends BaseMapper<Clothes> {
    @Select("SELECT count(*) FROM clothes WHERE create_time >= CURDATE() AND create_time < CURDATE() + INTERVAL 1 DAY")
    int getNewClothNum();
}




