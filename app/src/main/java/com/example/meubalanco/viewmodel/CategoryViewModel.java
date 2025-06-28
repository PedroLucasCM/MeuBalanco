package com.example.meubalanco.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.meubalanco.data.Category;
import com.example.meubalanco.data.Transaction;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository repository;

    public CategoryViewModel(Application application) {
        super(application);
        repository = new CategoryRepository(application);
    }

    public LiveData<List<String>> getAllCategories() {
        return repository.getAllCategories();
    }

    public void insert(Category category) {
        repository.insert(category);
    }

}
