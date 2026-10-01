package com.example.timecard.entity;

// shiftテーブルの1件分の予定。従業員・勤務日・開始と終了の時刻を持つ。

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "shift")
@Data
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "people_id")
    @NotNull
    private Person person;
    @FutureOrPresent(message = "過去の日付はダメです")
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "start_time")
    private LocalTime go;
    @Column(name = "end_time")
    private LocalTime out;
    @Column(name = "break_time")
    private Double breakTime = 0.0;
//-----------------------------------------------------
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

public LocalDate getDate() {
    return date;
}

public void setDate(LocalDate date) {
    this.date = date;
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

public Double getBreakTime() {
    return breakTime;
}

public void setBreakTime(Double breakTime) {
    this.breakTime = breakTime;
}
    
    

}

