package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Comments;
import lombok.Data;

@Data
public class CommentsVo extends Comments {
//    该用户是否点赞了
    private Boolean isLike = false;
//    该评论的点赞数量
    private Long likeNum = 0L;
//    点赞的样式
    private String likeType = "heart";
//    点赞的颜色
    private String likeColor = "#666666";
}
