package com.example.empr.author;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.empr.R;
import com.example.empr.databinding.ActivityPdfAddBinding;

public class PdfEditActivity extends AppCompatActivity {

    // view binding
    private ActivityPdfAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_EMPr);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}