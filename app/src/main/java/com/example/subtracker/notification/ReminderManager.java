package com.example.subtracker.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.subtracker.entites.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReminderManager {
    private static final String TAG = "ReminderManager";

    public static void schedulePaymentReminders(Context context, Subscription subscription) {
        if (subscription.getNextPaymentDate() == null) {
            Log.w(TAG, "Cannot schedule reminder: payment date is null");
            return;
        }

        try {
            // Parse the due date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dueDate = dateFormat.parse(subscription.getNextPaymentDate());
            
            if (dueDate == null) {
                Log.w(TAG, "Cannot parse due date: " + subscription.getNextPaymentDate());
                return;
            }

            Calendar dueCalendar = Calendar.getInstance();
            dueCalendar.setTime(dueDate);

            // Schedule reminder 2 days before due date
            scheduleReminder(context, subscription, dueCalendar, -2, "before_due");
            
            // Schedule reminder on due date
            scheduleReminder(context, subscription, dueCalendar, 0, "on_due");

            Log.d(TAG, "Scheduled reminders for " + subscription.getName() + " due on " + subscription.getNextPaymentDate());

        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + subscription.getNextPaymentDate(), e);
        }
    }

    private static void scheduleReminder(Context context, Subscription subscription, 
                                       Calendar dueCalendar, int daysOffset, String reminderType) {
        
        Calendar reminderCalendar = (Calendar) dueCalendar.clone();
        reminderCalendar.add(Calendar.DAY_OF_MONTH, daysOffset);
        reminderCalendar.set(Calendar.HOUR_OF_DAY, 9); // 9 AM
        reminderCalendar.set(Calendar.MINUTE, 0);
        reminderCalendar.set(Calendar.SECOND, 0);

        // Don't schedule if the time has already passed
        if (reminderCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Log.d(TAG, "Reminder time has already passed for " + subscription.getName());
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null");
            return;
        }

        Intent intent = new Intent(context, PaymentReminderReceiver.class);
        intent.putExtra("service_name", subscription.getName());
        intent.putExtra("amount", String.format("%.2f", subscription.getPrice()));
        intent.putExtra("due_date", subscription.getNextPaymentDate());
        intent.putExtra("reminder_type", reminderType);
        intent.putExtra("subscription_id", subscription.getId().intValue());

        // Create unique request code for each reminder
        int requestCode = subscription.getId().intValue() * 10 + (daysOffset == -2 ? 1 : 2);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 
                requestCode, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Schedule exact alarm
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderCalendar.getTimeInMillis(),
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminderCalendar.getTimeInMillis(),
                    pendingIntent
            );
        }

        Log.d(TAG, "Scheduled " + reminderType + " reminder for " + subscription.getName() + 
                " at " + reminderCalendar.getTime());
    }

    public static void cancelReminders(Context context, Subscription subscription) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        // Cancel both reminders (2 days before and on due date)
        for (int i = 1; i <= 2; i++) {
            int requestCode = subscription.getId().intValue() * 10 + i;
            
            Intent intent = new Intent(context, PaymentReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 
                    requestCode, 
                    intent, 
                    PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
            );
            
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Log.d(TAG, "Cancelled reminder " + i + " for " + subscription.getName());
            }
        }
    }
} 