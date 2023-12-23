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
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmclock30.entities.Clock;
import com.example.alarmclock30.R;

import java.util.List;

public class AlarmAdapter  extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private List<Clock> clocks;
    private Context context;

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {
        Clock clock = clocks.get(position);
    }

    @Override
    public int getItemCount() {
        if (clocks != null) {
            return clocks.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, amount;
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //name = itemView.findViewById(R.id.tvAccountName);
            //amount = itemView.findViewById(R.id.tvAccountAmount);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}