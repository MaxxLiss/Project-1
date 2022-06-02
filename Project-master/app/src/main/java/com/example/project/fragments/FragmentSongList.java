package com.example.project.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.FindUser;
import com.example.project.FindUserAdapter;
import com.example.project.FindUserAdapter.OnItemClickListener;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentSongList extends Fragment {

    private RecyclerView rv_songs_list;
    private FindUserAdapter adapter;

    private ArrayList<FindUser> findUserList;

    private View screen;

    private TextView tv_user_track_name;

    private Activity activity;

    public FragmentSongList (ArrayList<FindUser> findUserList) {
        this.findUserList = findUserList;
    }

    public FragmentSongList () {}

    public void updateSongList (ArrayList<FindUser> findUserList) {
        this.findUserList = findUserList;
        adapter = new FindUserAdapter(getActivity(), findUserList);
        rv_songs_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new SongListOnItemClickListener());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        screen = inflater.inflate(R.layout.fragment_songs_list, container, false);

        rv_songs_list = screen.findViewById(R.id.rv_songs_list);

        rv_songs_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FindUserAdapter(getActivity(), findUserList);
        rv_songs_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new SongListOnItemClickListener());

        return screen;
    }

    private class SongListOnItemClickListener implements OnItemClickListener {
        @Override
        public void OnItemClick(int position) {
            try {
                ((SendInfoFromFragment) activity).sendNumber(position);
            } catch (ClassCastException exception) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
