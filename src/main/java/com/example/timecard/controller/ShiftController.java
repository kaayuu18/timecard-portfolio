package com.example.timecard.controller;

// シフト登録フォームを受け取り、シフトの追加・修正・削除を行う。




import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Shift;
import com.example.timecard.repository.ShiftRepository;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;
import com.example.timecard.repository.dao.ShiftDAOPersonImpl;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ShiftController {
    @Autowired
    ShiftRepository sRepository;
    @Autowired
    PersonDAOPersonImpl personDao;
    @Autowired
    KintaiDAOPersonImpl kintaiDao;
    @Autowired
    ShiftDAOPersonImpl shiftDao;
@GetMapping("/shiftedit")
public String getMethodName(Model model) {
    List<Shift> shifts = new ArrayList<>();
    model.addAttribute("shift", shifts);
    return "shiftedit";
}
@Transactional
@PostMapping("/shiftedit")
public String edit(Model model,@RequestParam("shiftlist") String list ) {
    System.out.println(list);
    System.out.println(list.length());
    String[] str = list.split(",");
    System.out.println("文字列は"+str.length);
    for(int i = 0; i < str.length; i = i + 4){
        Shift shift = new Shift();
        shift.setPerson(personDao.findById(Integer.parseInt(str[i])));
        shift.setDate(LocalDate.parse(str[i + 1]));
        shift.setGo(LocalTime.parse(str[i + 2]));
        shift.setOut(LocalTime.parse(str[i + 3]));
        sRepository.saveAndFlush(shift);
    }
return "redirect:/shiftedit";

}
@Transactional
@PostMapping("/admin/addshift")
public String postMethodName(@Validated @ModelAttribute  Shift shift, BindingResult result1 ,@Validated @ModelAttribute  Kintai kintai, BindingResult result2,Model model) {
    String link = String.format("?month=%d&day=%d",shift.getDate().getMonthValue(),shift.getDate().getDayOfMonth());
    //-------------------------------ダイアログからidを入力し忘れてないか検証
    if(!result1.hasErrors() || !result2.hasErrors()){ 
    sRepository.saveAndFlush(shift);
    return "redirect:/dayClick" + link;
    }
    else{
        System.out.println("見つかりません");
        List<Shift> list = shiftDao.findByDate(shift.getDate());
        model.addAttribute("str",shift.getDate());
        model.addAttribute("date",list);
        model.addAttribute("addclick","shiftedit");

        return "kanri";
    }
}


@PostMapping("/admin/editshift")
public String editPost(
    @RequestParam("date") String d,
    @RequestParam("id") String id,
    @RequestParam(name = "name",required = false) String name,
    @RequestParam(name = "go",required = false) String start_time,
    @RequestParam(name = "out",required = false) String end_time,
    @RequestParam(name = "breaktime" ,required = false,defaultValue = "0") String breakTime,Model model) {
        LocalDate date = LocalDate.parse(d);
        String link = String.format("?month=%d&day=%d",date.getMonthValue(),date.getDayOfMonth());
    System.out.println(name);
    Long shiftId = Long.parseLong(id);
    Shift shift = shiftDao.findById(shiftId);
    shift.setId(shiftId);
    shift.setPerson(personDao.findByName(name));
    if( start_time != ""){ 
    LocalTime go = LocalTime.parse(start_time);
    shift.setGo(go);}
    if(end_time != ""){ 
    LocalTime out = LocalTime.parse(end_time);
    shift.setOut(out);}
    if(breakTime != ""){
        shift.setBreakTime(Double.parseDouble(breakTime));
    }
    sRepository.saveAndFlush(shift);
    model.addAttribute("error", "hello");
    return "redirect:/dayClick" + link;
}

@PostMapping("/admin/deleteshift")
public String removePost(
    @RequestParam("date") String d,
    @RequestParam("id") String id,
    @RequestParam("name") String name,
    @RequestParam(name = "go",required = false) String start_time,
    @RequestParam(name = "out",required = false) String end_time,
    @RequestParam(name = "breaktime" ,required = false) String breakTime
    ) {
        LocalDate date = LocalDate.parse(d);
        String link = String.format("?month=%d&day=%d",date.getMonthValue(),date.getDayOfMonth());
        System.out.println(name);
        Long shiftId = Long.parseLong(id);
        sRepository.deleteById(shiftId);
        return "redirect:/dayClick" + link;
        
}

}



