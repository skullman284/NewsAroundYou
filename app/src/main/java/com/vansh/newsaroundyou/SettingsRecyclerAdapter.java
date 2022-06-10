package com.vansh.newsaroundyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    private Context context;
    private ViewHolder.OnNoteListener onNoteListener;
    private final static String[] SETTINGSARRAY = new String[] {"Region", "Categories", "Clear Search History", "Sign Out"};

    public SettingsRecyclerAdapter(Context context, ViewHolder.OnNoteListener onNoteListener) {
        this.context = context;
        this.onNoteListener = onNoteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvSettings;
        private OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            //hooks
            tvSettings = itemView.findViewById(R.id.tv_settings);
            this.onNoteListener = onNoteListener;
            //setting the list items on click listener
            itemView.setOnClickListener(this);
        }

        public TextView getTvSettings() {
            return tvSettings;
        }

        @Override
        public void onClick(View view) {
            this.onNoteListener.onNoteClick(getAdapterPosition());
        }


        public interface OnNoteListener{
            void onNoteClick(int position);
        }

    }

    @NonNull
    @Override
    public SettingsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_settings_card, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsRecyclerAdapter.ViewHolder holder, int position) {

        TextView tvSettings = holder.getTvSettings();
        tvSettings.setText(SETTINGSARRAY[position]);

    }

    @Override
    public int getItemCount() {
        return SETTINGSARRAY.length;
    }
}
