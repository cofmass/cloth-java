package com.cofmass.clothRoomBackend.vo;

import lombok.Data;

@Data
public class ImageVo {
    private String imageId;
    private String imageUrl;

    private String localUrl;

    public ImageVo(String imageId, String imageUrl,String localUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.localUrl = localUrl;
    }
}
