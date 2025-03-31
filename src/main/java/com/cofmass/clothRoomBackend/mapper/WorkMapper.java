package com.cofmass.clothRoomBackend.mapper;

import com.cofmass.clothRoomBackend.entity.Work;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cofmass.clothRoomBackend.entity.WorkData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zlj
* @description 针对表【work】的数据库操作Mapper
* @createDate 2024-09-13 16:19:09
* @Entity com.cofmass.clothRoomBackend.entity.Work
*/
@Mapper
public interface WorkMapper extends BaseMapper<Work> {
  @Select("select `image_url` from `image` where `image_id` = #{workId}")
  List<String> getImagesById(String workId);

  @Select("select `index_img`,`avatar_img`,`user_name`,`work_title`,`work_content`,`create_time` from `work`")
  List<WorkData> getWorkList();

  @Select("SELECT count(*) FROM work WHERE create_time >= CURDATE() AND create_time < CURDATE() + INTERVAL 1 DAY")
  int getNewWorkNum();


}




