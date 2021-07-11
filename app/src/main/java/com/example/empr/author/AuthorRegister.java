package com.example.empr.author;

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
import com.example.empr.reader.Reader;
import com.example.empr.reader.ReaderLogin;
import com.example.empr.reader.ReaderRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userFullName, userEmail, userPassword, userConfPass;
    private Button registerBtn;
    private TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_register);
        setTheme(R.style.Theme_EMPr);

        userFullName = findViewById(R.id.fullnameReg_author);
        userEmail    = findViewById(R.id.emailReg_author);
        userPassword = findViewById(R.id.passwordReg_author);
        userConfPass = findViewById(R.id.confirmPassReg_author);
        registerBtn  = findViewById(R.id.registerBtn_author);
        toLogin      = findViewById(R.id.toLogin_author);

        mAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(v -> {
            startActivity(new Intent(AuthorRegister.this, LogInAs.class));
        });

        registerBtn.setOnClickListener(v -> {
            String fullname = userFullName.getText().toString().trim();
            String email = userEmail.getText().toString().trim();
            String password = userPassword.getText().toString().trim();
            String confirmPass = userConfPass.getText().toString().trim();

            String userType = "author";

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
                    Author author = new Author(fullname, email, userType);

                    FirebaseDatabase.getInstance().getReference("Reader")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(author)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    Toast.makeText(AuthorRegister.this, "Reader account has been created!", Toast.LENGTH_LONG).show();

                                    // redirect to Reader Login
                                    Intent intent = new Intent(AuthorRegister.this, AuthorLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AuthorRegister.this, "Error: " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(AuthorRegister.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}