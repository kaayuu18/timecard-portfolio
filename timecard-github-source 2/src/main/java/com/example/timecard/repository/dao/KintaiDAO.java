package com.example.timecard.repository.dao;

import java.io.Serializable;

public interface KintaiDAO<T> extends Serializable{
    public T findById(long id);
    
}

