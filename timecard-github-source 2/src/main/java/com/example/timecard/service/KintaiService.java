package com.example.timecard.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import com.example.timecard.entity.Kintai;

@Service
public class KintaiService {

    /** 休憩を控除した勤務時間（画面表示用、時間単位で小数第2位まで）。 */
    public Kintai workingHours(Kintai kintai) {
        long minutes = workingMinutes(kintai);
        kintai.setWorkingHours(Math.floor(minutes * 100.0 / 60.0) / 100.0);
        return kintai;
    }

    public Kintai breakTime(Kintai kintai) {
        if (kintai.getBreakStartTime() == null || kintai.getBreakEndTime() == null) {
            throw new IllegalArgumentException("休憩の開始・終了が必要です");
        }
        long minutes = Duration.between(kintai.getBreakStartTime(), kintai.getBreakEndTime()).toMinutes();
        if (minutes < 0) {
            throw new IllegalArgumentException("日付をまたぐ休憩には未対応です");
        }
        kintai.setBreakTime(Math.toIntExact(minutes));
        return kintai;
    }

    /** 給与は表示用の丸めた勤務時間ではなく、実際の勤務「分」から計算する。 */
    public Kintai todayWage(Kintai kintai) {
        if (kintai.getPerson() == null || kintai.getPerson().getHourWage() == null) {
            throw new IllegalArgumentException("時給が設定されていません");
        }
        long minutes = workingMinutes(kintai);
        long regular = Math.min(minutes, 8 * 60L);
        long overtime = Math.max(0, minutes - 8 * 60L);
        int hourlyWage = kintai.getPerson().getHourWage();
        if (hourlyWage < 0) {
            throw new IllegalArgumentException("時給は0円以上にしてください");
        }
        // 本アプリでは8時間超を1.25倍で参考計算。給与制度のすべてには非対応。
        java.math.BigDecimal total = java.math.BigDecimal.valueOf(regular)
            .add(java.math.BigDecimal.valueOf(overtime).multiply(new java.math.BigDecimal("1.25")))
            .multiply(java.math.BigDecimal.valueOf(hourlyWage))
            .divide(java.math.BigDecimal.valueOf(60), 0, java.math.RoundingMode.FLOOR);
        kintai.setTodayWage(total.intValueExact());
        return kintai;
    }

    private long workingMinutes(Kintai kintai) {
        if (kintai.getGo() == null || kintai.getOut() == null) {
            throw new IllegalArgumentException("出勤・退勤時刻が必要です");
        }
        long elapsed = Duration.between(kintai.getGo(), kintai.getOut()).toMinutes();
        long breakMinutes = kintai.getBreakTime() == null ? 0 : kintai.getBreakTime();
        if (elapsed < 0 || breakMinutes < 0 || elapsed < breakMinutes) {
            throw new IllegalArgumentException("勤務時間か休憩時間が不正です（日付またぎは未対応）");
        }
        return elapsed - breakMinutes;
    }
}
