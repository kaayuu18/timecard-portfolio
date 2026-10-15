package com.example.timecard.repository.dao;

import java.io.Serializable;
import java.util.List;

public interface PersonDAO<T> extends Serializable{
    public T findById(long id);
    public List<T> findByString(String name);
    

}
