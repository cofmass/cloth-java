package com.cofmass.clothRoomBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Clothes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cofmass.clothRoomBackend.vo.AdminSearchVo;
import com.cofmass.clothRoomBackend.vo.ClothesSearchVo;

/**
* @author zlj
* @description 针对表【clothes】的数据库操作Service
* @createDate 2024-09-13 16:18:03
*/
public interface ClothesService extends IService<Clothes> {
   public Page<Clothes> pageClothes(int currentPage, int pageSize, ClothesSearchVo param);
}
