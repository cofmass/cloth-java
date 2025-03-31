package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cofmass.clothRoomBackend.vo.UserSearchVo;

/**
* @author zlj
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-08-28 15:35:05
*/
public interface UserService extends IService<User> {
    User login(String phone, String password);
    User getLoginUser();

    public Page<User> pageUser(int currentPage, int pageSize, UserSearchVo param);
}
