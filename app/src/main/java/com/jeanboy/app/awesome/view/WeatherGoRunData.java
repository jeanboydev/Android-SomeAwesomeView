package com.jeanboy.app.awesome.view;

/**
 * Created by jeanboy on 2016/11/23.
 */

public class WeatherGoRunData {

    private String temperatureTip;
    private String hour;
    private int level;

    public WeatherGoRunData() {
    }

    public WeatherGoRunData(String temperatureTip, String hour, int level) {
        this.temperatureTip = temperatureTip;
        this.hour = hour;
        this.level = level;
    }

    public String getTemperatureTip() {
        return temperatureTip;
    }

    public void setTemperatureTip(String temperatureTip) {
        this.temperatureTip = temperatureTip;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
