package com.example.timecard.repository;

// 勤怠の保存・IDによる取得・削除をDBへ依頼する。

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;

@Repository
public interface KintaiRepository extends JpaRepository<Kintai,Long>{
          public boolean existsByPerson(Person person);
}

