package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zlj
* @description 针对表【comments】的数据库操作Mapper
* @createDate 2024-09-13 16:18:12
* @Entity com.cofmass.clothRoomBackend.entity.Comments
*/
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

}




