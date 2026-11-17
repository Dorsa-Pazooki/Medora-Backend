package org.dorsa.medorabackend.BLL.Domain;

import java.time.LocalTime;
import java.time.DayOfWeek;



public class Availability {
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek day;

    public  Availability (LocalTime startTime,LocalTime endTime,DayOfWeek day){

        if (!startTime.isBefore(endTime)){
            throw new IllegalArgumentException("start time must be before endtime");
        }

        if (startTime == null||endTime== null || day==null){
            throw new IllegalArgumentException("availability fields cannot be null");
        }

        startTime = startTime;
        endTime = endTime;
        day = day;


    }





}
