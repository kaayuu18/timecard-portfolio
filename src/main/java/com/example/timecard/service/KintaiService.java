package com.example.timecard.service;

// 打刻した時刻から休憩時間・勤務時間・日給を計算する。

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;

@Service
public class KintaiService {
        //壱日の出勤時間を計算する
         public Kintai workingHours(Kintai kintai){
         Duration workingTimeDuration = Duration.between(kintai.getGo(), kintai.getOut());
         double workingHours = workingTimeDuration.toMinutes();
         workingHours = workingHours - kintai.getBreakTime();
         workingHours = Math.floor((workingHours / 60) * 100) / 100;
         System.err.println("働いた時間は" + workingHours + "です");
         kintai.setWorkingHours(workingHours);
         return kintai;

         }
         //休憩時間を設定
         public Kintai breakTime(Kintai kintai){
         Duration duration = Duration.between(kintai.getBreakStartTime(), kintai.getBreakEndTime());//第一引数と第二引数の差分を調べる
         // 休憩は分で保存する。勤務時間から引くときも分でそろえる。
         kintai.setBreakTime(Math.toIntExact(duration.toMinutes()));
         return kintai;

         }
         //壱日の給料を計算
         public Kintai todayWage(Kintai kintai){
            if(kintai.getWorkingHours() > 8){
                Double overWorkingHours = kintai.getWorkingHours() - 8;
                System.out.println(overWorkingHours);
                Double todayWage = 8 * kintai.getPerson().getHourWage() + overWorkingHours * kintai.getPerson().getHourWage() * 1.25;
                kintai.setTodayWage(todayWage.intValue());
            }
            else{
                Double d = kintai.getWorkingHours() * kintai.getPerson().getHourWage();
                Integer todayWage = d.intValue();
                kintai.setTodayWage(todayWage);
            }
            System.out.println(kintai);
          
          return kintai;
         
         // Double d = a * person.getHourWage();
         // Integer todayWage = d.intValue();
         // return todayWage;
         }
}