package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.vo.CommentSearchVo;
import com.cofmass.clothRoomBackend.vo.CommentsVo;

import java.util.List;

/**
* @author zlj
* @description 针对表【comments】的数据库操作Service
* @createDate 2024-09-13 16:18:12
*/
public interface CommentsService extends IService<Comments> {
    public List<CommentsVo> listComments (String workId);

    public Page<Comments> pageComments(int currentPage, int pageSize, CommentSearchVo param);


}
