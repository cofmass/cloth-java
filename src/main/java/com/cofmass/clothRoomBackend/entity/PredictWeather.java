package com.cofmass.clothRoomBackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 城市天气预测表
 * @TableName predict_weather
 */
@TableName(value ="predict_weather")
@Data
public class PredictWeather implements Serializable {
    /**
     * 没什么含义的主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 城市预测天气外键
     */
    private String predictId;

    /**
     * 预测日期
     */
    @JsonFormat(pattern = "MM-dd")
    private LocalDate predictDate;

    /**
     * 白天天气
     */
    private String dayWeather;

    /**
     * 白天温度
     */
    private String dayTemp;

    /**
     * 夜晚天气
     */
    private String nightWeather;

    /**
     * 夜晚温度
     */
    private String nightTemp;

    /**
     * 白天风向
     */
    private String dayWind;

    /**
     * 白天风强
     */
    private String dayPower;

    /**
     * 夜晚风向
     */
    private String nightWind;

    /**
     * 夜晚风强
     */
    private String nightPower;

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
        PredictWeather other = (PredictWeather) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPredictId() == null ? other.getPredictId() == null : this.getPredictId().equals(other.getPredictId()))
            && (this.getPredictDate() == null ? other.getPredictDate() == null : this.getPredictDate().equals(other.getPredictDate()))
            && (this.getDayWeather() == null ? other.getDayWeather() == null : this.getDayWeather().equals(other.getDayWeather()))
            && (this.getDayTemp() == null ? other.getDayTemp() == null : this.getDayTemp().equals(other.getDayTemp()))
            && (this.getNightWeather() == null ? other.getNightWeather() == null : this.getNightWeather().equals(other.getNightWeather()))
            && (this.getNightTemp() == null ? other.getNightTemp() == null : this.getNightTemp().equals(other.getNightTemp()))
            && (this.getDayWind() == null ? other.getDayWind() == null : this.getDayWind().equals(other.getDayWind()))
            && (this.getDayPower() == null ? other.getDayPower() == null : this.getDayPower().equals(other.getDayPower()))
            && (this.getNightWind() == null ? other.getNightWind() == null : this.getNightWind().equals(other.getNightWind()))
            && (this.getNightPower() == null ? other.getNightPower() == null : this.getNightPower().equals(other.getNightPower()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPredictId() == null) ? 0 : getPredictId().hashCode());
        result = prime * result + ((getPredictDate() == null) ? 0 : getPredictDate().hashCode());
        result = prime * result + ((getDayWeather() == null) ? 0 : getDayWeather().hashCode());
        result = prime * result + ((getDayTemp() == null) ? 0 : getDayTemp().hashCode());
        result = prime * result + ((getNightWeather() == null) ? 0 : getNightWeather().hashCode());
        result = prime * result + ((getNightTemp() == null) ? 0 : getNightTemp().hashCode());
        result = prime * result + ((getDayWind() == null) ? 0 : getDayWind().hashCode());
        result = prime * result + ((getDayPower() == null) ? 0 : getDayPower().hashCode());
        result = prime * result + ((getNightWind() == null) ? 0 : getNightWind().hashCode());
        result = prime * result + ((getNightPower() == null) ? 0 : getNightPower().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", predictId=").append(predictId);
        sb.append(", predictDate=").append(predictDate);
        sb.append(", dayWeather=").append(dayWeather);
        sb.append(", dayTemp=").append(dayTemp);
        sb.append(", nightWeather=").append(nightWeather);
        sb.append(", nightTemp=").append(nightTemp);
        sb.append(", dayWind=").append(dayWind);
        sb.append(", dayPower=").append(dayPower);
        sb.append(", nightWind=").append(nightWind);
        sb.append(", nightPower=").append(nightPower);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}