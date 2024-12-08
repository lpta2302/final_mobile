package com.dev.mail.lpta2302.final_mobile.activities.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.logic.friend.FriendService;
import com.dev.mail.lpta2302.final_mobile.logic.friend.FriendStatus;
import com.dev.mail.lpta2302.final_mobile.logic.friend.Friendship;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
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
        Friendship friendship = friendsList.get(position);
        User user = friendship.getUser2();

        // Load friend's name and avatar
        holder.textViewFriend.setText(user.getFullName());
        if (user.getAvatar() == null) {
            holder.imageViewAvatar.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Picasso.get().load(user.getAvatar()).into(holder.imageViewAvatar);
        }

        // Check if this user is already a friend in `AuthUser.getInstance().getFriends()`
        boolean isFriend = AuthUser.getInstance().getFriends().stream()
                .anyMatch(f -> f.getUser2().getId().equals(user.getId()));

        if (isFriend) {
            // Show accept and decline buttons if the status is pending
            if (friendship.getStatus() == FriendStatus.PENDING) {
                holder.acceptBtn.setVisibility(View.VISIBLE);
                holder.declineBtn.setVisibility(View.VISIBLE);

                holder.acceptBtn.setOnClickListener(v -> {
                    Toast.makeText(holder.context, "Accepted Friend Request", Toast.LENGTH_SHORT).show();
                    acceptFriendship(friendship);
                });

                holder.declineBtn.setOnClickListener(v -> {
                    Toast.makeText(holder.context, "Declined Friend Request", Toast.LENGTH_SHORT).show();
                    declineFriendship(friendship);
                });
            } else {
                // Hide buttons if no longer pending
                holder.acceptBtn.setVisibility(View.GONE);
                holder.declineBtn.setVisibility(View.GONE);
            }
        } else if(friendship.getUser2().getId().equals(AuthUser.getInstance().getUser().getId())) {
            // If not a friend, show only "Add Friend"
            holder.acceptBtn.setVisibility(View.GONE);
            holder.declineBtn.setVisibility(View.VISIBLE);
            holder.declineBtn.setText("Add");

            holder.declineBtn.setOnClickListener(v -> {
                Toast.makeText(holder.context, "Added Friend Request", Toast.LENGTH_SHORT).show();
                sendFriendRequest(user);
            });
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    private void acceptFriendship(Friendship friendship) {
        // Handle accepting logic here
        friendship.setStatus(FriendStatus.ACCEPTED);
        FriendService.getInstance().update(friendship, new QueryCallback<Void>() {
            @Override
            public void onSuccess(Void expectation) {
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    private void declineFriendship(Friendship friendship) {
        // Handle declining logic here
        friendsList.remove(friendship);
        friendship.setStatus(FriendStatus.DECLINED);
        FriendService.getInstance().update(friendship, new QueryCallback<Void>() {
            @Override
            public void onSuccess(Void expectation) {
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    private void sendFriendRequest(User user) {
        FriendService.getInstance().create(new Friendship(AuthUser.getInstance().getUser(), user, LocalDateTime.now(), FriendStatus.PENDING), new QueryCallback<String>() {
            @Override
            public void onSuccess(String expectation) {
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriend;
        ImageView imageViewAvatar;
        Button acceptBtn, declineBtn;
        Context context;

        public FriendsViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            textViewFriend = itemView.findViewById(R.id.text_friend);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            declineBtn = itemView.findViewById(R.id.declineBtn);
        }
    }
}
