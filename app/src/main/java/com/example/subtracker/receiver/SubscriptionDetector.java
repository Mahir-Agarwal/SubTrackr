package com.example.subtracker.receiver;

import android.os.Build;

import com.example.subtracker.entites.Subscription;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubscriptionDetector {

    public static Subscription detect(String message) {
        // Netflix
        if (message.matches(".*Netflix.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Netflix", "Entertainment", extractAmount(message));
        }
        // Amazon Prime Video
        if (message.matches(".*Prime( Video)?( Membership)?( charged| auto-debited)?.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Prime Video", "Entertainment", extractAmount(message));
        }
        // Spotify
        if (message.matches(".*Spotify.*(charged|auto-debited).*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Spotify", "Music", extractAmount(message));
        }
        // Disney+ Hotstar
        if (message.matches(".*Hotstar.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Hotstar", "Entertainment", extractAmount(message));
        }
        // Zee5
        if (message.matches(".*Zee5.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Zee5", "Entertainment", extractAmount(message));
        }
        // SonyLIV
        if (message.matches(".*SonyLIV.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("SonyLIV", "Entertainment", extractAmount(message));
        }
        // Apple Music
        if (message.matches(".*Apple Music.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Apple Music", "Music", extractAmount(message));
        }
        // YouTube Premium
        if (message.matches(".*YouTube Premium.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("YouTube Premium", "Entertainment", extractAmount(message));
        }
        // Google One
        if (message.matches(".*Google One.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Google One", "Cloud", extractAmount(message));
        }
        // Audible
        if (message.matches(".*Audible.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Audible", "Books", extractAmount(message));
        }
        // Gaana
        if (message.matches(".*Gaana.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Gaana", "Music", extractAmount(message));
        }
        // JioSaavn
        if (message.matches(".*JioSaavn.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("JioSaavn", "Music", extractAmount(message));
        }
        // ALTBalaji
        if (message.matches(".*ALTBalaji.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("ALTBalaji", "Entertainment", extractAmount(message));
        }
        // Voot
        if (message.matches(".*Voot.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Voot", "Entertainment", extractAmount(message));
        }
        // Eros Now
        if (message.matches(".*Eros Now.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Eros Now", "Entertainment", extractAmount(message));
        }
        // Sun NXT
        if (message.matches(".*Sun NXT.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Sun NXT", "Entertainment", extractAmount(message));
        }
        // Discovery+
        if (message.matches(".*Discovery\\+.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Discovery+", "Entertainment", extractAmount(message));
        }
        // Times Prime
        if (message.matches(".*Times Prime.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Times Prime", "Lifestyle", extractAmount(message));
        }
        // Tata Play Binge
        if (message.matches(".*Tata Play Binge.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Tata Play Binge", "Entertainment", extractAmount(message));
        }
        // BookMyShow Stream
        if (message.matches(".*BookMyShow Stream.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("BookMyShow Stream", "Entertainment", extractAmount(message));
        }
        // Amazon Kindle
        if (message.matches(".*Kindle.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("Kindle", "Books", extractAmount(message));
        }
        // LinkedIn Premium
        if (message.matches(".*LinkedIn Premium.*charged.*₹(\\d+\\.?\\d*).*")) {
            return createSubscription("LinkedIn Premium", "Professional", extractAmount(message));
        }
        // Add more as needed...
        return null; // No subscription detected
    }

    private static double extractAmount(String message) {
        Pattern amountPattern = Pattern.compile("₹(\\d+\\.?\\d*)");
        Matcher matcher = amountPattern.matcher(message);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0;
    }

    private static Subscription createSubscription(String name, String category, double price) {
        Subscription sub = new Subscription();
        sub.setName(name);
        sub.setCategory(category);
        sub.setPrice(price);
        sub.setAutoPay(true);

        // Set next payment date to one month from now
        LocalDate nextPaymentDate = null;
        DateTimeFormatter formatter = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nextPaymentDate = LocalDate.now().plusMonths(1);
             formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            sub.setNextPaymentDate(nextPaymentDate.format(formatter));
        } else {
            // Fallback for older Android versions
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sub.setNextPaymentDate(sdf.format(calendar.getTime()));
        }



        return sub;
    }
}