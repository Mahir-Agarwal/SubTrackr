package com.example.subtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.subtracker.entites.Subscription;
import com.example.subtracker.network.ApiClient;
import com.example.subtracker.network.ApiService;
import com.example.subtracker.views.LineChartView;
import com.example.subtracker.views.PieChartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Analytics extends AppCompatActivity {

    private ApiService apiService;
    private List<Subscription> subscriptions = new ArrayList<>();
    
    // Views for displaying analytics
    private TextView tvTotalSpend;
    private TextView tvMonthlyAverage;
    private TextView tvCategoryBreakdown;
    
    // Custom chart views
    private LineChartView lineChart;
    private PieChartView pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize views
        tvTotalSpend = findViewById(R.id.tvTotalSpend);
        tvMonthlyAverage = findViewById(R.id.tvMonthlyAverage);
        tvCategoryBreakdown = findViewById(R.id.tvCategoryBreakdown);
        
        // Initialize custom charts
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);

        // Setup toolbar with back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Analytics");
        }

        // Load data
        loadSubscriptions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSubscriptions() {
        apiService.getAllSubscriptions().enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subscriptions = response.body();
                    updateAnalytics();
                } else {
                    Toast.makeText(Analytics.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                Toast.makeText(Analytics.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAnalytics() {
        calculateTotalSpend();
        updateLineChart();
        updatePieChart();
    }

    private void calculateTotalSpend() {
        double totalSpend = 0;
        for (Subscription sub : subscriptions) {
            if (sub.getPrice() != null) {
                totalSpend += sub.getPrice();
            }
        }

        double monthlyAverage = totalSpend / 12; // Assuming 12 months

        // Amounts are already in Indian Rupees, no conversion needed
        tvTotalSpend.setText(String.format("Total Annual Spend: ₹%.2f", totalSpend));
        tvMonthlyAverage.setText(String.format("Monthly Average: ₹%.2f", monthlyAverage));
    }

    private void updateLineChart() {
        // Create monthly spend data
        List<Float> dataPoints = new ArrayList<>();
        
        // For demo, distribute total spend across months
        double totalSpend = 0;
        for (Subscription sub : subscriptions) {
            if (sub.getPrice() != null) {
                totalSpend += sub.getPrice();
            }
        }

        // Distribute spend across 12 months
        double monthlySpend = totalSpend / 12;
        for (int i = 0; i < 12; i++) {
            dataPoints.add((float) monthlySpend);
        }

        lineChart.setData(dataPoints);
    }

    private void updatePieChart() {
        // Group subscriptions by category
        Map<String, Double> categorySpending = new HashMap<>();
        
        for (Subscription sub : subscriptions) {
            if (sub.getPrice() != null && sub.getCategory() != null) {
                String category = sub.getCategory();
                double currentSpend = categorySpending.getOrDefault(category, 0.0);
                categorySpending.put(category, currentSpend + sub.getPrice());
            }
        }

        // Calculate total for percentage
        double totalSpend = 0;
        for (Double spend : categorySpending.values()) {
            totalSpend += spend;
        }

        // Create pie chart data
        List<PieChartView.PieSlice> slices = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
            String category = entry.getKey();
            double spend = entry.getValue();
            double percentage = (spend / totalSpend) * 100;
            
            slices.add(new PieChartView.PieSlice(category, (float) spend, (float) percentage));
        }

        pieChart.setData(slices);
    }
}