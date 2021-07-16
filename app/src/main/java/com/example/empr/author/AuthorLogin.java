package com.example.empr.author;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empr.User;
import com.example.empr.reader.ReaderHome;
import com.example.empr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AuthorLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userEmail, userPass;
    private Button loginBtn;
    private TextView toSignUp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_login);
        setTheme(R.style.Theme_EMPr);

        userEmail = findViewById(R.id.email_author);
        userPass = findViewById(R.id.password_author);
        loginBtn = findViewById(R.id.login_author);
        toSignUp = findViewById(R.id.toSignUp_author);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        toSignUp.setOnClickListener(v -> {
            startActivity(new Intent(AuthorLogin.this, AuthorRegister.class));
        });

        loginBtn.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            String password = userPass.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                userEmail.setError("Email is required!");
                userEmail.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(password)){
                userPass.setError("Password is required!");
                userPass.requestFocus();
                return;
            }

            if(password.length() < 6){
                userPass.setError("Password must be 6 characters long!");
                userPass.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser author = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    String userID = author.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            if (userProfile != null){
                                String userType = userProfile.userType;
                                if(userType.equals("author")){

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if(user.isEmailVerified()){
                                        Toast.makeText(AuthorLogin.this, "Author successfully logged in!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        // redirect to home
                                        Intent intent = new Intent(AuthorLogin.this, AuthorHome.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        user.sendEmailVerification();
                                        Toast.makeText(AuthorLogin.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(AuthorLogin.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AuthorLogin.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }
}