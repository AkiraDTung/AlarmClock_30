package com.example.alarmclock30.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmclock30.entities.Clock;
import com.example.alarmclock30.R;

import java.util.List;
import java.util.Locale;

public class AlarmAdapter  extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<Clock> clocks;
    private Context context;

    public AlarmAdapter(List<Clock> clocks, Context context) {
        this.clocks = clocks;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {
        Clock clock = clocks.get(position);
        holder.time.setText(String.format(Locale.getDefault(),"%02d:%02d", clock.getHour(), clock.getMinute()));
        holder.switchCompat.setChecked(clock.isEnabled());
        holder.switchCompat.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (clocks != null) {
            return clocks.size();
        }
        return 0;
    }

    public void setData(List<Clock> clocks) {
        this.clocks.clear();
        this.clocks.addAll(clocks);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        SwitchCompat switchCompat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tvTime);
            switchCompat = itemView.findViewById(R.id.btnSwitch);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}