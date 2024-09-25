package com.abhishek.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abhishek.onlineshop.Adapter.NotificationAdapter;
import com.abhishek.onlineshop.Domain.NotificationDomain;
import com.abhishek.onlineshop.databinding.ActivityMainBinding;
import com.abhishek.onlineshop.databinding.ActivityNotificationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity {
    ActivityNotificationBinding binding;
    ArrayList<NotificationDomain> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNotification();
        initBack();
    }

    private void initBack() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    private void initNotification() {
        DatabaseReference myRef = database.getReference("Notifications");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    items.clear();
                    for (DataSnapshot issue : snapshot.getChildren()){
                        items.add(issue.getValue(NotificationDomain.class));
                    }

                    if(!items.isEmpty()){
                        binding.recyclerViewNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,
                                LinearLayoutManager.VERTICAL,false));
                        binding.recyclerViewNotification.setAdapter(new NotificationAdapter(items));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}