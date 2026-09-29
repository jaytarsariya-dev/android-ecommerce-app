package com.example.androidminorproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidminorproject.databinding.ItemTeamMemberBinding;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private final List<TeamMember> teamMembers;

    public TeamAdapter(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamMemberBinding binding = ItemTeamMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new TeamViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        TeamMember member = teamMembers.get(position);
        holder.bind(member);
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {
        private final ItemTeamMemberBinding binding;

        public TeamViewHolder(@NonNull ItemTeamMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TeamMember member) {
            binding.tvName.setText(member.getName());
            binding.tvRole.setText(member.getRole());
            binding.ivPhoto.setImageResource(member.getPhotoResId());
        }
    }
}
