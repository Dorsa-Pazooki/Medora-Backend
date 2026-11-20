package org.dorsa.medorabackend.BLL.Domain;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Objects;



public class Availability {
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek day;

    public  Availability (LocalTime startTime,LocalTime endTime,DayOfWeek day){

        if (startTime == null||endTime== null || day==null){
            throw new IllegalArgumentException("availability fields cannot be null");
        }

        if (!startTime.isBefore(endTime)){
            throw new IllegalArgumentException("start time must be before endtime");
        }



        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;


    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public boolean overlapsWith(Availability other) {
        Objects.requireNonNull(other, "Other availability cannot be null");

        if (this.day != other.day) {
            return false;
        }

        return this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }
}
