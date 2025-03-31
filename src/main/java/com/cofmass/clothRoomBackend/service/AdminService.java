package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.utils.HttpRequestUtil;
import com.cofmass.clothRoomBackend.vo.AdminSearchVo;

/**
* @author 96307
* @description 针对表【admin(管理员表)】的数据库操作Service
* @createDate 2024-11-05 13:57:39
*/
public interface AdminService extends IService<Admin> {
    public Admin getLoginUser();
    public Page<Admin> pageAdmin(int currentPage, int pageSize, AdminSearchVo param);
}
