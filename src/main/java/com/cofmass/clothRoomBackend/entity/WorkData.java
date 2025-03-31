package com.cofmass.clothRoomBackend.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WorkData {
     /**
     * 封面图片
     */
    private String indexImg;
    /**
     * 作者头像
     */
    private String avatarImg;
    /**
     * 作者名称
     */
    private String userName;
     /**
     * 作品标题，非空
     */
    private String workTitle;

    /**
     * 作品文字描述，非空
     */
    private String workContent;
    /**
     * 发布时间，非空，默认为当前时间
     */
    private Date createTime;
}
