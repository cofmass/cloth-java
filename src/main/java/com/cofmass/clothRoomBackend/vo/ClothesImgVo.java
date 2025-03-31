package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Image;

import java.util.List;

public class ClothesImgVo {
    private Integer clothesTypes;
    private List<Image> clothesImages;

    public ClothesImgVo(Integer clothesTypes, List<Image> clothesImages) {
        this.clothesTypes = clothesTypes;
        this.clothesImages = clothesImages;
    }

    public ClothesImgVo() {
    }

    public Integer getClothesTypes() {
        return clothesTypes;
    }

    public void setClothesTypes(Integer clothesTypes) {
        this.clothesTypes = clothesTypes;
    }

    public List<Image> getClothesImages() {
        return clothesImages;
    }

    public void setClothesImages(List<Image> clothesImages) {
        this.clothesImages = clothesImages;
    }

    @Override
    public String toString() {
        return "ClothesImgVo{" +
                "clothesTypes=" + clothesTypes +
                ", clothesImages=" + clothesImages +
                '}';
    }
}
