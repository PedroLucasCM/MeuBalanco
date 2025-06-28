package com.example.meubalanco.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.meubalanco.R;
import com.example.meubalanco.viewmodel.TransactionViewModel;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class ReportsActivity extends AppCompatActivity {
    private TextView tvTotalIncome, tvTotalExpenses, tvBalance;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializa Views
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        tvBalance = findViewById(R.id.tvBalance);

        // Inicializa ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Observa os dados
        loadTransactionData();
    }

    private void loadTransactionData() {

        transactionViewModel.getTotalIncome().observe(this, totalIncome -> {
            if (totalIncome != null) {
                String formattedIncome = formatCurrency(totalIncome);
                tvTotalIncome.setText("Receitas: " + formattedIncome);
            }
        });

        transactionViewModel.getTotalExpenses().observe(this, totalExpenses -> {
            if (totalExpenses != null) {
                String formattedExpenses = formatCurrency(totalExpenses);
                tvTotalExpenses.setText("Despesas: " + formattedExpenses);
            }
        });

        updateBalance();
    }

    private void updateBalance() {
        // pega o Saldo
        transactionViewModel.getBalance().observe(this, balance -> {
            if (balance != null) {
                String formattedBalance = formatCurrency(balance);
                tvBalance.setText("Saldo: " + formattedBalance);

                // Muda a cor conforme o saldo
                int color = balance >= 0 ?
                        ContextCompat.getColor(this, R.color.color_income) :
                        ContextCompat.getColor(this, R.color.color_expense);
                tvBalance.setTextColor(color);
            }
        });
    }

    private String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}