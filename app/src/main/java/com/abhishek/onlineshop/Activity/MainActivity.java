package com.abhishek.onlineshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.abhishek.onlineshop.Adapter.CategoryAdapter;
import com.abhishek.onlineshop.Adapter.NotificationAdapter;
import com.abhishek.onlineshop.Adapter.PopularAdapter;
import com.abhishek.onlineshop.Adapter.SliderAdapter;
import com.abhishek.onlineshop.Domain.CategoryDomain;
import com.abhishek.onlineshop.Domain.ItemsDomain;
import com.abhishek.onlineshop.Domain.SliderItems;
import com.abhishek.onlineshop.R;
import com.abhishek.onlineshop.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity{
    private ActivityMainBinding binding;
    private Drawable defaultBackground;
    private Drawable darkerBackground;

    private ArrayList<ItemsDomain> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize the default and darker backgrounds
        defaultBackground = ContextCompat.getDrawable(this, R.drawable.default_background);
        darkerBackground = ContextCompat.getDrawable(this, R.drawable.darker_background);



        // Set explorer_btn to have a darker background by default
        binding.explorerBtn.setBackground(darkerBackground);
        initNotifyMe();
        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
        initSearch();

    }

    private void initNotifyMe() {
        binding.notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                finish();
            }
        });

        DatabaseReference notificationsRef = database.getReference("Notifications");

        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long notificationCount = snapshot.getChildrenCount();
                    binding.notificationCount.setText(String.valueOf(notificationCount));
                } else {
                    binding.notificationCount.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }


    private void initSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String newText) {
                ArrayList<ItemsDomain> filteredList = new ArrayList<>();
                for (ItemsDomain itemsDomain : items) {  // Use the class-level 'items' list
                    if (itemsDomain.getTitle().trim().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(itemsDomain);
                    }
                }
                // Update the RecyclerView with the filtered list
                binding.recyclerviewPopular.setAdapter(new PopularAdapter(filteredList));
            }
        });
    }

    private void bottomNavigation() {
        //added this one
        binding.explorerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonBackgrounds();
                binding.explorerBtn.setBackground(darkerBackground);
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonBackgrounds();
                binding.cartBtn.setBackground(darkerBackground);
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonBackgrounds();
                binding.profileBtn.setBackground(darkerBackground);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        binding.wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonBackgrounds();
                binding.wishlistBtn.setBackground(darkerBackground);
                startActivity(new Intent(MainActivity.this, WishlistActivity.class));

            }
        });
    }

    private void resetButtonBackgrounds() {
        binding.explorerBtn.setBackground(defaultBackground);
        binding.cartBtn.setBackground(defaultBackground);
        binding.profileBtn.setBackground(defaultBackground);
        binding.wishlistBtn.setBackground(defaultBackground);
    }

    private void initPopular() {
        DatabaseReference itemsRef = database.getReference("Items");
        DatabaseReference wishlistRef = database.getReference("Wishlist").child(FirebaseAuth.getInstance().getUid());
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    items.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        items.add(item);
                    }

                    wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot wishlistSnapshot) {
                            for (ItemsDomain item : items) {
                                if (wishlistSnapshot.hasChild(item.getTitle())) {
                                    item.setInWishlist(true);
                                } else {
                                    item.setInWishlist(false);
                                }
                            }

                            if (!items.isEmpty()) {
                                binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                                binding.recyclerviewPopular.setAdapter(new PopularAdapter(items));
                            }
                            binding.progressBarPopular.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBarPopular.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
            }
        });
    }



    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarOffical.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> categories = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categories.clear(); // Clear the list before adding new data
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        categories.add(issue.getValue(CategoryDomain.class));
                    }
                    if (!categories.isEmpty()) {
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(categories));
                    }
                    binding.progressBarOffical.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> banners = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    banners.clear(); // Clear the list before adding new data
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        banners.add(issue.getValue(SliderItems.class));
                    }
                    banner(banners);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banner(ArrayList<SliderItems> banners) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(banners, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }


}
