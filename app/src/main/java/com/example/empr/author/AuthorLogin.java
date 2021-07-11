package com.example.empr.author;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.empr.R;
import com.example.empr.RegisterAs;
import com.example.empr.reader.ReaderLogin;

public class AuthorLogin extends AppCompatActivity {

    private EditText userEmail, userPass;
    private Button loginBtn;
    private TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_login);
        setTheme(R.style.Theme_EMPr);

        userEmail = findViewById(R.id.email_author);
        userPass = findViewById(R.id.password_author);
        loginBtn = findViewById(R.id.login_author);
        toSignUp = findViewById(R.id.toSignUp_author);

        toSignUp.setOnClickListener(v -> {
            startActivity(new Intent(AuthorLogin.this, RegisterAs.class));
        });

        loginBtn.setOnClickListener(v -> {

        });
    }
}