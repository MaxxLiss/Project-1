package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.fragments.SendInfoFromFragment;

import java.util.List;

public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<FindUser> findUsers;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public FindUserAdapter(Context context, List<FindUser> findUsers) {
        this.inflater = LayoutInflater.from(context);
        this.findUsers = findUsers;
    }

    @NonNull
    @Override
    public FindUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FindUserAdapter.ViewHolder holder, int position) {

        FindUser findUser = findUsers.get(position);
        holder.tv_find_user_song_name.setText(findUser.getSongName());
        holder.tv_find_user_device_name.setText(findUser.getDeviceName());

        holder.address = findUser.getAddress();
        holder.findUserDeviceName = findUser.getDeviceName();
    }

    @Override
    public int getItemCount() {
        return findUsers.size();
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public List<FindUser> getFindUsers() {
        return findUsers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_find_user_device_name;
        private TextView tv_find_user_song_name;

        private String address;
        private String findUserDeviceName;

        public String getAddress() {
            return address;
        }

        public String getFindUserDeviceName() {
            return findUserDeviceName;
        }

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            this.tv_find_user_device_name = itemView.findViewById(R.id.tv_find_user_device_name);
            this.tv_find_user_song_name = itemView.findViewById(R.id.tv_find_user_song_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
