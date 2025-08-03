package com.example.subtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subtracker.adapter.SubscriptionAdapter;
import com.example.subtracker.databinding.ActivityMainBinding;
import com.example.subtracker.entites.Subscription;
import com.example.subtracker.network.ApiClient;
import com.example.subtracker.network.ApiService;
import com.example.subtracker.notification.ReminderManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SubscriptionAdapter adapter;
    private List<Subscription> subscriptions = new ArrayList<>();
    private ApiService apiService;
    private TextView tvTotalAmount, tvActiveSubscriptions, tvNextDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        // Request SMS permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 
                    1001);
        }

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbarTitle.setText("SubTrackr");

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize views
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvActiveSubscriptions = findViewById(R.id.tvActiveSubscriptions);
//        tvNextDueDate = findViewById(R.id.tvNextDueDate);

        // Setup RecyclerView
        setupRecyclerView();

        // FAB click action with animation and intent
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FAB pop animation
                view.animate()
                        .scaleX(0.8f).scaleY(0.8f)
                        .setDuration(100)
                        .withEndAction(() -> view.animate().scaleX(1f).scaleY(1f).setDuration(100).start())
                        .start();
                // Launch AddSub activity
                Intent intent = new Intent(MainActivity.this, AddSub.class);
                startActivity(intent);
            }
        });
        // Analytics FAB click action (left bottom)
        binding.fabAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Analytics.class);
                startActivity(intent);
            }
        });

        // Load subscriptions
        loadSubscriptions();
    }

    private void setupRecyclerView() {
        adapter = new SubscriptionAdapter(subscriptions);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Set click listeners
        adapter.setOnDeleteClickListener(subscription -> {
            deleteSubscription(subscription);
        });
    }

    private void loadSubscriptions() {
        apiService.getAllSubscriptions().enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subscriptions = response.body();
                    adapter.updateSubscriptions(subscriptions);
                    updateDashboard();
                    
                    // Schedule reminders for all subscriptions
                    for (Subscription subscription : subscriptions) {
                        if (subscription.isAutoPay()) {
                            ReminderManager.schedulePaymentReminders(MainActivity.this, subscription);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load subscriptions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSubscription(Subscription subscription) {
        apiService.deleteSubscription(subscription.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cancel reminders for deleted subscription
                    ReminderManager.cancelReminders(MainActivity.this, subscription);
                    
                    Toast.makeText(MainActivity.this, "Subscription deleted", Toast.LENGTH_SHORT).show();
                    loadSubscriptions(); // Reload the list
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete subscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDashboard() {
        double totalAmount = 0;
        int activeSubscriptions = subscriptions.size();
        String nextDueDate = "No subscriptions";

        for (Subscription subscription : subscriptions) {
            if (subscription.getPrice() != null) {
                totalAmount += subscription.getPrice();
            }
        }

        // Amounts are already in Indian Rupees, no conversion needed
        binding.tvTotalAmount.setText(String.format("₹%.2f", totalAmount));
        binding.tvActiveSubscriptions.setText(String.valueOf(activeSubscriptions));
//        binding.tvNextDueDate.setText(nextDueDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload subscriptions when returning from AddSub
        loadSubscriptions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Handle back arrow click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // or finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openAnalytics(View view) {
        Intent intent = new Intent(this, Analytics.class);
        startActivity(intent);
    }
}
