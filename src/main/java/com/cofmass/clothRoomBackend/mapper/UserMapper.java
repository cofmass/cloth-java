package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author zlj
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-09-13 16:18:49
* @Entity com.cofmass.clothRoomBackend.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
      @Select("SELECT count(*) FROM user WHERE create_time >= CURDATE() AND create_time < CURDATE() + INTERVAL 1 DAY")
      int getNewUserNum();
}




