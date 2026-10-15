// package com.example.kadai3_2app.entity;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import lombok.Data;

// @Entity
// @Table(name = "salary")
// @Data
// public class Salary {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long Id;
// @OneToOne
// @JoinColumn(name = "people_id")
// private Person person;
// @Column(name = "total_wage")
// private Long totalWage;
// @Column(name = "hourly_wage")
// private Long hourlyWage;


// public String toString(){
//          return "hello";
// }
// }
