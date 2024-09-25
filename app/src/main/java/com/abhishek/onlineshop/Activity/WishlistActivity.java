package com.abhishek.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.onlineshop.Adapter.PopularAdapter;
import com.abhishek.onlineshop.Domain.ItemsDomain;
import com.abhishek.onlineshop.R;
import com.abhishek.onlineshop.databinding.ActivityWishlistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WishlistActivity extends BaseActivity implements PopularAdapter.OnWishlistChangeListener{
    ActivityWishlistBinding binding;
    ArrayList<ItemsDomain> wishlistItems = new ArrayList<>();
    private PopularAdapter popularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBack();
        initWishlist();
        loadWishlistItems();


    }

    private void initBack() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishlistActivity.this,MainActivity.class));
            }
        });
    }

    private void initWishlist() {
        popularAdapter = new PopularAdapter(wishlistItems,this);
        binding.recyclerViewWishlist.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerViewWishlist.setAdapter(popularAdapter);
    }
    private void loadWishlistItems() {
        // Load wishlist items from the database or shared preferences
        // This is just a placeholder, actual loading logic should go here

        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("Wishlist").child(FirebaseAuth.getInstance().getUid());

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wishlistItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ItemsDomain item = itemSnapshot.getValue(ItemsDomain.class);
                    if (item != null) {
                        wishlistItems.add(item);
                    }
                }
                popularAdapter.notifyDataSetChanged();
                updateEmptyState();
                binding.progressBarWishlist.setVisibility(View.GONE);

                if (wishlistItems.isEmpty()) {
                    binding.emptyStateTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyStateTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors here
                binding.progressBarWishlist.setVisibility(View.GONE);
            }
        });
    }

    private void updateEmptyState() {
        binding.progressBarWishlist.setVisibility(View.GONE);
        if (wishlistItems.isEmpty()) {
            binding.emptyStateTextView.setVisibility(View.VISIBLE);
            binding.recyclerViewWishlist.setVisibility(View.GONE);
        } else {
            binding.emptyStateTextView.setVisibility(View.GONE);
            binding.recyclerViewWishlist.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemRemoved(ItemsDomain item) {
        wishlistItems.remove(item);
        popularAdapter.notifyDataSetChanged();

        updateEmptyState();
    }
}
