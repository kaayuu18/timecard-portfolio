package com.example.timecard.entity;

// kintaiテーブルの1件分の勤怠。従業員・日付・打刻時刻・日給を持つ。

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import com.example.timecard.validation.PersonV;
import com.example.timecard.validation.UniqueValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Entity
@Table(name = "kintai")//データベースにあるテーブル名とおなじなのでname=は省略
//@Data//LomBokの機能ゲッターセッターtoString イコールメソッドを省略可能
public class Kintai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "people_id")
    private Person person;
    
    @Column(name = "start_time")
    private LocalTime go;
    
    @Column(name = "end_time")
    private LocalTime out;
    
    @Column(name = "working_hours")
    private Double workingHours = 0.0;
    
    @Column(name = "break_start_time")
    private LocalTime breakStartTime;
    
    @Column(name = "break_end_time")
    private LocalTime breakEndTime;
    
    @Column(name = "break_time")
    private Integer breakTime = 0;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "today_wage")
    private Integer todayWage = 0;
    
    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalTime getGo() {
        return go;
    }

    public void setGo(LocalTime go) {
        this.go = go;
    }

    public LocalTime getOut() {
        return out;
    }

    public void setOut(LocalTime out) {
        this.out = out;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(LocalTime breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public LocalTime getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(LocalTime breakEndTime) {
        this.breakEndTime = breakEndTime;
    }

    public Integer getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Integer breakTime) {
        this.breakTime = breakTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTodayWage() {
        return todayWage;
    }

    public void setTodayWage(Integer todayWage) {
        this.todayWage = todayWage;
    }
    
    // 勤務時間を計算してセット
    public void calculateWorkingHours(LocalTime go, LocalTime out, Integer breakTime) {
        Duration workingTimeDuration = Duration.between(go, out);
        double workingMinutes = workingTimeDuration.toMinutes();
        workingMinutes -= breakTime;  // 休憩時間を引く
        this.workingHours = Math.floor((workingMinutes / 60) * 10) / 10;  // 小数点1位まで計算
        System.out.println(this.workingHours + "時間です");
    }
    
    // 休憩時間を計算してセット
    public void calculateBreakTime(LocalTime breakStart, LocalTime breakEnd) {
        Duration breakDuration = Duration.between(breakStart, breakEnd);
        this.breakTime = (int) breakDuration.toMinutes();  // 休憩時間（分単位）に変換
    }
    
    // 今日の給料を計算してセット
    public void calculateTodayWage(Double workingHours, Person person) {
        if(workingHours > 8){
            Double overWorkingHours = workingHours - 8;
            Double todayWage = 8 * person.getHourWage() + overWorkingHours.intValue() * person.getHourWage() *1.25 ;
            this.todayWage = todayWage.intValue();
        }
        else{
            Double wage = workingHours * person.getHourWage();
            this.todayWage = wage.intValue();  // 今日の給料を整数で計算
        }
    }
    
    // toStringメソッド（整形）
    @Override
    public String toString() {
        return "Kintai{id=" + id +
               ",従業員id=" + person.getId() +
               ", person=" + person.getName() +
               ", 出勤時間=" + go + 
               ", 退勤時間=" + out + 
               ", 勤務時間=" + workingHours +
               "時間, 休憩時間=" + breakTime +
               "分, 日付=" + date + 
               ", 今日の給料=" + todayWage + "円}";
    }
}


