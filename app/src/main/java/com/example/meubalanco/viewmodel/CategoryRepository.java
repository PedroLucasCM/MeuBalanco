package com.example.meubalanco.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.meubalanco.data.AppDatabase;
import com.example.meubalanco.data.Category;
import com.example.meubalanco.data.CategoryDao;
import com.example.meubalanco.data.Transaction;

import java.util.List;
import java.util.concurrent.Executor;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final Executor executor;
    private final LiveData<List<String>> allCategories;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        executor = db.getDatabaseExecutor();

        allCategories = categoryDao.getAllCategories();
        insertDefaultCategories();
    }
    public void insert(Category category){
        executor.execute(() ->  categoryDao.insert(category));
    }
    public LiveData<List<String>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void insertDefaultCategories() {
        executor.execute(() -> {
            if (categoryDao.getCount() == 0) {  // Adicione este método no DAO
                String[] defaultCategories = {
                        "Alimentação", "Transporte", "Moradia",
                        "Lazer", "Saúde", "Educação", "Salário", "Investimentos"
                };

                for (String name : defaultCategories) {
                    Category category = new Category();
                    category.name = name;
                    categoryDao.insert(category);
                }
            }
        });
    }
}
