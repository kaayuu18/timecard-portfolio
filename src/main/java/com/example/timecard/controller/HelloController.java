package com.example.timecard.controller;

// 従業員一覧・検索と、カレンダーの日付ごとの管理画面を表示する。

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.repository.KintaiRepository;
import com.example.timecard.repository.PersonRepository;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;
import com.example.timecard.repository.dao.ShiftDAOPersonImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.entity.Shift;

import org.springframework.web.bind.annotation.PostMapping;




@Controller

public class HelloController{

@Autowired
PersonRepository repository;
@Autowired
KintaiRepository kintaiRepository;
@Autowired
PersonDAOPersonImpl dao;
@Autowired
KintaiDAOPersonImpl kintaiDao;
@Autowired 
ShiftDAOPersonImpl shiftdao;



//一覧ページを表示
@GetMapping("/admin/employee-list")
public String all(@ModelAttribute Person person,Model model,HttpServletRequest request) {
    List<Person> list = repository.findAll();
    model.addAttribute("data",list);
    return "all";
}

 //一覧ページから名前を検索findページに遷移
 @Transactional
 @PostMapping("/admin/employee-list")
public String serach(@ModelAttribute Person person,Model model,HttpServletRequest request) {
    String param = request.getParameter("name");
    List<Person> list = dao.personFind(param);
    model.addAttribute("data", list);
    return "find";
}




//要修正30日までしか表示されないnowのところを変更(11/18)修正済み
// @GetMapping("/calendar")
// public String ca(Model model,@RequestParam(value = "month",required=false)  String date) {
//     LocalDate now = LocalDate.now();
//     System.out.println("今日は" + now.getMonthValue() +"月"+now.getDayOfMonth() +"日");
//     if(date == null){
//         //intをString型に変換
//         date = String.valueOf(now.getMonthValue());
//     }
//     List<Integer> list = new ArrayList<>();
//     int count = 0;
//     for(int i = 0; i <= 7; i++){
//         if(now.withMonth(Integer.parseInt(date)).withDayOfMonth(1).getDayOfWeek().getValue() == count){
//             break;}
//         count++;
//     }
//     for(int i = 0; i < count; i++){
//         list.add(i,0);
//     }
//     for(int i = 1; i <= now.withMonth(Integer.parseInt(date)).lengthOfMonth(); i++){
//         list.add(i);
//         System.out.print(list.get(i) + ":");
//     }
//     System.out.println("count と list.size=" + list.size() + ":"  +  count);
//     for(int i = 1; i < list.size() + 2; i++){
//         try{ 
//         if(list.get(i -1)==0){
//             continue;
//         }
//         model.addAttribute("month",now.withMonth(Integer.parseInt(date)).getMonthValue());//月を取得
//         model.addAttribute(String.format("day%d",i), list.get(i - 1));
//         }
//         catch(IndexOutOfBoundsException e){

//         }
        
//     }
//     return "calendar";
    
// }
@GetMapping("/admin/calendar")
public String ca(Model model,@RequestParam(value = "month",required=false)  YearMonth date) {
    LocalDate now = LocalDate.now();
    List<Integer> list = new ArrayList<>();
    if(date == null){
        //intをString型に変換
        date = YearMonth.now();
    }
    DayOfWeek week = date.atDay(1).getDayOfWeek();
    System.out.println("カレンダーのげと" + date.atEndOfMonth().getDayOfMonth() + ":" + week.getValue());
    int i;
    for( i = 0; i <= week.getValue(); i++){
        
        list.add(0);
    }
    for( i = 1; i <= date.atEndOfMonth().getDayOfMonth(); i++){
        list.add(i);
    }
    System.out.println(week.getValue() + "カレンダーの月の曜日です");
    for( i = 0; i <= list.size() -1; i++){
        if(list.get(i) == 0){
            continue;
        }
        model.addAttribute(String.format("day%d", i),list.get(i));
    }
   
    model.addAttribute("month",date.getMonthValue());//月を取得
    
     
    return "calendar";
    
}


//calendarから日付を押すとpost(/dayClick)がよびだされる。
//勤怠記録の管理
@GetMapping("/dayClick")
public String postMethodName(@RequestParam(name = "day",required = false) String day,@RequestParam("month") String month,@ModelAttribute Kintai kintai,@ModelAttribute Shift shift,Model model){
    if(day == ""){
       return "redirect:/admin/calendar";
    }
     LocalDate date = LocalDate.of(LocalDate.now().getYear(),Integer.parseInt(month) , Integer.parseInt(day));
     //今日または今日より前を日付をクリックした場合は勤怠記録が呼び出される(修正するかも)
     if(date.isBefore(LocalDate.now()) || date.equals(LocalDate.now()))
        { System.out.println("hello");
        List<Kintai> list = kintaiDao.findByDate(date);
        System.out.println("これからPostMaippingのListを表示されるよ" + list.size());
        model.addAttribute("str",date);
        model.addAttribute("date",list);
        model.addAttribute("addclick","kintaiedit");
        model.addAttribute("edit", "edit");
        model.addAttribute("remove", "remove");

        }
        //今日以降の日付をクリックした場合はシフト予定が表示されるisAfterの戻り値はboolean
    else if(date.isAfter(LocalDate.now())){
        List<Shift> list = shiftdao.findByDate(date);
        model.addAttribute("str",date);
        model.addAttribute("date",list);
        model.addAttribute("addclick","shiftedit");
        model.addAttribute("edit", "editshift");
        model.addAttribute("remove", "removeshift");
        
    }
    //kanri.htmlが呼び出される
    return "kanri";
}





}


