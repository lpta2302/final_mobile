package com.dev.mail.lpta2302.final_mobile.activities.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.friend.Friendship;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private final List<Friendship> friendsList;

    public FriendsAdapter(List<Friendship> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friendship friend = friendsList.get(position);
        User user = friend.getUser2();
        holder.textViewFriend.setText(user.getFullName());

        if(user.getAvatar() == null){
            holder.imageViewAvatar.setImageResource(R.drawable.ic_launcher_foreground);
        }else{
            Picasso.get().load(user.getAvatar()).into(holder.imageViewAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriend;
        ImageView imageViewAvatar;
        Context context;

        public FriendsViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            textViewFriend = itemView.findViewById(R.id.text_friend);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
        }
    }
}
