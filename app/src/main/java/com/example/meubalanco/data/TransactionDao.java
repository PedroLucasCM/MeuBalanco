package com.example.meubalanco.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {
    // Operações básicas
    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE id = :id")
    LiveData<Transaction> getTransactionById(int id);
    // Consultas
    @Query("SELECT * FROM transactions ORDER BY date ASC")
    LiveData<List<Transaction>> getAllTransactions();

    // Receitas (soma de valores positivos)
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE amount > 0")
    LiveData<Double> getTotalIncome();

    // Despesas (soma de valores negativos, convertidos para positivo)
    @Query("SELECT COALESCE(ABS(SUM(amount)), 0) FROM transactions WHERE amount < 0")
    LiveData<Double> getTotalExpenses();

    // Saldo (soma total)
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions")
    LiveData<Double> getBalance();

    // Consulta por tipo (usando type = "income" ou "expense")
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = :type")
    LiveData<Double> getSumByType(String type);

    // Delete por ID
    @Query("DELETE FROM transactions WHERE id = :id")
    void deleteById(int id);
}