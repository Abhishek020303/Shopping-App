package com.abhishek.onlineshop.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Size;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.onlineshop.Adapter.SizeAdapter;
import com.abhishek.onlineshop.Adapter.SliderAdapter;
import com.abhishek.onlineshop.Domain.ItemsDomain;
import com.abhishek.onlineshop.Domain.SliderItems;
import com.abhishek.onlineshop.Fragment.DescriptionFragment;
import com.abhishek.onlineshop.Fragment.ReviewFragment;
import com.abhishek.onlineshop.Fragment.SoldFragment;
import com.abhishek.onlineshop.Helper.ManagmentCart;
import com.abhishek.onlineshop.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private ItemsDomain object;
    private int numberOrder =1;
    private ManagmentCart managementCart;
    private Handler slideHandle = new Handler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());

        managementCart = new ManagmentCart(this);
        getBundles();
        initBanners();
        initSize();
        setupViewPager();
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void initBanners(){
        ArrayList <SliderItems> sliderItems = new ArrayList<>();
        for(int i=0 ; i  < object.getPicUrl().size(); i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewpageSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object  = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating()+" Rating");

        binding.addTocartBtn.setOnClickListener(view -> {
            String selectedSize = ((SizeAdapter) binding.recyclerSize.getAdapter()).getSelectedSize();
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
            } else {
                object.setNumberinCart(numberOrder);
                object.setSelectedSize(selectedSize); // Assuming ItemsDomain has a setSelectedSize method
                managementCart.insertFood(object);
            }
            object.setNumberinCart(numberOrder);
            managementCart.insertFood(object);
        });

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

    }

    private void setupViewPager(){
        ViewpagerAdapter adapter = new ViewpagerAdapter (getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle  bundle1 = new Bundle();
        Bundle  bundle2 = new Bundle();
        Bundle  bundle3 = new Bundle();

        bundle1.putString("description",object.getDescription());


        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Description");
        adapter.addFrag(tab2,"Reviews");
        adapter.addFrag(tab3,"Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }


    private class ViewpagerAdapter extends FragmentPagerAdapter{

        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewpagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment ,String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}