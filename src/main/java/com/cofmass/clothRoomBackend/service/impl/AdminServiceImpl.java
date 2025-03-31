package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.service.AdminService;
import com.cofmass.clothRoomBackend.mapper.AdminMapper;
import com.cofmass.clothRoomBackend.utils.HttpRequestUtil;
import com.cofmass.clothRoomBackend.vo.AdminSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 96307
* @description 针对表【admin(管理员表)】的数据库操作Service实现
* @createDate 2024-11-05 13:57:39
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Admin getLoginUser() {
        String token = HttpRequestUtil.getRequest().getHeader("token");
        if (token!=null){
            try {
                Admin o = (Admin) redisTemplate.opsForValue().get(token);
                return o;
            } catch (NullPointerException n){
                return null;
            }
        }
        return null;
    }

    @Override
    public Page<Admin> pageAdmin(int currentPage, int pageSize, AdminSearchVo param) {
        Page<Admin> page = new Page<>(currentPage, pageSize);
        page.setOptimizeCountSql(false);
        if (param != null){
            Page<Admin> pageAdmin = page(page, new LambdaQueryWrapper<Admin>()
                .like(Admin::getAdminName,param.getUserName())
                .like(Admin::getPhone,param.getPhone())
                .like(Admin::getGender,param.getGender())
                .like(Admin::getAddress,param.getAddress())
                .like(Admin::getAdminId,param.getUserId())
            );
            return pageAdmin;
        }else {
            Page<Admin> pageAdmin = page(page);
            return pageAdmin;
        }
    }
}




