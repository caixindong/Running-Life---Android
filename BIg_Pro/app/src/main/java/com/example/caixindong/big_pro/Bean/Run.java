package com.example.caixindong.big_pro.Bean;

/**
 * Created by caixindong on 16/6/8.
 */
public class Run {
    private int Id = 0;
    private Double distance = 0.0;// 路程：米
    private Double calories = 0.0;// 热量：卡路里
    private Double velocity = 0.0;// 速度：米每秒
    private long timer = 0;// 运动时间
    private int stepCount = 0;
    private String startTime = "";

    public Run() {

    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
