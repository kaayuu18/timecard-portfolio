package com.example.timecard.controller;

// 従業員情報の表示・更新・削除と、個人のシフト・勤怠確認を扱う。

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.entity.Shift;
import com.example.timecard.repository.KintaiRepository;
import com.example.timecard.repository.PersonRepository;
import com.example.timecard.repository.ShiftRepository;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;
import com.example.timecard.repository.dao.ShiftDAOPersonImpl;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;






@Controller
public class PersonInfoController{
         @Autowired
         KintaiDAOPersonImpl kintaiDAO;
         @Autowired
         PersonDAOPersonImpl personDAO;
         @Autowired
         PersonRepository personRepository;
         @Autowired
         KintaiRepository kintaiRepository;
         @Autowired
         ShiftDAOPersonImpl shiftDAO;
         @Autowired
         ShiftRepository shiftRepository;
@PostMapping("/admin/personinfo")
public String getMethodName(@RequestParam("name") String name,Model model) {
         Person person = personDAO.findById(Long.parseLong(name));
         model.addAttribute("data", person);
         // HTMLのファイル名 personInfo.html と大文字・小文字を合わせる。
         return "personInfo"; 
}
@Transactional
@PostMapping("/editpersonInfo")
public String postEdtiPersonInfo(@ModelAttribute Person data){
    Person person = personDAO.findById(data.getId());
    person.setName(data.getName());
    person.setAge(data.getAge());
    person.setPhoneNum(data.getPhoneNum());
    person.setMail(data.getMail());
    person.setAddress(data.getAddress());
    person.setHourWage(data.getHourWage());
    personRepository.saveAndFlush(person);
    System.out.println(data.getHourWage());
    System.out.println(person);
    return "redirect:/admin/employee-list";
}
@Transactional
@PostMapping("/admin/deleteperson")
public String postMethodName(@RequestParam("id") String id) {
     Person person = personDAO.findById(Long.parseLong(id));  
     List<Kintai> kintaiList = kintaiDAO.findKintaidata(person);
     List<Shift> shiftList = shiftDAO.findShiftData(person);
     shiftRepository.deleteAll(shiftList);
     kintaiRepository.deleteAll(kintaiList);  //たくさん消します
     personRepository.deleteById(Long.parseLong(id));
    return "redirect:/admin/employee-list";
}
@GetMapping("/employee/form")
public String getMethodName(Model model) {
    List<Person> list = personRepository.findAll();
    model.addAttribute("list", list);
    return "employee-list";
}
@PostMapping("/employee/form")
public String post(@RequestParam(name = "name") String name,@RequestParam("id") String id,Model model) {
    
    List<Person> list = personRepository.findAll();
    model.addAttribute("list", list);
    
    return "employee-list";
}

@GetMapping("/employee/shiftkintaiinfo")
public String getKintaiCheck(@ModelAttribute Person person,
@RequestParam(name = "name",required = false) String name,
@RequestParam(name="id",required = false) String id,Model model) {
    LocalDate now = LocalDate.now();
    if(name != null){
        person = personDAO.findByName(name);
    }
    if(id != null){
        person = personDAO.findById(Integer.parseInt(id));
    }
    if(person == null){
        List<Person> list = personRepository.findAll();
        model.addAttribute("list", list);
        model.addAttribute("error", "見つかりませんでした");
        return "employee-list";
    }
    System.out.println(person.getName() + person.getId() + "です");
    List<Kintai> kintaiList =kintaiDAO.findMonthKintai2(person, now);
    List<Shift> shiftList = shiftDAO.findMonthShift(person, now);
    model.addAttribute("kintailist", kintaiList);
    model.addAttribute("shiftlist", shiftList);
    model.addAttribute("person", person);
    
    return "shift_kintai_list";
}
@Transactional
@PostMapping("/employee/shiftkintaiinfo")
public String postKintaiCheck(@ModelAttribute Person person,
@RequestParam(name="id",required = false) String id,
@RequestParam(name = "month",required=false) String month,Model model) {
    if(month == ""){
        month = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue();
    }
    person = personDAO.findById(Integer.parseInt(id));
    List<Kintai> kintaiList =kintaiDAO.findMonthKintai2(person, LocalDate.parse(month + "-01"));
    List<Shift> shiftList = shiftDAO.findMonthShift(person, LocalDate.parse(month + "-01"));
    model.addAttribute("kintailist", kintaiList);
    model.addAttribute("shiftlist", shiftList);
    model.addAttribute("person", person);
    return "shift_kintai_list";
}



}