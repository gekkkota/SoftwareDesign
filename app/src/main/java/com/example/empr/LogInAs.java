package com.example.empr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.empr.author.AuthorLogin;
import com.example.empr.reader.ReaderLogin;

public class LogInAs extends AppCompatActivity {

    private ImageView authorBtn, readerBtn;
    private TextView toRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);
        setTheme(R.style.Theme_EMPr);

        authorBtn = findViewById(R.id.author_btn);
        readerBtn = findViewById(R.id.reader_btn);
        toRegister = findViewById(R.id.register);

        toRegister.setOnClickListener(v -> {
            startActivity(new Intent(LogInAs.this, RegisterAs.class));
            finish();
        });

        authorBtn.setOnClickListener(v -> {
            startActivity(new Intent(LogInAs.this, AuthorLogin.class));
        });

        readerBtn.setOnClickListener(v -> {
            startActivity(new Intent(LogInAs.this, ReaderLogin.class));
        });
    }
}