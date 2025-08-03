package com.example.subtracker;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.subtracker.databinding.ActivityAddSubBinding;
import com.example.subtracker.entites.Subscription;
import com.example.subtracker.network.ApiClient;
import com.example.subtracker.network.ApiService;
import com.example.subtracker.notification.ReminderManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSub extends AppCompatActivity {

    private ActivityAddSubBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityAddSubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup toolbar with back button
        //setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Subscription");
        }

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Submit button click listener
        binding.btnSubmit.setOnClickListener(view -> saveSubscription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSubscription() {
        String category = binding.etCategory.getText().toString().trim();
        String name = binding.etService.getText().toString().trim();
        String priceText = binding.etAmount.getText().toString().trim();
        String nextPaymentDate = binding.etDueDate.getText().toString().trim();
        boolean autoPay = binding.switch1.isChecked(); // Ensure correct switch ID

        if (category.isEmpty() || name.isEmpty() || priceText.isEmpty() || nextPaymentDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format date for Spring Boot (convert from dd/MM/yyyy to yyyy-MM-dd)
        String formattedDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(nextPaymentDate);
            formattedDate = outputFormat.format(date);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date format. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        Subscription sub = new Subscription(category, name, price, formattedDate, autoPay);

        // Add loading indicator
        binding.btnSubmit.setEnabled(false);
        binding.btnSubmit.setText("Saving...");

        // Call API
        apiService.saveSubscription(sub).enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                binding.btnSubmit.setEnabled(true);
                binding.btnSubmit.setText("Submit");

                if (response.isSuccessful() && response.body() != null) {
                    ReminderManager.schedulePaymentReminders(AddSub.this, response.body());
                    Toast.makeText(AddSub.this, "Subscription saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMsg = "Failed to save: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += " - " + response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    Toast.makeText(AddSub.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                binding.btnSubmit.setEnabled(true);
                binding.btnSubmit.setText("Submit");

                String errorMsg = "Network error: " + t.getMessage();
                if (t instanceof java.net.ConnectException) {
                    errorMsg = "Cannot connect to server. Please check your internet connection.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMsg = "Request timeout. Please try again.";
                }
                Toast.makeText(AddSub.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
