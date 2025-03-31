package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Clothes;
import com.cofmass.clothRoomBackend.entity.Image;
import lombok.Data;

import java.util.List;

@Data
public class ClothesVo {
    private List<ImageVo> chooseImags;
    private String clothesType;

    public ClothesVo(String clothesType) {
        this.clothesType = clothesType;
    }

    public ClothesVo(List<ImageVo> chooseImags, String clothesType) {
        this.chooseImags = chooseImags;
        this.clothesType = clothesType;
    }

    public ClothesVo() {
    }
}
