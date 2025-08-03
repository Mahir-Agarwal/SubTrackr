package com.example.subtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.subtracker.R;
import com.example.subtracker.entites.Subscription;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private List<Subscription> subscriptions;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Subscription subscription);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Subscription subscription);
    }

    public SubscriptionAdapter(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscription subscription = subscriptions.get(position);
        // Bind data to views
        holder.tvServiceName.setText(subscription.getName());
        holder.tvCategory.setText(subscription.getCategory());
        
        // Amounts are already in Indian Rupees, no conversion needed
        double price = subscription.getPrice() != null ? subscription.getPrice() : 0;
        holder.tvPrice.setText(String.format("₹%.2f", price));
        
        holder.tvDueDate.setText(subscription.getNextPaymentDate());
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    public void updateSubscriptions(List<Subscription> newSubscriptions) {
        this.subscriptions = newSubscriptions;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName;
        private TextView tvCategory;
        private TextView tvPrice;
        private TextView tvDueDate;
        private ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(subscriptions.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteListener != null) {
                    deleteListener.onDeleteClick(subscriptions.get(position));
                }
            });
        }


    }
} 