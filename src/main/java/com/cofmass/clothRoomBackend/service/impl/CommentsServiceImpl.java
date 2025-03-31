package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.CommentLike;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.mapper.CommentLikeMapper;
import com.cofmass.clothRoomBackend.service.CommentLikeService;
import com.cofmass.clothRoomBackend.service.CommentsService;
import com.cofmass.clothRoomBackend.mapper.CommentsMapper;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.vo.CommentSearchVo;
import com.cofmass.clothRoomBackend.vo.CommentsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author zlj
* @description 针对表【comments】的数据库操作Service实现
* @createDate 2024-09-13 16:18:12
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private CommentLikeMapper commentLikeMapper;
    @Autowired
    private UserService userService;

    @Override
    public List<CommentsVo> listComments(String workId) {
        User loginUser = userService.getLoginUser();
//        该作品的所有评论
        List<Comments> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getWorkId, workId));
        List<CommentsVo> commentsVos = new ArrayList<>();
        for (Comments cm :comments) {
            CommentsVo c = new CommentsVo();
            BeanUtils.copyProperties(cm, c);

//            在所有评论里面根据各个ID查看是否有点赞该评论
            CommentLike cl = commentLikeMapper.selectOne(new LambdaQueryWrapper<CommentLike>()
                    .eq(CommentLike::getLikeId, workId)
                    .eq(CommentLike::getLikerId, loginUser.getUserId())
                    .eq(CommentLike::getCommentId, c.getCommentId()));
//            如果这个作品的这个评论当前用户有记录，说明有点赞
            if (cl != null) {
//                设置为点赞
               c.setIsLike(true);
               c.setLikeColor("#e5404f");
               c.setLikeType("heart-filled");
            }
//            每个都要设置点赞数量
             Long likeNum = commentLikeMapper.selectCount(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, c.getCommentId()));
             c.setLikeNum(likeNum);
             commentsVos.add(c);
        }
        return commentsVos;
    }

    @Override
    public  Page<Comments> pageComments(int currentPage, int pageSize, CommentSearchVo param) {
//        这里会出现报错，是关键字报错
        Page<Comments> page = new Page<>(currentPage, pageSize);
//        关闭就不报错了
        page.setOptimizeCountSql(false);
        if (param != null){
            Page<Comments> pageComment = page(page, new LambdaQueryWrapper<Comments>()
                    .like(Comments::getCommentContent, param.getComment())
                    .like(Comments::getWorkId,param.getWorkId())
                    .like(Comments::getUserName, param.getUserName())
            );
            return pageComment;
        }else {
            Page<Comments> pageComment = page(page);
            return pageComment;
        }
    }

}




