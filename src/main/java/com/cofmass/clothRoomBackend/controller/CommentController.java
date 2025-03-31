package com.cofmass.clothRoomBackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.CommentLike;
import com.cofmass.clothRoomBackend.entity.Comments;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.service.CommentLikeService;
import com.cofmass.clothRoomBackend.service.CommentsService;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.utils.HttpRequestUtil;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.CommentSearchVo;
import com.cofmass.clothRoomBackend.vo.CommentsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.cofmass.clothRoomBackend.utils.HttpStatus.UPDATE_MY;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论管理")
public class CommentController {
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentLikeService commentLikeService;

    @ApiOperation("根据workId添加评论")
    @PostMapping("/add")
    public R add(@RequestBody Comments comments) {
        User loginUser = userService.getLoginUser();
        String commentsId = "CM"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        comments.setCommentId(commentsId);
        comments.setUserId(loginUser.getUserId());
        comments.setUserName(loginUser.getUserName());
        comments.setAvatarImg(loginUser.getAvatarUrl());
        commentsService.save(comments);
        return R.ok("添加成功");
    }

    @ApiOperation("根据workId显示评论")
    @GetMapping("/list/{workId}")
    public R list(@PathVariable String workId) {
//        需要返回点赞数量和该用户是否点赞
        List<CommentsVo> list = commentsService.listComments(workId);
        boolean haveComment = !list.isEmpty();
        return R.ok("显示成功").put("data", list).put("haveComment", haveComment);
    }

    @ApiOperation("点赞评论")
    @GetMapping("/like/{commentId}/{workId}")
    public R like(@PathVariable String commentId,@PathVariable String workId) {
        User loginUser = userService.getLoginUser();
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setLikerId(loginUser.getUserId());
        commentLike.setLikeId(workId);
        commentLikeService.save(commentLike);
        long commentsNum = commentLikeService.count(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId));
        return R.ok("点赞成功").put("data", commentsNum);
    }

    @ApiOperation("取消点赞评论")
    @GetMapping("/dislike/{commentId}/{workId}")
    public R dislike(@PathVariable String commentId,@PathVariable String workId) {
        User loginUser = userService.getLoginUser();
        commentLikeService.remove(new LambdaQueryWrapper<CommentLike>()
                .eq(CommentLike::getCommentId, commentId)
                .eq(CommentLike::getLikerId, loginUser.getUserId())
                .eq(CommentLike::getLikeId, workId)
        );
        long commentsNum = commentLikeService.count(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId));
        return R.ok("取消点赞成功").put("data", commentsNum);
    }

    @ApiOperation("评论分页")
    @PostMapping("/commentsPage")
    public R commentsPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "8") int pageSize, @RequestBody(required = false) CommentSearchVo param) {
        Page<Comments> commentsPage = commentsService.pageComments(currentPage, pageSize, param);
        List<Comments> records = commentsPage.getRecords();
        for (Comments c : records) {
            c.setAvatarImg(convertFileToBase64(c.getAvatarImg()));
        }
        commentsPage.setRecords(records);
        return R.ok("获取成功").put("data", commentsPage);
    }

    @ApiOperation("修改评论")
    @PutMapping("/update")
    public R updateById(@RequestBody Comments comments) {
        commentsService.updateById(comments);
        return R.ok("修改成功");
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delById")
    public R delById(@RequestParam String commentId) {
        commentsService.removeById(commentId);
        return R.ok("删除成功");
    }


}
