package com.example.exersice_2_7_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserRegistration extends AppCompatActivity {

    private EditText et_login_reg, et_password_reg;
    private AppCompatButton btn_reg;
    protected String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        et_login_reg = findViewById(R.id.et_login_reg);
        et_password_reg = findViewById(R.id.et_password_reg);
        btn_reg = findViewById(R.id.btn_reg);

        login = getIntent().getStringExtra("login");
        password = getIntent().getStringExtra("password");

        et_login_reg.setText(login);
        et_password_reg.setText(password);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = et_login_reg.getText().toString();
                password = et_password_reg.getText().toString();

                Intent i = new Intent();
                i.putExtra("login", login);
                i.putExtra("password", password);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }


}