package com.dev.mail.lpta2302.final_mobile.activities.friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private final List<Friend> friendsList;

    public FriendsAdapter(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.textViewFriend.setText(friend.getName());
        holder.imageViewAvatar.setImageResource(friend.getAvatarResId());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriend;
        ImageView imageViewAvatar;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFriend = itemView.findViewById(R.id.text_friend);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
        }
    }
}
