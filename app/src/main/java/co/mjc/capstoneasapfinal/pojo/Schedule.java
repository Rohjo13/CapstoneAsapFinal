package co.mjc.capstoneasapfinal.pojo;

import java.io.Serializable;

public class Schedule implements Serializable {

    // 수업 이름
    private String lecName;

    // 무슨 요일인지? Day of the week?
    private ScheduleEnum dayOTW;

    public String getLecName() {
        return this.lecName;
    }

    public void setLecName(String lecName) {
        this.lecName = lecName;
    }

    public ScheduleEnum getDayOTW() {
        return dayOTW;
    }

    public void setDayOTW(ScheduleEnum dayOTW) {
        this.dayOTW = dayOTW;
    }

}
