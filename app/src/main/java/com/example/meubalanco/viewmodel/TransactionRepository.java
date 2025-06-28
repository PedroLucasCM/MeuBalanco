package com.example.meubalanco.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.meubalanco.data.AppDatabase;
import com.example.meubalanco.data.Transaction;
import com.example.meubalanco.data.TransactionDao;

import java.util.List;
import java.util.concurrent.Executor;

public class TransactionRepository {
    private final TransactionDao transactionDao;
    private final Executor executor;
    private final LiveData<List<Transaction>> allTransactions;
    private final LiveData<Double> totalIncome;
    private final LiveData<Double> totalExpenses;

    public TransactionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        executor = db.getDatabaseExecutor();

        // Inicializa as consultas
        allTransactions = transactionDao.getAllTransactions();
        totalIncome = transactionDao.getTotalIncome();
        totalExpenses = transactionDao.getTotalExpenses();
    }

    public LiveData<Transaction> getTransactionById(int id) {
        return transactionDao.getTransactionById(id);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    public LiveData<Double> getTotalIncome() {
        return transactionDao.getSumByType("income");
    }

    public LiveData<Double> getTotalExpenses() {
        return transactionDao.getSumByType("expense");
    }

    public LiveData<Double> getBalance() {
        return transactionDao.getBalance();
    }

    public void insert(Transaction transaction) {
        executor.execute(() -> transactionDao.insert(transaction));
    }

    public void delete(Transaction transaction) {
        executor.execute(() -> transactionDao.delete(transaction));
    }

    public void deleteTransaction(Transaction transaction) {
        executor.execute(() -> {
            transactionDao.deleteById(transaction.getId()); // Método 2
        });
    }
    public void update(Transaction transaction) {
        executor.execute(() -> transactionDao.update(transaction));
    }
}