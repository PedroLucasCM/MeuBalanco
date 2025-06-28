package com.example.meubalanco.ui;

import static com.example.meubalanco.ui.AddTransactionActivity.NOTIFICATION_PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meubalanco.R;
import com.example.meubalanco.adapter.TransactionAdapter;
import com.example.meubalanco.data.Transaction;
import com.example.meubalanco.viewmodel.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;  // Para Context

import androidx.core.app.NotificationCompat;  // Para NotificationCompat
import androidx.core.content.ContextCompat;  // Para ContextCompat (opcional)

import java.util.Random;

public class MainActivity extends AppCompatActivity implements TransactionAdapter.OnItemClickListener {

    private TransactionViewModel transactionViewModel;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private static final String CHANNEL_ID = "meubalanco_channel";
    private ProgressBar progressBar;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MeuBalanco);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        progressBar = findViewById(R.id.progressBar);

        // Configura a Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        // Cria o canal de notificações
        createNotificationChannel();

        // Inicializa Views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        // Configura RecyclerView
        adapter = new TransactionAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Inicializa ViewModel
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Observa as transações (atualiza a lista automaticamente)
        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            if (transactions != null || transactions.isEmpty()) {
                Log.d("MainActivity", "Dados recebidos: " + transactions.size()); // Verifique no Logcat
                showLoadingAnimation(() -> {
                    adapter.submitTransactionList(transactions);
                });
            }
        });

        // Botão para adicionar nova transação
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            startActivity(intent);
            transactionViewModel.getAllTransactions().observe(this, transactions -> {
                if (transactions != null || transactions.isEmpty()) {
                    Log.d("MainActivity", "Dados recebidos: " + transactions.size()); // Verifique no Logcat
                    adapter.submitTransactionList(transactions);
                }
            });
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1
                );
            }
        }
    }

    private void showLoadingAnimation(Runnable onComplete) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        handler.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            onComplete.run();
        }, 500); // meio segundo de delay
    }

    // Menu da Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_reports) {
            startActivity(new Intent(this, ReportsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Clique em um item da lista (**editar**/excluir)
    @Override
    public void onTransactionClick(Transaction transaction) {
        // Cria um array com as opções
        CharSequence[] options = {"Editar", "Excluir"};

        new AlertDialog.Builder(this)
                .setTitle("Opções da Transação")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Editar
                            editTransaction(transaction);
                            break;
                        case 1: // Excluir
                            deleteTransaction(transaction);
                            break;
                    }
                })
                .show();
    }

    private void editTransaction(Transaction transaction) {
        Intent intent = new Intent(this, EditTransactionActivity.class);
        intent.putExtra("transaction_id", transaction.getId());
        startActivity(intent);
    }

    private void deleteTransaction(Transaction transaction) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir esta transação?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Feedback visual imediato
                    int position = adapter.getCurrentList().indexOf(transaction);
                    adapter.notifyItemRemoved(position);

                    // Exclusão no banco de dados
                    transactionViewModel.deleteTransaction(transaction);

                    Toast.makeText(this, "Transação excluída", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, pode mostrar notificação
                Toast.makeText(this, "Notificações ativadas!", Toast.LENGTH_SHORT).show();
            } else {
                // Permissão negada
                Toast.makeText(this, "Notificações desativadas nas configurações",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cria o canal de notificações
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "meubalanco_channel",      // ID do canal
                    "Transações Financeiras",  // Nome visível ao usuário
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificações de receitas e despesas");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}