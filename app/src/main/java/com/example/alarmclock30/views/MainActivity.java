package com.example.alarmclock30.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.alarmclock30.R;
import com.example.alarmclock30.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Handler handler;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        controlAction();
        updateCurrentTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }

    private void init() {
        handler = new Handler();
        timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy z", Locale.getDefault());
        binding.tvDate.setText(dateFormat.format(new Date()));

    }

    private void controlAction() {
        binding.alarmButton.setOnClickListener(view -> {
            Intent iAlarm = new Intent(MainActivity.this, SetAlarmActivity.class);
            startActivity(iAlarm);
        });
    }

    private void updateCurrentTime() {
        binding.tvTime.setText(timeFormat.format(new Date()));
        handler.postDelayed(this::updateCurrentTime, 1000);
    }
}

