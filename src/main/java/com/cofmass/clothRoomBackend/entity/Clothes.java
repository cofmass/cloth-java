package com.cofmass.clothRoomBackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName clothes
 */
@TableName(value ="clothes")
@Data
public class Clothes implements Serializable {
    /**
     * 衣服唯一标识，自增主键
     */
    @TableId
    private String clothesId;

    /**
     * 用户ID，表示衣服属于哪个用户
     */
    private String userId;

    /**
     * 衣服类别，非空
     */
    private String clothesType;

    /**
     * 上传时间，非空，默认为当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 衣服标签，如颜色等
     */
    private String clothesLables;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Clothes other = (Clothes) that;
        return (this.getClothesId() == null ? other.getClothesId() == null : this.getClothesId().equals(other.getClothesId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getClothesType() == null ? other.getClothesType() == null : this.getClothesType().equals(other.getClothesType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getClothesLables() == null ? other.getClothesLables() == null : this.getClothesLables().equals(other.getClothesLables()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getClothesId() == null) ? 0 : getClothesId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getClothesType() == null) ? 0 : getClothesType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getClothesLables() == null) ? 0 : getClothesLables().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", clothesId=").append(clothesId);
        sb.append(", userId=").append(userId);
        sb.append(", clothesType=").append(clothesType);
        sb.append(", createTime=").append(createTime);
        sb.append(", clothesLables=").append(clothesLables);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}