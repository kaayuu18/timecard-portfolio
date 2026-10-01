package com.example.timecard.config;

import javax.sql.DataSource;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.timecard.repository.PersonRepository;
import com.example.timecard.service.PersonService;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    // パスワードエンコーダーの設定
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // PersonServiceの設定
    @Bean
    public PersonService personService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        return new PersonService(passwordEncoder, personRepository);
    }

    // Webセキュリティ設定
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/","/timecard", "/personedit").permitAll()  // 誰でも見れる
                .requestMatchers("/js/**", "/css/**", "/img/**").permitAll()  // 静的ファイル
                .requestMatchers("/employee/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者用
                .anyRequest().authenticated()  // その他は認証が必要
        )
        .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/admin",true)
                .failureUrl("/login?error")
        )
        .exceptionHandling(ex -> ex
                .accessDeniedPage("/login")  // 権限不足でリダイレクト
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/timecard")  // ログアウト後にtimecardに遷移
                .deleteCookies("JSESSIONID")  // Cookie削除
                .permitAll()
        );
        
        
        
        return http.build();
    }

    // 認証管理者の設定
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PersonService personService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);

        // PersonServiceを使って認証を設定
        authenticationManagerBuilder.userDetailsService(personService).passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}