package com.example.empr.author;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.empr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private Button submitBtn;
    private EditText categoryET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        setTheme(R.style.Theme_EMPr);

        firebaseAuth = FirebaseAuth.getInstance();
        submitBtn = findViewById(R.id.submitBtn);
        categoryET = findViewById(R.id.categoryEt);
        progressBar = findViewById(R.id.progressBar);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String category = "";

    private void validateData() {
        // Before adding validate data

        //get data
        category = categoryET.getText().toString().trim();

        //validate if not empty
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Please enter category!", Toast.LENGTH_LONG).show();
        } else {
            addCategoryFirebase();
        }

    }

    private void addCategoryFirebase() {
        // show progress
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Adding category...", Toast.LENGTH_LONG).show();

        // get timestamp
        long timestamp = System.currentTimeMillis();

        // setup info to add in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", timestamp);
        hashMap.put("category", category);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", firebaseAuth.getUid());

        // add to firebase db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // category add success
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CategoryAddActivity.this, "Category added successfully!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        // category add failed
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CategoryAddActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}