package com.example.subtracker.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.subtracker.MainActivity;
import com.example.subtracker.R;

public class PaymentReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "payment_reminders";
    private static final String CHANNEL_NAME = "Payment Reminders";
    private static final String CHANNEL_DESCRIPTION = "Notifications for subscription payment reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        String serviceName = intent.getStringExtra("service_name");
        String amount = intent.getStringExtra("amount");
        String dueDate = intent.getStringExtra("due_date");
        String reminderType = intent.getStringExtra("reminder_type");
        int subscriptionId = intent.getIntExtra("subscription_id", 0);

        createNotificationChannel(context);
        showNotification(context, serviceName, amount, dueDate, reminderType, subscriptionId);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 200, 500});

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(Context context, String serviceName, String amount, 
                                String dueDate, String reminderType, int subscriptionId) {
        
        String title;
        String message;
        int icon;

        if ("before_due".equals(reminderType)) {
            title = "💳 Upcoming Subscription Payment";
            message = String.format("Hey Mahir, your %s subscription of ₹%s is due on %s. Make sure your wallet has enough balance to avoid service interruption.", 
                    serviceName, amount, dueDate);
            icon = android.R.drawable.ic_dialog_alert;
        } else {
            title = "⚠️ Payment Due Today";
            message = String.format("Your %s subscription of ₹%s is due today. We'll auto-deduct from your wallet. Please ensure sufficient balance.", 
                    serviceName, amount);
            icon = android.R.drawable.ic_dialog_alert;
        }

        // Create intent to open the app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, subscriptionId, intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 500, 200, 500});

        // Show notification
        NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(subscriptionId, builder.build());
        }
    }
} 