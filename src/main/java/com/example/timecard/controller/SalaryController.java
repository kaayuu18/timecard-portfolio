package com.example.timecard.controller;

// 対象期間の勤怠を取得し、日給と勤務時間を合計して画面に渡す。

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class SalaryController {
         @Autowired
         KintaiDAOPersonImpl kintaiDao;
         @Autowired
         PersonDAOPersonImpl personDao;
//--------------------------------1ヶ月の合計勤怠時間や給料を表示 25日締め-------------------
@GetMapping("/admin/salaryinfo")
public String postMethodName(@ModelAttribute("data") Person person,Model model) {
    LocalDate date = LocalDate.now();
    person = personDao.findById(person.getId());
    List<Kintai> list = kintaiDao.findMonthKintai(person,date);
    int result = 0;
    // 小数の勤務時間を、整数に変換せず合計する。
    BigDecimal result2 = BigDecimal.ZERO;
    for(Kintai a: list){
         result += a.getTodayWage();
         if (a.getWorkingHours() != null) {
             result2 = result2.add(BigDecimal.valueOf(a.getWorkingHours()));
         }
    }
    model.addAttribute("person", person);
    model.addAttribute("personname", "給料管理:"+ person.getName());
    model.addAttribute("month",date.getYear() + "-" + date.getMonthValue());
    model.addAttribute("workingHours", "1ヶ月の勤務時間:" + result2.stripTrailingZeros().toPlainString() + "時間");
    model.addAttribute("houlywage", "時給:"+ person.getHourWage() + "円");
    model.addAttribute("monthWage", "1ヶ月の給料:" + result + "円");
    model.addAttribute("list", list);
    
    return "salaryinfo";
}
//--------------------------------月を選べるようにしてその月の合計勤怠時間や給料を表示 25日締め-----------
@PostMapping("/admin/salaryinfo")
public String postMethodName(@RequestParam("month") String month,@ModelAttribute Person person,Model model) {
    person = personDao.findById(person.getId());
    List<Kintai> list = kintaiDao.findMonthKintai(person,LocalDate.parse(month + "-01") );
    int result = 0;
    // 小数の勤務時間を、整数に変換せず合計する。
    BigDecimal result2 = BigDecimal.ZERO;
    for(Kintai a: list){
         result += a.getTodayWage();
         if (a.getWorkingHours() != null) {
             result2 = result2.add(BigDecimal.valueOf(a.getWorkingHours()));
         }
    }
    model.addAttribute("person", person);
    model.addAttribute("personname", "給料管理:"+ person.getName());
    model.addAttribute("month", month);
    model.addAttribute("workingHours", result2.stripTrailingZeros().toPlainString() + "時間");
    model.addAttribute("houlywage", "時給:"+ person.getHourWage() + "円");
    model.addAttribute("monthWage", "1ヶ月の給料:" + result + "円");
    model.addAttribute("list", list);
    return "salaryinfo";
}

         
         

}
