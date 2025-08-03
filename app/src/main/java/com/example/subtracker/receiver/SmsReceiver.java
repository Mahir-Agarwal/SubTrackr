package com.example.subtracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.subtracker.entites.Subscription;
import com.example.subtracker.network.ApiClient;
import com.example.subtracker.network.ApiService;
import com.example.subtracker.notification.NotificationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage smsMessage : messages) {
                String messageBody = smsMessage.getMessageBody();
                Log.d(TAG, "SMS received: " + messageBody);

                // Detect subscription from SMS
                Subscription detectedSubscription = SubscriptionDetector.detect(messageBody);

                if (detectedSubscription != null) {
                    Log.d(TAG, "Subscription detected: " + detectedSubscription.getName());
                    saveSubscription(context, detectedSubscription);
                }
            }
        }
    }

    private void saveSubscription(Context context, Subscription subscription) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.saveSubscription(subscription).enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Subscription auto-saved successfully");
                    NotificationHelper.showSubscriptionDetectedNotification(context, subscription);
                } else {
                    Log.e(TAG, "Failed to auto-save subscription: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                Log.e(TAG, "Error auto-saving subscription", t);
            }
        });
    }
} 