package com.example.empr.reader;

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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class ReaderRegister extends AppCompatActivity {

    FirebaseAuth mAuth;

    private EditText userFullName, userEmail, userPassword, userConfPass;
    private Button registerBtn;
    private TextView toLogin;
    private ProgressBar progressBar;

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

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(v -> {
            startActivity(new Intent(ReaderRegister.this, ReaderLogin.class));
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(fullname, email, userType);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ReaderRegister.this, "Author account has been created!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        // redirect to Reader Login
                                        Intent intent = new Intent(ReaderRegister.this, ReaderLogin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ReaderRegister.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ReaderRegister.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
