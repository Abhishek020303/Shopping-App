package com.abhishek.onlineshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.onlineshop.Domain.NotificationDomain;
import com.abhishek.onlineshop.databinding.ViewholderNotificationBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder> {

    ArrayList<NotificationDomain> items;
    Context context;

    public NotificationAdapter(ArrayList<NotificationDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public NotificationAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderNotificationBinding binding = ViewholderNotificationBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.Viewholder holder, int position) {
        holder.binding.dateTxt.setText(""+items.get(position).getDate());
        holder.binding.titleTxt.setText(""+items.get(position).getTitle());
        holder.binding.messageTxt.setText(""+items.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderNotificationBinding binding;
        public Viewholder(ViewholderNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
