package com.example.vito.wakemeup;

/**
 * Created by vito on 26/10/16.
 */

public class SleepTimes {

    private int id;
    private String week;
    private String time1;
    private String time2;

    public SleepTimes(){}

    public SleepTimes(String week, String time1, String time2){
        this.week = week;
        this.time1 = time1;
        this.time2 = time2;
    }

    //GET & SET ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    //GET & SET week
    public String getWeek() {
        return week;
    }
    public void setWeek(String week) {
        this.week = week;
    }

    //GET & SET time1
    public String getTime1() {
        return time1;
    }
    public void setTime1(String time1) {
        this.time1 = time1;
    }

    //GET & SET time2
    public String getTime2() {
        return time2;
    }
    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String toString() {
        return "ID : " + id + "\n WEEK : " + week + "\n Heure couché : " + time1 + "\n Heure levé : " + time2;
    }
}
