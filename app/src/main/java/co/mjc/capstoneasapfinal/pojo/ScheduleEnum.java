package co.mjc.capstoneasapfinal.pojo;


import java.io.Serializable;

// 자바 캘린더에는 요일이 월(1)~일(7)로 되어있음
public enum ScheduleEnum implements Serializable {
    // 일치하게끔 Enum 설정
    SUNDAY(1),MONDAY(2), TUESDAY(3), WEDNESDAY(4),THURSDAY(5), FRIDAY(6), SATURDAY(7);

    private final int value;

    // Enum 에 필요한 값 생성자
    ScheduleEnum(int value) {
        this.value = value;
    }

    // 요일별 해당하는 값을 리턴
    public int intValue() {
        return value;
    }
}
