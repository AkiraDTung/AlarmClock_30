package com.example.alarmclock30.entities;

public class Clock {
    private int id;
    private int hour;
    private int minute;
    private boolean repeated;
    private boolean enabled;


    public Clock(int id, int hour, int minute, boolean repeated, boolean enabled) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.repeated = repeated;
        this.enabled = enabled;
    }

    public Clock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

