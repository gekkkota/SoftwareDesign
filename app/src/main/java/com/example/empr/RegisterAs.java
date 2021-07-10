package com.example.empr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.empr.author.AuthorRegister;
import com.example.empr.reader.ReaderRegister;

public class RegisterAs extends AppCompatActivity {

    private ImageView authorBtn, readerBtn;
    private TextView toLogInAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);
        setTheme(R.style.Theme_EMPr);

        authorBtn = findViewById(R.id.author_btn_signUp);
        readerBtn = findViewById(R.id.reader_btn_signUp);
        toLogInAs = findViewById(R.id.signup_btn);

        toLogInAs.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAs.this, LogInAs.class));
            finish();
        });

        authorBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAs.this, AuthorRegister.class));
        });

        readerBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAs.this, ReaderRegister.class));
        });
    }
}