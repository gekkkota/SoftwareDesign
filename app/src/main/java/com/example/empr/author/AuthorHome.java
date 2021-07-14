package com.example.empr.author;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.empr.LogInAs;
import com.example.empr.R;
import com.example.empr.reader.ReaderHome;
import com.google.firebase.auth.FirebaseAuth;

public class AuthorHome extends AppCompatActivity {

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_EMPr);
        setContentView(R.layout.activity_author_home);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AuthorHome.this, LogInAs.class));
            }
        });
    }

    //handle click, start category add screen

}