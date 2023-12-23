package com.example.alarmclock30.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alarmclock30.R;
import com.example.alarmclock30.adapters.AlarmAdapter;
import com.example.alarmclock30.databinding.ActivitySetAlarmBinding;
import com.example.alarmclock30.entities.Clock;
import com.example.alarmclock30.receivers.AlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetAlarmActivity extends AppCompatActivity {

    private ActivitySetAlarmBinding binding;
    private List<Clock> alarms = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private AlarmManager alarmManager;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        ininitRecycleView();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        prefs = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        editor = prefs.edit();
    }

    private void ininitRecycleView() {
        alarmAdapter = new AlarmAdapter(alarms, this);
        loadAlarms();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvAlarms.setLayoutManager(linearLayoutManager);
        binding.rvAlarms.setAdapter(alarmAdapter);
    }

    private void controlAction() {
        binding.btnAddAlarm.setOnClickListener(v -> {
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();
            setAlarm(hour, minute);
        });

        alarmAdapter.setOnItemClickListener(position -> {
            Clock clock = alarms.get(position);
            clock.setEnabled(!clock.isEnabled());
            alarms.set(position, clock);
            String json2 = new Gson().toJson(alarms);
            editor.putString("Clocks", json2);
            editor.apply();
            if (!clock.isEnabled()) {
                cancelAlarm(clock);
            } else {
                setAlarm(clock.getHour(), clock.getMinute());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setTitle("Xóa báo thức")
                        .setMessage("Bạn có chắc chắn muốn xóa báo thức này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            if (alarms.get(position).isEnabled()) {
                                cancelAlarm(alarms.get(position));
                            }
                            alarms.remove(position);
                            alarmAdapter.setData(alarms);
                            String json2 = new Gson().toJson(alarms);
                            editor.putString("Clocks", json2);
                            editor.apply();
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            alarmAdapter.notifyItemChanged(position);
                        })
                        .show();
            }
        }).attachToRecyclerView(binding.rvAlarms);

    }

    private void setAlarm(int hour, int minute) {
        int id = (int) System.currentTimeMillis();
        Clock clock = new Clock(id,hour, minute, true, true);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            String timeText = String.format(Locale.getDefault(),"Đã đặt báo thức lúc %02d:%02d", hour, minute);
            Toast.makeText(this, timeText, Toast.LENGTH_SHORT).show();
        }
        this.alarms.add(clock);
        String json2 = new Gson().toJson(alarms);
        editor.putString("Clocks", json2);
        editor.apply();
        alarmAdapter.setData(alarms);
    }

    private void cancelAlarm(Clock clock) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, clock.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            String timeText = String.format(Locale.getDefault(),"Đã hủy báo thức lúc %02d:%02d", clock.getHour(), clock.getMinute());
            Toast.makeText(this, timeText, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAlarms() {
        String json = prefs.getString("Clocks", null);
        Type type = new TypeToken<ArrayList<Clock>>() {}.getType();
        alarms = new Gson().fromJson(json, type);
        if (alarms == null) {
            alarms = new ArrayList<>();
        }
        alarmAdapter.setData(alarms);
    }


}
