package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Clothes;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.service.ClothesService;
import com.cofmass.clothRoomBackend.mapper.ClothesMapper;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.vo.AdminSearchVo;
import com.cofmass.clothRoomBackend.vo.ClothesSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zlj
* @description 针对表【clothes】的数据库操作Service实现
* @createDate 2024-09-13 16:18:03
*/
@Service
public class ClothesServiceImpl extends ServiceImpl<ClothesMapper, Clothes>
    implements ClothesService{

    @Autowired
    UserService userService;

    @Override
    public Page<Clothes> pageClothes(int currentPage, int pageSize, ClothesSearchVo param) {
        Page<Clothes> page = new Page<>(currentPage, pageSize);
        page.setOptimizeCountSql(false);
        if (param != null){
            List<User> list = userService.list(new LambdaQueryWrapper<User>()
                    .like(User::getUserName, param.getUserName())
            );
            List<Clothes> records = new ArrayList<>();
            Page<Clothes> pageEndClothes = page(page);
            for (User user :list) {
                Page<Clothes> pageClothes = page(page, new LambdaQueryWrapper<Clothes>()
                        .like(Clothes::getUserId, user.getUserId())
                        .like(Clothes::getClothesType,param.getClothesType())
                );
                records.addAll(pageClothes.getRecords());
            }
            pageEndClothes.setRecords(records);
            return pageEndClothes;
        }else {
            Page<Clothes> pageClothes = page(page);
            return pageClothes;
        }
    }
}




