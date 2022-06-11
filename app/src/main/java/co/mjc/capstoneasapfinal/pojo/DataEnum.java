package co.mjc.capstoneasapfinal.pojo;

import java.util.Calendar;

import co.mjc.capstoneasapfinal.pojo.ScheduleEnum;


public class DataEnum {
    public static String dateCheck() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);
        String returnName;
        switch (dayOfWeek) {
            case 1:
                returnName = ScheduleEnum.SUNDAY.name();
                break;
            case 2:
                returnName = ScheduleEnum.MONDAY.name();
                break;
            case 3:
                returnName = ScheduleEnum.TUESDAY.name();
                break;
            case 4:
                returnName = ScheduleEnum.WEDNESDAY.name();
                break;
            case 5:
                returnName = ScheduleEnum.THURSDAY.name();
                break;
            case 6:
                returnName = ScheduleEnum.FRIDAY.name();
                break;
            case 7:
                returnName = ScheduleEnum.SATURDAY.name();
                break;
            default:
                returnName = "NULL";
                break;
        }
        return returnName;
    }
}
