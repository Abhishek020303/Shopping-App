package com.abhishek.onlineshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.abhishek.onlineshop.Adapter.CartAdapter;
import com.abhishek.onlineshop.Adapter.CouponAdapter;
import com.abhishek.onlineshop.Helper.ManagmentCart;
import com.abhishek.onlineshop.R;
import com.abhishek.onlineshop.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends MainActivity {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private ArrayList<CouponAdapter.Coupon> availableCoupons;
    private CouponAdapter couponAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        availableCoupons = new ArrayList<>();

        availableCoupons.add(new CouponAdapter.Coupon("30% off upto $20", "USE TRYNEW  | ABOVE ", "$20"));
        availableCoupons.add(new CouponAdapter.Coupon("20% off on orders over $50", "USE SAVE20  | ABOVE ", "$30"));
        availableCoupons.add(new CouponAdapter.Coupon("Flat $10 off ", "USE DHELA10  | ABOVE ", "$25"));
        couponAdapter = new CouponAdapter(availableCoupons, this);

        initCartList();
        initCoupons();
        calculatorCart();
        setVariable();
    }

    private void initCoupons() {

        binding.availableCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.availableCouponsRecyclerView.setAdapter(couponAdapter);
        binding.availableCouponsRecyclerView.setVisibility(View.VISIBLE);



        binding.couponCodeBtn.setOnClickListener(view -> {
            String coup = binding.myCouponCode.getText().toString().trim();
            double discount =0;
            if(coup.equals("TRYNEW") || coup.equals("SAVE20") || coup.equals("DHELA10")) {
                discount = 10;
                Toast.makeText(this, "Coupon Applied.", Toast.LENGTH_SHORT).show();
                binding.couponCodeBtn.setText("Applied");
                binding.textView15.setVisibility(View.VISIBLE);
                binding.discountTxt.setText("$"+discount);
                binding.discountTxt.setVisibility(View.VISIBLE);
                calculatorCart(discount);

            }else {

                Toast.makeText(this, "Invalid Coupon Code", Toast.LENGTH_SHORT).show();
                binding.discountTxt.setVisibility(View.GONE);
                binding.textView15.setVisibility(View.GONE);
            }

            binding.myCouponCode.clearFocus();
            // this is to remove the soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        });
    }


    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> {
            calculatorCart();
            initCartList();
        }));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
            finish();
        });
        binding.checkOutBtn.setOnClickListener(view -> {
            String cost = binding.totalTxt.getText().toString().trim();
            Intent intent = new Intent(CartActivity.this,OrderPlacedActivity.class);
            intent.putExtra("amount",cost);
            startActivity(intent);
        });
    }

    private void calculatorCart(double discount) {
        double percentTax = 0.02;
        double delivery = managmentCart.getListCart().isEmpty() ? 0.00 : 10.00;

        tax = Math.round((managmentCart.getTotalFee() * 100.0 * percentTax)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery-discount) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
    private void calculatorCart() {
        calculatorCart(0);
    }
}
