package com.example.empr.reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empr.LogInAs;
import com.example.empr.R;
import com.example.empr.author.Author;
import com.example.empr.author.AuthorRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class ReaderRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userFullName, userEmail, userPassword, userConfPass;
    private Button registerBtn;
    private TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_register);
        setTheme(R.style.Theme_EMPr);

        userFullName = findViewById(R.id.fullnameReg_reader);
        userEmail    = findViewById(R.id.emailReg_reader);
        userPassword = findViewById(R.id.passwordReg_reader);
        userConfPass = findViewById(R.id.confirmPassReg_reader);
        registerBtn  = findViewById(R.id.registerBtn_reader);
        toLogin      = findViewById(R.id.toLogin_reader);

        mAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(v -> {
            startActivity(new Intent(ReaderRegister.this, LogInAs.class));
        });

        registerBtn.setOnClickListener(v -> {
            String fullname = userFullName.getText().toString().trim();
            String email = userEmail.getText().toString().trim();
            String password = userPassword.getText().toString().trim();
            String confirmPass = userConfPass.getText().toString().trim();

            String userType = "reader";

            if (TextUtils.isEmpty(fullname)){
                userFullName.setError("Full Name is required!");
                userFullName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)){
                userEmail.setError("Full Name is required!");
                userEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                userEmail.setError("Email ID is invalid!");
                userEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)){
                userPassword.setError("Full Name is required!");
                userPassword.requestFocus();
                return;
            }

            if (password.length() < 6){
                userPassword.setError("Password must be 6 characters long!");
                userPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confirmPass)){
                userConfPass.setError("Full Name is required!");
                userConfPass.requestFocus();
                return;
            }

            if (!password.equals(confirmPass)){
                userConfPass.setError("Passwords must match!");
                userConfPass.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Reader reader = new Reader(fullname, email, userType);

                    FirebaseDatabase.getInstance().getReference("Readers")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(reader)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    Toast.makeText(ReaderRegister.this, "Reader account has been created!", Toast.LENGTH_LONG).show();

                                    // redirect to Reader Login
                                    Intent intent = new Intent(ReaderRegister.this, ReaderLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ReaderRegister.this, "Error: " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(ReaderRegister.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

}