package com.example.empr.author;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empr.R;
import com.example.empr.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AuthorRegister extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference reference;

    private EditText userFullName, userEmail, userPassword, userConfPass;
    private Button registerBtn;
    private TextView toLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_register);
        setTheme(R.style.Theme_EMPr);

        userFullName = findViewById(R.id.fullnameReg_author);
        userEmail = findViewById(R.id.emailReg_author);
        userPassword = findViewById(R.id.passwordReg_author);
        userConfPass = findViewById(R.id.confirmPassReg_author);
        registerBtn = findViewById(R.id.registerBtn_author);
        toLogin = findViewById(R.id.toLogin_author);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(v -> {
            startActivity(new Intent(AuthorRegister.this, AuthorLogin.class));
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = userFullName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                String confirmPass = userConfPass.getText().toString().trim();

                String userType = "author";

                if (TextUtils.isEmpty(fullName)){
                    userFullName.setError("Full Name is required!");
                    userFullName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    userEmail.setError("Email is required!");
                    userEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userEmail.setError("Email ID is invalid!");
                    userEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    userPassword.setError("Password is required!");
                    userPassword.requestFocus();
                    return;
                }

                if (password.length() < 6){
                    userPassword.setError("Password must be 6 characters long!");
                    userPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPass)){
                    userConfPass.setError("Passwords must match!");
                    userConfPass.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                User user = new User(fullName, email, userType);

                db = FirebaseDatabase.getInstance();
                reference = db.getReference("Users");
                reference.child(email).setValue(user);
            }
        });
    }
}