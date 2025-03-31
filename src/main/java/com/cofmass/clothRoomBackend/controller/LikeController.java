package com.cofmass.clothRoomBackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.cofmass.clothRoomBackend.entity.LikeTb;
import com.cofmass.clothRoomBackend.service.CommentsService;
import com.cofmass.clothRoomBackend.service.LikeTbService;
import com.cofmass.clothRoomBackend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@RestController
@RequestMapping("/like")
@Api(tags = "点赞管理")
public class LikeController {

    @Autowired
    private LikeTbService likeTbService;

    @ApiOperation("点赞分页")
    @GetMapping("/likePage")
    public R commentsPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "15") int pageSize) {
        Page<LikeTb> likePage = likeTbService.pageLike(currentPage, pageSize);
        List<LikeTb> records = likePage.getRecords();
        return R.ok("获取成功").put("data", records);
    }

}
