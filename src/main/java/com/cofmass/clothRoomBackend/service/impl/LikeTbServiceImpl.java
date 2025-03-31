package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.cofmass.clothRoomBackend.entity.LikeTb;
import com.cofmass.clothRoomBackend.service.LikeTbService;
import com.cofmass.clothRoomBackend.mapper.LikeTbMapper;
import org.springframework.stereotype.Service;

/**
* @author zlj
* @description 针对表【like_tb】的数据库操作Service实现
* @createDate 2024-09-19 16:36:32
*/
@Service
public class LikeTbServiceImpl extends ServiceImpl<LikeTbMapper, LikeTb>
    implements LikeTbService{
        @Override
        public Page<LikeTb> pageLike(int currentPage, int pageSize) {
    //        这里会出现报错，是关键字报错
            Page<LikeTb> page = new Page<>(currentPage, pageSize);
    //        关闭就不报错了
            page.setOptimizeCountSql(false);
            return this.page(page);
        }
}




