package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.cofmass.clothRoomBackend.entity.LikeTb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zlj
* @description 针对表【like_tb】的数据库操作Service
* @createDate 2024-09-19 16:36:32
*/
public interface LikeTbService extends IService<LikeTb> {
    public Page<LikeTb> pageLike(int currentPage, int pageSize);

}
