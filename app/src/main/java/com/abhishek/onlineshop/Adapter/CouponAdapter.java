package com.abhishek.onlineshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.onlineshop.R;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private List<Coupon> coupons;
    private Context context;

    public CouponAdapter(List<Coupon> coupons, Context context) {
        this.coupons = coupons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coupon coupon = coupons.get(position);
        holder.couponTextView.setText(coupon.getText());
        holder.couponCodeTextView.setText(coupon.getCode());
        holder.couponCompareTextView.setText(coupon.getCompare());

    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView couponTextView;
        TextView couponCodeTextView;
        TextView couponCompareTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponTextView = itemView.findViewById(R.id.couponTextView);
            couponCodeTextView = itemView.findViewById(R.id.couponCodeTextView);
            couponCompareTextView = itemView.findViewById(R.id.couponCompareTextView);
        }
    }


    public static class Coupon {
        private String text;
        private String code;
        private String compare;

        public Coupon(String text, String code, String compare) {
            this.text = text;
            this.code = code;
            this.compare = compare;
        }

        public String getText() {
            return text;
        }

        public String getCode() {
            return code;
        }

        public String getCompare() {
            return compare;
        }
    }
}
