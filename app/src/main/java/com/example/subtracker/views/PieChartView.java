package com.example.subtracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {
    private List<PieSlice> slices = new ArrayList<>();
    private Paint slicePaint;
    private Paint textPaint;
    private Paint centerPaint;
    private int[] colors = {
        Color.parseColor("#FF5722"), Color.parseColor("#2196F3"), 
        Color.parseColor("#4CAF50"), Color.parseColor("#FFC107"),
        Color.parseColor("#9C27B0"), Color.parseColor("#00BCD4"),
        Color.parseColor("#FF9800"), Color.parseColor("#795548")
    };

    public static class PieSlice {
        public String label;
        public float value;
        public float percentage;

        public PieSlice(String label, float value, float percentage) {
            this.label = label;
            this.value = value;
            this.percentage = percentage;
        }
    }

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        slicePaint = new Paint();
        slicePaint.setStyle(Paint.Style.FILL);
        slicePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(12f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        centerPaint = new Paint();
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setAntiAlias(true);
    }

    public void setData(List<PieSlice> data) {
        this.slices = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (slices.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 3;
        int holeRadius = radius / 3;

        // Draw pie slices
        float startAngle = 0;
        for (int i = 0; i < slices.size(); i++) {
            PieSlice slice = slices.get(i);
            float sweepAngle = (slice.percentage / 100f) * 360f;

            slicePaint.setColor(colors[i % colors.length]);

            // Draw slice
            RectF rect = new RectF(centerX - radius, centerY - radius, 
                                  centerX + radius, centerY + radius);
            canvas.drawArc(rect, startAngle, sweepAngle, true, slicePaint);

            // Draw text on slice
            float textAngle = startAngle + sweepAngle / 2;
            float textRadius = radius * 0.7f;
            float textX = centerX + (float) (textRadius * Math.cos(Math.toRadians(textAngle)));
            float textY = centerY + (float) (textRadius * Math.sin(Math.toRadians(textAngle)));

            canvas.drawText(String.format("%.1f%%", slice.percentage), 
                          textX, textY + 4, textPaint);

            startAngle += sweepAngle;
        }

        // Draw center hole
        canvas.drawCircle(centerX, centerY, holeRadius, centerPaint);
    }
} 