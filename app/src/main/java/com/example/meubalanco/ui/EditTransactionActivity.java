package com.example.meubalanco.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.meubalanco.R;
import com.example.meubalanco.data.Category;
import com.example.meubalanco.data.Transaction;
import com.example.meubalanco.viewmodel.CategoryViewModel;
import com.example.meubalanco.viewmodel.TransactionViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class EditTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDescription, etDate;
    private RadioGroup rgType;
    private Spinner spCategory;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private ArrayAdapter<String> categoryAdapter;
    private int transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adiciona botão de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageButton btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        // Inicializa Views
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        rgType = findViewById(R.id.rgType);
        spCategory = findViewById(R.id.spCategory);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Configura ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);


        // Obtém o ID da transação
        transactionId = getIntent().getIntExtra("transaction_id", -1);
        if (transactionId == -1) {
            finish();
        }
        Log.i("OBJECT", "" + transactionId);
        // Carrega os dados da transação
        transactionViewModel.getTransactionById(transactionId).observe(this, transaction -> {
            if (transaction != null) {
                Log.i("OBJECT", transaction.toString());

                // Preenche os campos
                etAmount.setText(String.format(Locale.getDefault(), "%.2f", Math.abs(transaction.getAmount())));
                etDescription.setText(transaction.getDescription());
                etDate.setText(transaction.getDate());

                if (transaction.getAmount() >= 0) {
                    rgType.check(R.id.rbIncome);
                } else {
                    rgType.check(R.id.rbExpense);
                }

                categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
                loadCategories();

                // Seleciona a categoria atual
                if (transaction.getCategory() != null) {
                    int spinnerPosition = categoryAdapter.getPosition(transaction.getCategory());
                    spCategory.setSelection(spinnerPosition);
                }
            }
        });


        // Botão Salvar
        btnSave.setOnClickListener(v -> saveTransaction());

        // Botão Cancelar
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveTransaction() {
        // Validação dos campos
        if (etAmount.getText().toString().isEmpty()) {
            etAmount.setError("Digite um valor válido!");
            return;
        }

        try {
            double amount = Double.parseDouble(etAmount.getText().toString());
            String type = rgType.getCheckedRadioButtonId() == R.id.rbIncome ? "income" : "expense";
            double finalAmount = type.equals("income") ? amount : -amount;

            Transaction transaction = new Transaction();
            transaction.setId(transactionId); // Mantém o mesmo ID para atualização
            transaction.setAmount(finalAmount);
            transaction.setType(type);
            transaction.setCategory(spCategory.getSelectedItem().toString());
            transaction.setDescription(etDescription.getText().toString());
            transaction.setDate(etDate.getText().toString());

            // Atualiza no banco de dados
            transactionViewModel.update(transaction);

            Toast.makeText(this, "Transação atualizada!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            etAmount.setError("Valor inválido");
        }
    }

    private void loadCategories() {
        categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        // Observa as categorias
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.clear();
            categoryAdapter.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nova Categoria");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            String categoryName = input.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                Category category = new Category();
                category.name = categoryName;
                categoryViewModel.insert(category);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}