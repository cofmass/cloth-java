package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Comments;
import lombok.Data;

@Data
public class CommentsWorkVo extends Comments {
    private String indexImg;
    private Long likeNum;
    private String workTitle;

}
