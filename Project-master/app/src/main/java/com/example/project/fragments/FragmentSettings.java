package com.example.project.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.SendInfo;

public class FragmentSettings extends Fragment {

    private EditText et_user_device_name;
    private AppCompatCheckBox btn_background_scanning_permission;

    private String deviceName;

    private View screen;

    private Activity activity;

    public FragmentSettings(String deviceName) {
        this.deviceName = deviceName;
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

        screen = inflater.inflate(R.layout.fragment_settings, container, false);
        et_user_device_name = screen.findViewById(R.id.et_user_device_name);
        btn_background_scanning_permission = screen.findViewById(R.id.btn_background_scanning_permission);

        et_user_device_name.setText(deviceName);

        et_user_device_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().matches(MainActivity.FIND_MUSIC_APP + "(.*)")) {
                    et_user_device_name.setText("");
                    Toast.makeText(getActivity(), "Используйте другое имя", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                et_user_device_name.setText(editable);
                ((SendInfo) activity).sendNewDeviceName(editable.toString());

            }
        });
        return screen;
    }
}
