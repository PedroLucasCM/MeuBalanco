package com.example.meubalanco.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.meubalanco.R;
import com.example.meubalanco.data.Category;
import com.example.meubalanco.data.Transaction;
import com.example.meubalanco.viewmodel.CategoryViewModel;
import com.example.meubalanco.viewmodel.TransactionViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Context;  // Para Context


public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private RadioGroup rgType;
    private Spinner spCategory;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private ArrayAdapter<String> categoryAdapter;


    // Constante para o código de requisição
    public static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

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
        rgType = findViewById(R.id.rgType);
        spCategory = findViewById(R.id.spCategory);
        Button btnSave = findViewById(R.id.btnSave);

        // Configura ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Formatação monetária em tempo real
        etAmount.addTextChangedListener((TextWatcher) new MoneyTextWatcher(etAmount));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        loadCategories();

        btnSave.setOnClickListener(v -> saveTransaction());
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

    private void saveTransaction() {

        // Validação dos campos
        if (etAmount.getText().toString().trim().isEmpty()) {
            etAmount.setError("Digite um valor válido!");
            return;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Digite uma descrição!");
            return;
        }

        try {
            String amountText = etAmount.getText().toString()
                    .replaceAll("[^\\d]", "")  // Remove tudo que não for dígito
                    .replaceAll("\\s+", "");   // Remove espaços especiais

            // Verifica se há valor para converter
            if (amountText.isEmpty()) {
                etAmount.setError("Digite um valor válido!");
                return;
            }

            // Converte para double (já em centavos)
            double rawAmount = Double.parseDouble(amountText) / 100;

            // Restante do método permanece igual...
            String type = rgType.getCheckedRadioButtonId() == R.id.rbIncome ? "income" : "expense";
            double finalAmount = type.equals("income") ? rawAmount : -rawAmount;
            // Valida o valor mínimo
            if (Math.abs(finalAmount) < 0.01) {
                etAmount.setError("Valor mínimo: R$ 0,01");
                return;
            }

            // Obtém os demais valores
            String category = spCategory.getSelectedItem().toString();
            String description = etDescription.getText().toString();
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            // Cria e salva a transação
            Transaction transaction = new Transaction();
            transaction.setAmount(finalAmount);
            transaction.setType(type);
            transaction.setCategory(category);
            transaction.setDescription(description);
            transaction.setDate(date);

            transactionViewModel.insert(transaction);

            // Feedback visual
            showTransactionSuccess(type, finalAmount);
            setResult(RESULT_OK);

            // Notificação
            String transactionType = transaction.getType().equals("income") ? "Receita" : "Despesa";
            String msg = transactionType + " de " + formatCurrency(transaction.getAmount()) + " registrada!";

            showNotification(this, "Nova Transação", msg);
            finish();

        } catch (NumberFormatException e) {
            etAmount.setError("Valor inválido");
            Log.e("AddTransaction", "Erro ao converter: " + etAmount.getText().toString(), e);
        }
    }

    private String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
    }

    private void showTransactionSuccess(String type, double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String message = type.equals("income")
                ? "Receita de " + format.format(amount) + " adicionada"
                : "Despesa de " + format.format(Math.abs(amount)) + " registrada";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "meubalanco_channel")
                .setSmallIcon(R.drawable.ic_money)  // Ícone obrigatório
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);  // Fecha ao clicar

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            manager.notify(new Random().nextInt(1000), builder.build());  // ID aleatório para múltiplas notificações
        }
    }

    // Cria o canal de notificação
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "meubalanco_channel",
                    "Transações",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Classe auxiliar para formatação monetária
    private static class MoneyTextWatcher implements TextWatcher {
        private final EditText editText;
        private String current = "";

        public MoneyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(current)) {
                editText.removeTextChangedListener(this);

                // Remove todos os caracteres não numéricos
                String cleanString = s.toString().replaceAll("[^\\d]", "");

                try {
                    if (!cleanString.isEmpty()) {
                        // Formata como valor monetário
                        double value = Double.parseDouble(cleanString) / 100;
                        String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"))
                                .format(value)
                                .replace("\u00A0", " "); // Substitui espaço não separável por espaço normal

                        current = formatted;
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } else {
                        current = "";
                        editText.setText("");
                    }
                } catch (Exception e) {
                    current = "";
                    editText.setText("");
                }

                editText.addTextChangedListener(this);
            }
        }
    }


}