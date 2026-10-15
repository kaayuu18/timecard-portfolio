package com.example.timecard.validation;

// フォームで渡された従業員のIDがDBに存在するかを確認する。

import org.springframework.beans.factory.annotation.Autowired;

import com.example.timecard.entity.Person;
import com.example.timecard.repository.KintaiRepository;
import com.example.timecard.repository.PersonRepository;
import com.example.timecard.repository.dao.KintaiDAO;
import com.example.timecard.repository.dao.PersonDAOPersonImpl;

import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Person> {

    @Autowired
    PersonRepository personRepository;

@Override
public boolean isValid(Person value, ConstraintValidatorContext context) {
  if (value == null) {
      return false; // 値が空なら別のバリデーションに任せる
  }
  return personRepository.existsById(value.getId()); // DBに存在しない場合のみOK
}
}
