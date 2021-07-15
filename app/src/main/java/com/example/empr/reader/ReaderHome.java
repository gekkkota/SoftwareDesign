package com.example.empr.reader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.empr.LogInAs;
import com.example.empr.R;
import com.google.firebase.auth.FirebaseAuth;

public class ReaderHome extends AppCompatActivity {

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_EMPr);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ReaderHome.this, LogInAs.class));
            }
        });
    }
}