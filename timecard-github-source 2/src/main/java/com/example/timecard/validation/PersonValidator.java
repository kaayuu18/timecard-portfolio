package com.example.timecard.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.timecard.entity.Kintai;
import com.example.timecard.repository.dao.KintaiDAOPersonImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonValidator implements ConstraintValidator<PersonV,Long> {
         @Autowired
         KintaiDAOPersonImpl dao;
         public void initialize(PersonV personV){}
         public boolean isValid( Long id, ConstraintValidatorContext context) {
                  if(id == null){
                           return true;
                  }
                  try{
                           Kintai kintai = dao.findById(id);
                  }
                  catch(NullPointerException e){
                           return false;
                  }
                  return true;

                  
         }
}
