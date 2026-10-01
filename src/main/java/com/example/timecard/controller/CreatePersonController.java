package com.example.timecard.controller;

// 従業員登録画面を表示し、入力内容を確認してDBへ保存する。

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.timecard.CreatePerson;
import com.example.timecard.entity.Person;
import com.example.timecard.repository.PersonRepository;

@Controller
public class CreatePersonController {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    DataSource dataSource;
    @Autowired
    PasswordEncoder passwordEncoder;

        
    //従業員登録画面
@GetMapping("/personedit")
public String personEdit(@ModelAttribute("createPerson") CreatePerson createPerson,Model model) {
    model.addAttribute("createPerson", createPerson);
    return "personEdit";
}
@Transactional
@PostMapping("/personedit")
    public String personedit(@ModelAttribute("createPerson") @Validated CreatePerson createPerson,
                             BindingResult result, @RequestParam("role") String role, Model model) {

        if (!result.hasErrors()) {
            System.out.println(createPerson.getRole() + "です");

            // Personエンティティの作成
            Person person = new Person();
            person.setName(createPerson.getName());
            person.setAge(createPerson.getAge());
            person.setAddress(createPerson.getAddress());
            person.setMail(createPerson.getMail());
            person.setPhoneNum(createPerson.getPhoneNum());
            person.setPassword(passwordEncoder.encode(createPerson.getPassword()));
            //person.setPassword(createPerson.getPassword());
            person.setRole(role);
            person.setEnable(true);
            personRepository.saveAndFlush(person);
            System.out.println("リポジトリに保存されました");
            return "redirect:/admin/employee-list"; // 成功時に遷移するURL
        } else {
            System.out.println("エラーです");
            return "personEdit"; // エラーがあれば、同じページを表示
        }
    }
}
// @PostMapping("/personedit")
// public String personedit( @ModelAttribute("createPerson") @Validated CreatePerson createPerson,BindingResult result,@RequestParam("role") String role ,Model model ) {
    
//     if(!result.hasErrors()){
//         System.out.println(createPerson.getRole() + "です");
//         Person person = new Person();
//         person.setName(createPerson.getName());
//         person.setAge(createPerson.getAge());
//         person.setAddress(createPerson.getAddress());
//         person.setMail(createPerson.getMail());
//         person.setPhoneNum(createPerson.getPhoneNum());
//         UserDetails userDetails = User.withUsername(
//             createPerson.getName())
//             .password(PasswordEncoderFactories.createDelegatingPasswordEncoder()
//             .encode(createPerson.getPassword()))
//             .authorities(role)  
//             .build();
//             userDetailsManager.createUser(userDetails);
//         personRepository.saveAndFlush(person);
        
        
//         return "redirect:/all";

//     }
//     else{
//         System.out.println("エラーです");
//         return "personEdit";
//     }
    
// }

// }

