package com.example.meubalanco.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Query("SELECT name FROM categories")
    LiveData<List<String>> getAllCategories();

    @Query("SELECT COUNT(*) FROM categories")
    int getCount();
}
