package com.example.timecard.service;

// ログイン時に名前から従業員を探し、認証に必要な情報を返す。

import java.security.InvalidAlgorithmParameterException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.timecard.entity.Person;
import com.example.timecard.repository.PersonRepository;


@Service  //ビジネスロジックをBEan化
@Transactional  // データベース操作をトランザクション（ひとまとまり）で管理します
public class PersonService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;  // パスワードを暗号化するため
    private final PersonRepository personRepository;  // ユーザー情報を保存しているデータベース

    // コンストラクタインジェクション（必要なものを外から受け取る）
    public PersonService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;  // 受け取ったパスワードを暗号化
        this.personRepository = personRepository;  // データベースを使います
    }

    // ユーザー名でユーザーを探して、その情報を返す
@Transactional
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // ユーザーが見つからなかった場合はエラーを出す
    return personRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
}

    // パスワードが正しいかをチェックする
    public boolean checkPassword(String inputPassword, String datapass) {
        return passwordEncoder.matches(inputPassword, datapass);  // 入力されたパスワードとデータベースのパスワードを比べる
    }
    public Collection<GrantedAuthority> getAuthorities(Person person){
        if(person.getRole() == "ADMIN"){ 
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        }
        else{
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }
    }
}