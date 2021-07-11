package com.example.empr.reader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.empr.R;
import com.example.empr.RegisterAs;

public class ReaderLogin extends AppCompatActivity {

    private EditText userEmail, userPass;
    private Button loginBtn;
    private TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_login);
        setTheme(R.style.Theme_EMPr);

        userEmail = findViewById(R.id.email_reader);
        userPass = findViewById(R.id.password_reader);
        loginBtn = findViewById(R.id.login_reader);
        toSignUp = findViewById(R.id.toSignUp_reader);

        toSignUp.setOnClickListener(v -> {
            startActivity(new Intent(ReaderLogin.this, RegisterAs.class));
        });

        loginBtn.setOnClickListener(v -> {

        });
    }
}