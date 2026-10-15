package com.example.timecard;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.service.KintaiService;

import static org.junit.jupiter.api.Assertions.*;

class KintaiServiceTests {
    private final KintaiService service = new KintaiService();

    @Test
    void minuteAccurateWageNotBasedOnTruncatedDisplay() {
        Person person = new Person();
        person.setHourWage(1200);
        Kintai record = new Kintai();
        record.setPerson(person);
        record.setGo(LocalTime.of(9, 0));
        record.setOut(LocalTime.of(10, 1));
        record.setBreakTime(0);
        service.workingHours(record);
        service.todayWage(record);
        assertEquals(1.01, record.getWorkingHours(), 0.001);
        assertEquals(1220, record.getTodayWage());
    }

    @Test
    void overtimeIsCalculatedInMinutes() {
        Person person = new Person();
        person.setHourWage(1200);
        Kintai record = new Kintai();
        record.setPerson(person);
        record.setGo(LocalTime.of(9, 0));
        record.setOut(LocalTime.of(18, 30));
        record.setBreakTime(60);
        service.todayWage(record);
        assertEquals(10350, record.getTodayWage());
    }

    @Test
    void invalidBreakTimeIsRejected() {
        Kintai record = new Kintai();
        record.setGo(LocalTime.of(9, 0));
        record.setOut(LocalTime.of(9, 10));
        record.setBreakTime(20);
        assertThrows(IllegalArgumentException.class, () -> service.workingHours(record));
    }
}
