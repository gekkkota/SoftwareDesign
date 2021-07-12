package com.example.empr.author;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empr.Home;
import com.example.empr.LogInAs;
import com.example.empr.R;
import com.example.empr.RegisterAs;
import com.example.empr.reader.Reader;
import com.example.empr.reader.ReaderLogin;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_login);
        setTheme(R.style.Theme_EMPr);

        userEmail = findViewById(R.id.email_author);
        userPass = findViewById(R.id.password_author);
        loginBtn = findViewById(R.id.login_author);
        toSignUp = findViewById(R.id.toSignUp_author);

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

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser reader = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Readers");
                    String userID = reader.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Author authorProfile = snapshot.getValue(Author.class);

                            if (authorProfile != null){
                                String userType = authorProfile.userType;
                                if(userType.equals("author")){
                                    Toast.makeText(AuthorLogin.this, "Reader successfully logged in!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(AuthorLogin.this, Home.class));
                                } else {
                                    Toast.makeText(AuthorLogin.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                }
            });
        });
    }
}