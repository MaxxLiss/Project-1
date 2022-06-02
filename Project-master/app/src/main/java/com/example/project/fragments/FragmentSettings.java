package com.example.project.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class FragmentSettings extends Fragment {

    private EditText et_user_device_name;
    private AppCompatCheckBox btn_background_scanning_permission;

    private View screen;

    private Activity activity;

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

        screen = inflater.inflate(R.layout.fragment_settings, container, false);
        et_user_device_name = screen.findViewById(R.id.et_user_device_name);
        btn_background_scanning_permission = screen.findViewById(R.id.btn_background_scanning_permission);

        return screen;
    }
}
