package com.example.timecard.repository.dao;

// 今日の打刻や指定期間の勤怠など、条件を指定した検索をまとめる。

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Person;
import com.example.timecard.entity.Shift;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class KintaiDAOPersonImpl implements KintaiDAO<Kintai>{
    private static final long serialVersionUID = 1L;
    //JDBCTEMPLATE(javaに直接SQL文)を書くためのDIを注入(注意これはJPAの管理下ではない)
    @Autowired
    JdbcTemplate jdbcTemplate;
    //多分JPQL用
    @PersistenceContext
    EntityManager entityManager;
    
    //引数idから検索
    public Kintai findById(long id){
        Kintai kintai = new Kintai();
        Query query = entityManager.createQuery("FROM Kintai WHERE id = :id");
        query.setParameter("id", id);
         kintai = (Kintai)query.getSingleResult();
        return kintai;
    }
    //一人の従業員の勤怠記録を取得//
    public List<Kintai> findKintaidata(Person person){
        Query query = entityManager.createQuery("from Kintai k where k.person = :person ");
        query.setParameter("person", person);
        List<Kintai> list = query.getResultList();
        return list;
    }
    
    //退勤がおされていないかつ出勤が押されているpeople_idを検索
    //これをしないと出勤をレコードに退勤が表示されない
    public Kintai findKintaiById(Person person){
        Kintai kintai = new Kintai();
        try{ 
        Query query = entityManager.createQuery("FROM Kintai k WHERE person = :id  and k.out is null");
        query.setParameter("id", person);
        kintai = (Kintai)query.getSingleResult();
        return kintai;
        }
        catch(NoResultException e){
            System.out.println("ありませんエラーです");
            kintai = null;
            return kintai;
        }
        
    }
    //今日出勤していてかつ退勤が押されていないpeople_idを検索
    public Kintai findTodayKintaiById(Person person){
        LocalDate date = LocalDate.now();
        Kintai kintai = new Kintai();
        try{
            Query query = entityManager.createQuery("From Kintai k where person = :id and k.out is null and date = :date");
            query.setParameter("id", person);
            query.setParameter("date", date);
            kintai = (Kintai)query.getSingleResult();
            return kintai;
        }
        catch(NoResultException e){
            System.out.println("ありませんでした");
            return kintai;

        }

    }
    //出勤ボタンが押されているか確認(12/2日に作成)
    public Boolean findCheckGoKintaiId(Person person,LocalDate date){
        try{ 
            Query query = entityManager.createQuery("From Kintai k where k.person = :person and k.date = :date and k.go is not null");
            query.setParameter("person", person);
            query.setParameter("date", date);
            Kintai kintai = (Kintai)query.getSingleResult();
            System.out.println(kintai.getPerson().getName());
            return true;
        }catch(NoResultException e){
            
            return false;
        }

        
        
    }
    
    //これも使ってませんメモ用(javaに直接SQL文を書く)
    public void add(long personid,Kintai kintai){
        String sql = "UPDATE kintai SET end_time = ? where id = ?";
        jdbcTemplate.update(sql,kintai.getOut(),personid);
        
    }
    //日付を引数にデータベースにアクセスする
    public List<Kintai> todayList(LocalDate date){
        Query query = entityManager.createQuery("from Kintai where date = :date order by id desc");
        query.setParameter("date", date);
        System.out.println("日付は" + date);
        List<Kintai> list = (List<Kintai>)query.getResultList();
        return list;
    }
    //こちらは使ってません。JAVAにJPA以外の方法でデーターベースアクセスする方法。javaに直接SQL文を書く方法
    // public void add(Kintai kintai){
    //     String sql = "INSERT INTO kintai"+
	// 			"(people_id,start_time,date)"+
	// 			"VALUES(?,?,?)";//???にはそれぞれ左からpeople_id,start_time,dateがはいる
    //         jdbcTemplate.update(sql,kintai.getPerson().getId(),kintai.getGo(),kintai.getOut());
    //     }
        //これは削除予定
    public List<Kintai> findByDate(LocalDate date){
        //今の月を表すのはnow.getMonthValue()：日を表すのは、now.getDayOfMonth()
        //kはエイリアス（変数みたいなもの）
        Query query = entityManager.createQuery(
            "From Kintai  where date = :date");//date = current_date"で今日
            System.out.println("findByDateの日付を検索します" +date);
        query.setParameter("date", date);
        List<Kintai> list = (List<Kintai>)query.getResultList();
        return list;
    }
    public List<Kintai> findAll(){
        List<Kintai> list;
        Query query = entityManager.createQuery("From Kintai order by date desc");//where のあとにはカラム名ではなくてフィールド名
        list = query.getResultList();
        return list;
    }
    //一ヶ月の給料と勤怠記録を検索
    public List<Kintai> findMonthKintai(Person person,LocalDate date){
        List<Kintai> list;
        Query query = entityManager.createQuery(
            "From Kintai k where k.person = :person and k.date between :startdate and :enddate order by k.date desc");
            query.setParameter("person", person);
            query.setParameter("startdate",date.minusMonths(1).withDayOfMonth(26));
            query.setParameter("enddate", date.withDayOfMonth(25));
        list = (List<Kintai>)query.getResultList();
        return list;
    }
    public List<Kintai> findMonthKintai2(Person person,LocalDate date){
        int year = date.getYear();
        int month = date.getMonthValue();
        Query query = entityManager.createQuery(
    "From Kintai k where k.person = :person and k.date between :startdate and :enddate");
        query.setParameter("person", person);
        query.setParameter("startdate", date.of(year,month,1));
        query.setParameter("enddate", date.of(year,month,date.lengthOfMonth()));
        List<Kintai> list = (List<Kintai>)query.getResultList();
        System.out.println(list.size() + "これは勤怠記録dao");
        return list;
    }
    
   
    
    
    
    


}


