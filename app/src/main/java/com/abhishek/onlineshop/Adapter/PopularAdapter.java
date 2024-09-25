package com.abhishek.onlineshop.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.onlineshop.Activity.DetailActivity;
import com.abhishek.onlineshop.Activity.WishlistActivity;
import com.abhishek.onlineshop.Domain.ItemsDomain;
import com.abhishek.onlineshop.R;
import com.abhishek.onlineshop.databinding.ViewholderPopListBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {

    ArrayList<ItemsDomain> items;
    Context context;
    OnWishlistChangeListener wishlistChangeListener;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    public PopularAdapter(ArrayList<ItemsDomain> items, OnWishlistChangeListener wishlistChangeListener) {
        this.items = items;
        this.wishlistChangeListener = wishlistChangeListener;
    }
    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder,int position) {

        ItemsDomain item = items.get(holder.getAdapterPosition());


        holder.binding.titleTxt.setText(items.get(position).getTitle());
        holder.binding.reviewTxt.setText(""+items.get(position).getReview());
        holder.binding.priceTxt.setText("$"+items.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + items.get(position).getRating()+")");
        holder.binding.OldPriceTxt.setText("$"+items.get(position).getOldPrice());
        holder.binding.OldPriceTxt.setPaintFlags(holder.binding.OldPriceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        holder.binding.checkBox.setOnCheckedChangeListener(null);

        if (item.isInWishlist()) {
            holder.binding.checkBox.setChecked(true);
            holder.binding.checkBox.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_filled));
        } else {
            holder.binding.checkBox.setChecked(false);
            holder.binding.checkBox.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_outlined));
        }

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("Wishlist")
                        .child(FirebaseAuth.getInstance().getUid()).child(item.getTitle());
                if(isChecked){
                    holder.binding.checkBox.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_filled));
                    Toast.makeText(context.getApplicationContext(), "Item added to Wishlist",Toast.LENGTH_SHORT).show();
                    item.setInWishlist(true);
                    wishlistRef.setValue(item);


                }
                else{
                    holder.binding.checkBox.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_outlined));
                    Toast.makeText(context.getApplicationContext(), "Item removed to Wishlist",Toast.LENGTH_SHORT).show();
                    item.setInWishlist(false);
                    wishlistRef.removeValue();

                    if (wishlistChangeListener != null) {
                        wishlistChangeListener.onItemRemoved(item);
                    }


                }
            }


        });


        holder.binding.ratingBar.setRating((float) items.get(position).getRating());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;
        public Viewholder(ViewholderPopListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnWishlistChangeListener {
        void onItemRemoved(ItemsDomain item);
    }




}
