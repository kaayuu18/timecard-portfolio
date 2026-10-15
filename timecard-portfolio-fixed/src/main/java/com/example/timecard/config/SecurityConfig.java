package com.example.timecard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.timecard.service.PersonService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 打刻画面だけログインなしで利用する。
                .requestMatchers("/", "/timecard", "/login").permitAll()
                .requestMatchers("/js/**", "/css/**", "/img/**").permitAll()
                // 管理・編集操作は、URLが /admin で始まらない場合も管理者に限定する。
                .requestMatchers("/admin", "/admin/**", "/personedit", "/editpersonInfo",
                                 "/shiftedit", "/dayClick").hasRole("ADMIN")
                .requestMatchers("/employee/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    boolean admin = authentication.getAuthorities().stream()
                            .anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));
                    response.sendRedirect(request.getContextPath() + (admin ? "/admin" : "/employee/form"));
                })
                .failureUrl("/login?error")
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/login"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/timecard")
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }

    // PersonService は @Service で登録済み。二重のBean登録を避ける。
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PersonService personService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(personService).passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
