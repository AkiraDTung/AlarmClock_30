package com.example.alarmclock30.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alarmclock30.R;
import com.example.alarmclock30.adapters.AlarmAdapter;
import com.example.alarmclock30.databinding.ActivitySetAlarmBinding;
import com.example.alarmclock30.entities.Clock;
import com.example.alarmclock30.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

public class SetAlarmActivity extends AppCompatActivity {

    private ActivitySetAlarmBinding binding;
    private List<Clock> alarms;
    private AlarmAdapter alarmAdapter;
    private AlarmReceiver alarmReceiver;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        unregisterAlarmReceiver();
    }

    private void init() {
        alarmReceiver = new AlarmReceiver();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        registerAlarmReceiver();
    }

    private void controlAction() {
        binding.btnAddAlarm.setOnClickListener(v -> {
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();
            setAlarm(hour, minute);
        });
    }

    private void setAlarm(int hour, int minute) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long alarmTime = calendar.getTimeInMillis();

        if (alarmTime <= currentTime) {
            alarmTime += 24 * 60 * 60 * 1000;
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent);
        Toast.makeText(this, "Báo thức đã được đặt", Toast.LENGTH_SHORT).show();
    }

    private void registerAlarmReceiver() {
        IntentFilter filter = new IntentFilter("android.intent.action.ALARM_RECEIVER");
        registerReceiver(alarmReceiver, filter);
    }

    private void unregisterAlarmReceiver() {
        if (alarmReceiver != null) {
            unregisterReceiver(alarmReceiver);
        }
    }

}
