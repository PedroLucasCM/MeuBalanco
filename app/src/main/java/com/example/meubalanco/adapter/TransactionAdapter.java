package com.example.meubalanco.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meubalanco.R;
import com.example.meubalanco.data.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {

    private static OnItemClickListener listener;

    // Interface para clique
    public interface OnItemClickListener {
        void onTransactionClick(Transaction transaction);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Construtor
    public TransactionAdapter() {
        super(new TransactionDiffCallback());
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = getItem(position); // Mudança importante aqui
        holder.bind(transaction);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(transaction);
            }
        });
    }

    // Método simplificado para atualização
    public void submitTransactionList(List<Transaction> transactions) {
        super.submitList(transactions); // Usa o submitList nativo do ListAdapter
    }


    // 🔥 Classe TransactionViewHolder (Implementação Completa)
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategory;
        private final TextView tvDescription;
        private final TextView tvAmount;
        private final TextView tvDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }


        // Método para vincular dados
        public void bind(final Transaction transaction) {
            tvCategory.setText(transaction.getCategory());
            tvDescription.setText(transaction.getDescription());
            tvDate.setText(transaction.getDate());

            // Formata o valor (negativo para despesas)
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String formattedAmount = format.format(transaction.getAmount());
            tvAmount.setText(formattedAmount);

            // Muda a cor conforme o tipo
            int color;
            if (transaction.getAmount() >= 0) {
                color = ContextCompat.getColor(itemView.getContext(), R.color.color_income); // Receita
            } else {
                color = ContextCompat.getColor(itemView.getContext(), R.color.color_expense); // Despesa
                formattedAmount = "-" + formattedAmount; // Adiciona sinal negativo (opcional)
            }
            tvAmount.setTextColor(color);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onTransactionClick(transaction);
                }
            });
        }
    }

    static class TransactionDiffCallback extends DiffUtil.ItemCallback<Transaction> {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.equals(newItem);
        }
    }
}