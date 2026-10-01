package com.example.timecard.entity;

// employeeテーブルの1人分の情報。ログイン用の名前・パスワード・権限も持つ。

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name ="employee")//MYSQLのテーブル名とおなじにする。@Cloumnはカラムとおなじにする。もし一緒やったら省略可能
//@Data
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "phone_num")
    private String phoneNum;
    
    @Column(name = "mail")
    private String mail;
    
    @Column(name = "address")
    private String address; //住所
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "hour_wage")
    private Integer hourWage = 1000;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "authority")
    private String authority;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "enable", nullable = false)
    private Boolean enable;
    
    @OneToMany(mappedBy = "person")
    private List<Kintai> kintai;
    
    @OneToMany(mappedBy = "person")
    private List<Shift> shift;

    // ゲッター、セッターの追加
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHourWage() {
        return hourWage;
    }

    public void setHourWage(Integer hourWage) {
        this.hourWage = hourWage;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<Kintai> getKintai() {
        return kintai;
    }

    public void setKintai(List<Kintai> kintai) {
        this.kintai = kintai;
    }

    public List<Shift> getShift() {
        return shift;
    }

    public void setShift(List<Shift> shift) {
        this.shift = shift;
    }
    public void setPassword(String password){
        this.password = password;
    }

    // UserDetailsのメソッド実装
    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable != null && enable;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // toStringメソッド
    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', phoneNum='" + phoneNum + "', mail='" + mail + "', address='" + address + "', age=" + age + ", hourWage=" + hourWage + ", role='" + role + "'}";
    }
}    

    



    







