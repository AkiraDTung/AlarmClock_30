package com.example.alarmclock30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.alarmclock30.Clock;
import com.example.alarmclock30.R;

import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Clock> {
    public AlarmAdapter(Context context, List<Clock> alarms) {
        super(context, 0, alarms);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        Clock alarm = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.switch_alarm, parent, false);
        }

        TextView timeTV = convertView.findViewById(R.id.tvTime);
        Switch switchButton = convertView.findViewById(R.id.onoffswitch);

        timeTV.setText(alarm.getHour() + " : " + alarm.getMinute());

        switchButton.setChecked(alarm.isEnabled());
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Clock alarm1 = getItem(position);
            alarm1.setEnabled(isChecked);
        });

        return convertView;
    }
}