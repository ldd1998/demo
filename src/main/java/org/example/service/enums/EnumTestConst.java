package org.example.service.enums;
/**
 * 枚举类型作为常量集合
 * @author ldd
 */
public class EnumTestConst {
    public static void main(String[] args) {
        System.out.println(Weekday.MONDAY == Weekday.MONDAY);
    }
}
enum Weekday {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}