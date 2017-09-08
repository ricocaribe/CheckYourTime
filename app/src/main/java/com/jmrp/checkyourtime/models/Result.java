package com.jmrp.checkyourtime.models;

public class Result {

    private long totalTime;
    private double percent;

    public Result(long totalTime, double percent) {
        this.totalTime = totalTime;
        this.percent = percent;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getPercent() {
        return percent;
    }
}
