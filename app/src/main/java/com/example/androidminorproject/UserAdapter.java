package com.example.androidminorproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<String> emailList;
    private OnRemoveClickListener onRemoveClickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(String email);
    }

    public UserAdapter(List<String> emailList, OnRemoveClickListener onRemoveClickListener) {
        this.emailList = emailList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        String email = emailList.get(position);
        holder.emailTextView.setText(email);

        holder.removeButton.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(email);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        Button removeButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            removeButton = itemView.findViewById(R.id.actionButton);
        }
    }
}
