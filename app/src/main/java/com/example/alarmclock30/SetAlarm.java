package com.example.alarmclock30;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SetAlarm extends AppCompatActivity {
    static final int ALARM_REQ_CODE = 0;
    private int jam, min;
    private List<Clock> alarms;
    private AlarmAdapter alarmAdapter;
    private static final String PREFS_NAME = "AlarmPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        FloatingActionButton bTimer = findViewById(R.id.timerButton);
        TimePicker tpAlarm = findViewById(R.id.tp);
        ListView listView = findViewById(R.id.lvAlarms);

        alarms = loadAlarms(); // Implement this method to load alarms from SharedPreferences

        alarmAdapter = new AlarmAdapter(this, alarms);
        listView.setAdapter(alarmAdapter);

        tpAlarm.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            jam = hourOfDay;
            min = minute;
        });

        bTimer.setOnClickListener(v -> {
            Clock alarm = new Clock(jam, min, true);
            alarms.add(alarm);
            saveAlarms(alarms); // Implement this method to save alarms in SharedPreferences
            alarmAdapter.notifyDataSetChanged();
            Toast.makeText(SetAlarm.this, "Alarm Set " + jam + " : " + min, Toast.LENGTH_SHORT).show();
            setTimer(alarm);
            notification();
        });
    }

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Reminders";
            String description = "Hey, Wake Up!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.setDescription(description);

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    private void setTimer(Clock alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent iTimer = new Intent(SetAlarm.this, MyReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(SetAlarm.this, ALARM_REQ_CODE, iTimer, PendingIntent.FLAG_IMMUTABLE);

        Calendar cal_alarm = Calendar.getInstance();
        cal_alarm.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        cal_alarm.set(Calendar.MINUTE, alarm.getMinute());
        cal_alarm.set(Calendar.SECOND, 0);

        Calendar cal_now = Calendar.getInstance();

        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Log.d("SetAlarm", "Set timer for " + cal_alarm.getTime());

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pi);
    }

    private List<Clock> loadAlarms() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("alarms", null);
        Type type = new TypeToken<List<Clock>>() {}.getType();

        if (json == null) {
            return new ArrayList<>();
        } else {
            return gson.fromJson(json, type);
        }
    }

    private void saveAlarms(List<Clock> alarms) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarms);
        editor.putString("alarms", json);
        editor.apply();
    }
}
