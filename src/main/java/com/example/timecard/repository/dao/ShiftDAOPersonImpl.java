package com.example.timecard.repository.dao;

// 従業員や日付・期間を指定して、シフトを検索する。

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Person;
import com.example.timecard.entity.Shift;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class ShiftDAOPersonImpl implements ShiftDAO<Shift>{
@PersistenceContext
EntityManager entityManager;
 
public List<Shift> findByDate(LocalDate date){
   
    Query query = entityManager.createQuery("From Shift where date = :date");
    query.setParameter("date", date);
    List<Shift> list = (List<Shift>)query.getResultList();
    return list;
}
public List<Shift> findShiftData(Person person){
   Query query = entityManager.createQuery("From Shift where person = :person");
   query.setParameter("person", person);
   List<Shift> list = (List<Shift>)query.getResultList();
   return list; 
} 
public Shift findById(Long id){
    Query query = entityManager.createQuery("From Shift where id = :id");
    query.setParameter("id", id);
    Shift shift = (Shift)query.getSingleResult();
    return shift;
}
public List<Shift> findMonthShift(Person person,LocalDate date){
    int year = date.getYear();
    int month = date.getMonthValue();
    LocalDate firstdate = date.withDayOfMonth(1);
 Query query = entityManager.createQuery(
    "From Shift s where s.person = :person and s.date between :startdate and :enddate");
 query.setParameter("person", person);
 query.setParameter("startdate", date.of(year,month,1));
 query.setParameter("enddate", date.of(year,month,date.lengthOfMonth()));
 List<Shift> list = (List<Shift>)query.getResultList();
 return list;
}
//---------------今日の出勤予定の人を検索-------------------
public List<Shift> findTodayShiftByDate(LocalDate today){
    Query query = entityManager.createQuery("From Shift where date = :today");
    query.setParameter("today", today);
    List<Shift> list = (List<Shift>)query.getResultList();
    return list;
}
}

