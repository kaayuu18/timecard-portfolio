package com.example.timecard.controller;

// 打刻ボタンを受け取り、勤怠を保存する。管理画面からの追加・修正・削除も扱う。

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.entity.Shift;
import com.example.timecard.repository.KintaiRepository;
import com.example.timecard.repository.PersonRepository;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;
import com.example.timecard.repository.dao.ShiftDAOPersonImpl;
import com.example.timecard.service.KintaiService;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

import jakarta.transaction.Transactional;

@Controller
public class KintaiController {
@Autowired
PersonRepository pRepository;
@Autowired
KintaiDAOPersonImpl kintaiDao;
@Autowired
ShiftDAOPersonImpl shiftDao;
@Autowired
PersonDAOPersonImpl personDao;
@Autowired
KintaiRepository repository;
@Autowired
KintaiService kintaiService;


//----------------------------------1番最初の画面(打刻画面)----------------------------------
@GetMapping("/timecard")
public String index(@ModelAttribute Kintai kintai,Model model) {
    LocalDate date = LocalDate.now();//現在の年月日を取得
    List<Kintai> list1 = kintaiDao.todayList(date);//今日の出勤者を検索、Listに入れる
    //List<Shift> shiftlist = shiftDao.findTodayShiftByDate(LocalDate.now());
    model.addAttribute("kintaitoday",list1 );//今日の出勤者がはいったリストをモデルに果たしHTMLに表示
   // model.addAttribute("todayShift", shiftlist);
    
    return "index";//index.html
}
//--------------------------出勤ボタンや退勤ボタンを押した時の押した時に実行されるハンドラメソッド
@Transactional
@PostMapping("/timecard")
public String postMethodName(//引数にはフォームから送られてきたID,String　actionには出勤か退勤ボタンの文字？が入っている出勤だとstr=go 退勤だとstr =out
@RequestParam(name ="action",required = false) String action,
@Validated @ModelAttribute Kintai kintai,BindingResult result,Model model) {
   
    LocalTime now = LocalTime.now();//年/月/日/時間/を取得
    LocalDate date = LocalDate.now();//現在の年月日を取得
    List<Kintai> list1 = kintaiDao.todayList(date);//今日の出勤者を検索、Listに入れる
    kintai.setDate(LocalDate.now());//今日の日付をセット
    
    if(result.hasErrors() || kintai.getPerson() == null){
        System.out.println("エラーが発見");
        if(kintai.getPerson() == null){ 
        model.addAttribute("personerror","存在しません");
        }
        model.addAttribute("kintaitoday",list1 );//今日の出勤者がはいったリストをモデルに果たしHTMLに表示
        return "index";
    }
    
    if(action.equals("go"))//出勤ボタンが押された場合
    {   //本日すでに出勤済みか確認するfalseだとまだ本日出勤していない。elseだと本日出勤済み
        if(kintaiDao.findCheckGoKintaiId(kintai.getPerson(), LocalDate.now()) == false)
        { 
        System.out.println(kintai.getPerson().getName()+ "さんが出勤しました" + kintai.getBreakTime());
        kintai.setDate(LocalDate.now());//日付を入れる
        kintai.setGo(now);//出勤時間ををセットする
        repository.saveAndFlush(kintai);//kintaiデータを保存
        }
        else{
            List<Kintai> kintailist = kintaiDao.todayList(date);//今日の出勤者を検索、Listに入れる
            model.addAttribute("kintaitoday",kintailist );//今日の出勤者がはいったリストをモデルに果たしHTMLに表示
            model.addAttribute("kintaierror", "本日出勤済みです");
            return "index";
        }
        
    }
    else if(action.equals("breakstart")){
        kintai = kintaiDao.findTodayKintaiById(kintai.getPerson());
        if(kintai.getGo() == null && kintai.getOut() == null){
            model.addAttribute("kintaierror", "出勤情報がありません");
            model.addAttribute("kintaitoday",list1 );
            return "index";
        }
        kintai.setBreakStartTime(now);
        repository.saveAndFlush(kintai);
    }
    else if(action.equals("breakend")){
        kintai = kintaiDao.findTodayKintaiById(kintai.getPerson());
        if(//-------------------正常時-----------------------------
            kintai.getOut() == null && kintai.getBreakStartTime() != null && kintai.getGo() != null){
            kintai.setBreakEndTime(now);
            kintai = kintaiService.breakTime(kintai);
            repository.saveAndFlush(kintai);
            }
        else{
            //--------------------エラー表示------------------------------------------
            model.addAttribute("kintaierror", "出勤情報がありません");
            model.addAttribute("kintaitoday",list1 );
            return "index";
        }
    }
    else if(action.equals("out")){
        {   
        kintai = kintaiDao.findTodayKintaiById(kintai.getPerson());
        if(kintai.getGo() == null){
            System.out.println("ありませんでした");
            model.addAttribute("kintaierror", "出勤が押されてません");
            model.addAttribute("kintaitoday",list1 );//今日の出勤者がはいったリストをモデルに果たしHTMLに表示
            return "index";
        }
        if(kintai.getBreakStartTime() != null && kintai.getBreakEndTime() == null){
            model.addAttribute("kintaierror", "休憩戻りが押されてません");
            model.addAttribute("kintaitoday",list1 );//今日の出勤者がはいったリストをモデルに果たしHTMLに表示
            return "index";
        }
       
        kintai.setOut(now);//退勤時間をセットする
        
        kintai = kintaiService.workingHours(kintai);//時間単位単位
        kintai = kintaiService.todayWage(kintai);
        
        System.out.println(kintai.getWorkingHours());
        System.out.println(kintai);
        System.out.println(kintai.getPerson());
        repository.saveAndFlush(kintai);//保存
        
        
    }
}
    return "redirect:/timecard";//打刻画面にリダイレクト
    

}
//------------------------------勤怠記録を追加する用のハンドラメソッド(打刻忘れしたした時)-------------------------
@Transactional
@PostMapping("/admin/addkintai")
public String postMethodName(
    @Validated @ModelAttribute  Kintai kintai, BindingResult result1,//なぜかシフトも勤怠とシフトの両方のvalidationを追加する
    @Validated @ModelAttribute Shift shift,BindingResult result2
    ,Model model) {
        //urlに書くため用のlinkを作成
    String link = String.format("?month=%d&day=%d",kintai.getDate().getMonthValue(),kintai.getDate().getDayOfMonth());
    if(!result1.hasErrors() || !result2.hasErrors()){
        if(kintai.getGo() != null && kintai.getOut() != null){
            kintai = kintaiService.workingHours(kintai);
            kintai = kintaiService.todayWage(kintai);
            System.out.println(kintai);
        }
        repository.saveAndFlush(kintai);
        return "redirect:/dayClick" + link;
    }
        List<Kintai> list = kintaiDao.findByDate(kintai.getDate());
        model.addAttribute("str",kintai.getDate());
        model.addAttribute("date",list);
        model.addAttribute("addclick","kintaiedit");
        return "kanri";
    
    
    
}

//-----------------勤怠記録の修正(力技)------------------------//
@PostMapping("/admin/edit")
public String editPost(
    @RequestParam("date") String d,
    @RequestParam("id") String id,
    @RequestParam(name = "name",required = false) String name,
    @RequestParam(name = "go",required = false) String start_time,
    @RequestParam(name = "out",required = false) String end_time,
    @RequestParam(name = "breaktime" ,required = false) String breakTime,Model model) {
        System.out.println(start_time + end_time + breakTime);
        LocalDate date = LocalDate.parse(d);
        String link = String.format("?month=%d&day=%d",date.getMonthValue(),date.getDayOfMonth());
        Long kintaiId = Long.parseLong(id);
        Kintai kintai = kintaiDao.findById(kintaiId);
        kintai.setId(kintaiId);
        kintai.setPerson(personDao.findByName(name));
        if(start_time != ""){
            LocalTime go = LocalTime.parse(start_time);
            kintai.setGo(go);
        }
        if(breakTime != ""){
            kintai.setBreakTime(Integer.parseInt(breakTime));
        }
        if(end_time != ""){ 
        LocalTime out = LocalTime.parse(end_time);
        kintai.setOut(out);
        kintai = kintaiService.workingHours(kintai);
        kintai = kintaiService.todayWage(kintai);
        System.out.println(kintai);
       }
    repository.saveAndFlush(kintai);
    return "redirect:/dayClick" + link;
}
//---------------------------------勤怠記録の削除(力技)-------------------------
@PostMapping("/admin/delete")
public String removePost(
    @RequestParam("date") String d,
    @RequestParam("id") String id,
    @RequestParam(name = "name",required = false) String name,
    @RequestParam(name = "go",required = false) String start_time,
    @RequestParam(name = "out",required = false) String end_time
    ) {
        LocalDate date = LocalDate.parse(d);
        String link = String.format("?month=%d&day=%d",date.getMonthValue(),date.getDayOfMonth());
        System.out.println(name);
        Long kintaiId = Long.parseLong(id);
        repository.deleteById(kintaiId);
        return "redirect:/dayClick" + link;
        
}
//--------------------------------1ヶ月の合計勤怠時間や給料を表示 25日締め-------------------
// @GetMapping("/salaryinfo")
// public String postMethodName(@ModelAttribute("data") Person person,Model model) {
//     List<Kintai> list = kintaiDao.findMonthKintai(person,LocalDate.now());
//     model.addAttribute("person", person);
//     model.addAttribute("personname", "給料管理："+person.getName());
//     model.addAttribute("list", list);
    
//     return "salaryinfo";
// }
//--------------------------------月を選べるようにしてその月の合計勤怠時間や給料を表示 25日締め-----------
// @PostMapping("/salaryinfo")
// public String postMethodName(@RequestParam("month") String month,@ModelAttribute Person person,Model model) {
//     System.out.println("person.getname" + person.getName() +  ":" + person.getId());
//     System.out.println(month);
//     List<Kintai> list = kintaiDao.findMonthKintai(person,LocalDate.parse(month + "-01") );
//     model.addAttribute("person", person);
//     model.addAttribute("personname", "給料管理："+person.getName());
//     model.addAttribute("list", list);
//     return "salaryinfo";
// }
}


