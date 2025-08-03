package com.example.subtracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    private List<Float> dataPoints = new ArrayList<>();
    private Paint linePaint;
    private Paint fillPaint;
    private Paint gridPaint;
    private Paint textPaint;
    private int maxValue = 100;
    private String[] labels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                              "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#4CAF50"));
        linePaint.setStrokeWidth(4f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setColor(Color.parseColor("#4CAF50"));
        fillPaint.setAlpha(50);
        fillPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1f);
        gridPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(12f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Float> data) {
        this.dataPoints = data;
        if (!data.isEmpty()) {
            maxValue = (int) Math.ceil(data.stream().max(Float::compareTo).orElse(100f));
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (dataPoints.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        // Draw grid
        drawGrid(canvas, width, height, padding);

        // Draw line chart
        drawLineChart(canvas, width, height, padding);
    }

    private void drawGrid(Canvas canvas, int width, int height, int padding) {
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;

        // Horizontal grid lines
        for (int i = 0; i <= 4; i++) {
            float y = padding + (chartHeight * i / 4f);
            canvas.drawLine(padding, y, width - padding, y, gridPaint);
        }

        // Vertical grid lines
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + (chartWidth * i / (dataPoints.size() - 1f));
            canvas.drawLine(x, padding, x, height - padding, gridPaint);
        }
    }

    private void drawLineChart(Canvas canvas, int width, int height, int padding) {
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding;

        Path linePath = new Path();
        Path fillPath = new Path();

        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + (chartWidth * i / (dataPoints.size() - 1f));
            float y = height - padding - (chartHeight * dataPoints.get(i) / maxValue);

            if (i == 0) {
                linePath.moveTo(x, y);
                fillPath.moveTo(x, height - padding);
                fillPath.lineTo(x, y);
            } else {
                linePath.lineTo(x, y);
                fillPath.lineTo(x, y);
            }
        }

        // Close fill path
        fillPath.lineTo(width - padding, height - padding);
        fillPath.close();

        // Draw fill
        canvas.drawPath(fillPath, fillPaint);
        
        // Draw line
        canvas.drawPath(linePath, linePaint);

        // Draw data points
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + (chartWidth * i / (dataPoints.size() - 1f));
            float y = height - padding - (chartHeight * dataPoints.get(i) / maxValue);
            canvas.drawCircle(x, y, 6f, linePaint);
        }
    }
} 