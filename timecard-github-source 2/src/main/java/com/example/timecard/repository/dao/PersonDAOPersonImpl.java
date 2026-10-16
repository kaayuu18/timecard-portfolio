package com.example.timecard.repository.dao;

// 名前やIDなどの条件を指定して、従業員を検索する。

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class PersonDAOPersonImpl implements PersonDAO<Person>{
    private static final long serialVersionUID = 1L;
  @PersistenceContext
  EntityManager entityManager;//データベースアクセスするために必要

  public List<Person> findByString(String name)//名前の曖昧検索
  {
    List<Person> list; 
  Query query = entityManager.createQuery("FROM Person where name Like :name");
  query.setParameter("name", name);
  list = query.getResultList();
  return list;
  }
  public Person findById(long id){
    Person person = new Person();
    try{ 
      Query query = entityManager.createQuery("from Person where id =:id");
      query.setParameter("id", id);
      person =  (Person)query.getSingleResult();
      return person;
    }catch(NoResultException e){
      person = null;
      return person;
    }
  }
  //名前から検索
  public Person findByName(String name){
    Query query = entityManager.createQuery("from Person where name = :name");
    query.setParameter("name", name);
    return (Person)query.getSingleResult();
  }
 
  //名前かidを検索
  public List<Person> personFind(String str){
    Query query;
    List<Person> list;
    //strが文字列か数字(ID)か文字(名前)かを検証。
    try{
      //文字列がただけどidが入力されていたらString型のstrをintに変換
       int intId = Integer.parseInt(str);
       query = entityManager.createQuery("from Person where id = :id");
       query.setParameter("id",intId);
    }
    //名前だとそのまま名前を検索
    catch(NumberFormatException e){
      query = entityManager.createQuery("from Person where name Like :name");
      query.setParameter("name", "%" + str + "%");
  }
    list = query.getResultList();
    return list;
  }

}

