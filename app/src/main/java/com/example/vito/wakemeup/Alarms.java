package com.example.vito.wakemeup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeanTindel on 19/10/2016.
 */
public class Alarms {
    ArrayList<Time> timeList;

    private static Alarms instance = null;

    private Alarms()
    {

    }

    public static Alarms getInstance()
    {
        if(instance == null)
        {
            instance = new Alarms();
        }

        return instance;
    }

    public void Create(int hour, int min)
    {
        Time T1 = new Time(hour,min);
        timeList.add(T1);
    }

    public ArrayList<Time> getAlarms()
    {
        return timeList;
    }

}
