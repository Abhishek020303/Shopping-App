package com.abhishek.onlineshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.abhishek.onlineshop.R;
import com.abhishek.onlineshop.databinding.ActivityOrderPlacedBinding;

public class OrderPlacedActivity extends BaseActivity {
    ActivityOrderPlacedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String cost =intent.getStringExtra("amount");
        binding.totalAmt.setText(cost);

        binding.button.setOnClickListener(view -> {
            startActivity(new Intent(OrderPlacedActivity.this,MainActivity.class));
            finish();
        });

    }
}