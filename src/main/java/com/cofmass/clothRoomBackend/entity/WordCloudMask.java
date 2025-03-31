package com.cofmass.clothRoomBackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 词云蒙版以及词云图片关联表
 * @TableName word_cloud_mask
 */
@TableName(value ="word_cloud_mask")
@Data
public class WordCloudMask implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String userId;

    /**
     * 词云蒙版id
     */
    private String maskId;

    /**
     * 词云图id
     */
    private String wordCloudId;

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
        WordCloudMask other = (WordCloudMask) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMaskId() == null ? other.getMaskId() == null : this.getMaskId().equals(other.getMaskId()))
            && (this.getWordCloudId() == null ? other.getWordCloudId() == null : this.getWordCloudId().equals(other.getWordCloudId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMaskId() == null) ? 0 : getMaskId().hashCode());
        result = prime * result + ((getWordCloudId() == null) ? 0 : getWordCloudId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", maskId=").append(maskId);
        sb.append(", wordCloudId=").append(wordCloudId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}