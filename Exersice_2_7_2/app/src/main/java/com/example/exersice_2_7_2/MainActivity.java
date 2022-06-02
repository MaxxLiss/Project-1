package com.example.exersice_2_7_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_enter;
    private TextView tv_ans;
    private EditText et_login, et_password;
    private ArrayList <ArrayList<String>> users;

    public int findUser(String find) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).get(0).equals(find)) {
                return i;
            }
        }
        return  -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                String login = data.getStringExtra("login");
                String password = data.getStringExtra("password");

                ArrayList <String> add = new ArrayList<>();
                add.add(login);
                add.add(password);

                users.add(add);

                et_login.setText(login);
                et_password.setText(password);
                tv_ans.setText(R.string.success);
                tv_ans.setTextColor(getResources().getColor(R.color.green));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_enter = findViewById(R.id.btn_enter);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        tv_ans = findViewById(R.id.tv_ans);
        users = new ArrayList<>();

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = et_login.getText().toString();
                String password = et_password.getText().toString();
                int ask = findUser(login);

                if (ask != -1 && password.equals(users.get(ask).get(1))) {
                    tv_ans.setText(R.string.right_password);
                    tv_ans.setTextColor(getResources().getColor(R.color.green));
                } else if (ask != -1 && !password.equals(users.get(ask).get(1))) {
                    tv_ans.setText(R.string.try_again);
                    tv_ans.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tv_ans.setText(R.string.wrong_password);
                    tv_ans.setTextColor(getResources().getColor(R.color.red));
                    Intent i = new Intent(MainActivity.this, UserRegistration.class);
                    i.putExtra("login", login);
                    i.putExtra("password", password);
                    startActivityForResult(i, 1);
                }

            }
        });
    }
}