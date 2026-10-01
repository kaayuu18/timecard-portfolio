package com.example.timecard.repository;

// 従業員の保存・一覧取得・名前による検索をDBへ依頼する。

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long>{
   public Optional<Person> findByName(String name);
}


