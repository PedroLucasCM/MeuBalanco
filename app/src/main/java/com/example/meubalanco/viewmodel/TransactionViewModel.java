package com.example.meubalanco.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.meubalanco.data.Transaction;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private final TransactionRepository repository;

    public TransactionViewModel(Application application) {
        super(application);
        repository = new TransactionRepository(application);
    }

    // Método para obter todas as transações
    public LiveData<List<Transaction>> getAllTransactions() {
        return repository.getAllTransactions();
    }

    // Método para inserir transação
    public void insert(Transaction transaction) {
        repository.insert(transaction);
    }

    // Método para deletar transação
    public void delete(Transaction transaction) {
        repository.delete(transaction);
    }

    // Método para obter uma transação específica
    public LiveData<Transaction> getTransactionById(int id) {
        return repository.getTransactionById(id);
    }

    // Método para obter todas as receitas
    public LiveData<Double> getTotalIncome() {
        return repository.getTotalIncome();
    }

    // Método para obter todas as despesas
    public LiveData<Double> getTotalExpenses() {
        return repository.getTotalExpenses();
    }

    // Método para deletar a balança (receita + despesa)
    public LiveData<Double> getBalance() {
        return repository.getBalance();
    }

    // Método para deletar uma transação
    public void deleteTransaction(Transaction transaction) {
        repository.deleteTransaction(transaction);
    }

    //Metodo para atualizar a transaçao
    public void update(Transaction transaction) {
        repository.update(transaction);
    }
}