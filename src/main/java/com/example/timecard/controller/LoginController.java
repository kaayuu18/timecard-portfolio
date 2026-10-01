package com.example.timecard.controller;

// ログイン画面・管理者トップ画面など、画面への入口を用意する。

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
         @GetMapping("/")
         public String getMethodName() {
             return "redirect:/timecard";
         }
         @GetMapping("/admin")
         public String getAdmin() {
             return "admin";
         }
         
         
         @GetMapping("/login")
         public String getMethodName(@RequestParam(value="error",required = false) String error,Model model) {
         if(error != null){
                  model.addAttribute("str","ログインできませんでした");
         }
         else{
                  model.addAttribute("str","ログインフォーム");
         }
         return "login";
         }
         @GetMapping("/logout")
         public String logout(HttpServletRequest request, HttpServletResponse response) {
         
         // ログアウト処理が完了したらトップページにリダイレクトする
         return "redirect:/timecard";
         }
         

}
