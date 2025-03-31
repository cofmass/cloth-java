package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Clothes;
import lombok.Data;

@Data
public class ClothesUserVo extends Clothes {
    private String userName;
    private String imgUrl;

    public ClothesUserVo() {
    }

    public ClothesUserVo(String userName, String imgUrl) {
        this.userName = userName;
        this.imgUrl = imgUrl;
    }
}
