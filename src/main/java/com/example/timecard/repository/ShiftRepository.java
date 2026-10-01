package com.example.timecard.repository;

// シフトの保存・IDによる取得・削除をDBへ依頼する。

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,Long>{
         

         
} 
