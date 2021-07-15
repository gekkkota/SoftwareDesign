package com.example.empr.author;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.empr.R;
import com.google.firebase.auth.FirebaseAuth;

public class PdfAddActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    // uri of picked pdf
    private Uri pdfUri = null;
    public static final int PDF_PICK_CODE = 1000;

    private ImageButton backBtn, attachBtn;
    private TextView categoryTv;

    // TAG for debugging;
    public static final String TAG = "ADD_PDF_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_add);
        setTheme(R.style.Theme_EMPr);

        firebaseAuth = FirebaseAuth.getInstance();
        loadPdfCategories();

        // go to previous activity
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // attach pdf
        attachBtn = findViewById(R.id.attachBtn);
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
            }
        });

        // choose category
        categoryTv = findViewById(R.id.categoryTv);
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });

    }

    private void loadPdfCategories() {
        Log.d(TAG, "LoadPdfCategories: Loading pdf categories...");
    }

    private void categoryPickDialog() {
        // get categories from firebase
    }

    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == PDF_PICK_CODE){
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();

                Log.d(TAG, "onActivityResult: URI:" + pdfUri);
            }
        } else {
            Log.d(TAG, "onActivityResult: cancelled picking pdf");
            Toast.makeText(this, "Cancelled picking pdf", Toast.LENGTH_LONG).show();
        }
    }
}