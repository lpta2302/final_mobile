package com.dev.mail.lpta2302.final_mobile.activities.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.logic.notification.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>{
    private final List<Notification> notificationList;

    public NotificationsAdapter(List<Notification> notificationList) { this.notificationList = notificationList;}

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.textViewNotification.setText(notification.getMessage());
        if(notification.getRecipient()!= null){
            if(notification.getRecipient().getAvatar() != null){
                Picasso.get().load(notification.getRecipient().getAvatar()).into(holder.imageViewAvatar);
            }
            holder.textViewFriend.setText(notification.getRecipient().getFullName());
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNotification, textViewFriend;
        ImageView imageViewAvatar;

        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNotification = itemView.findViewById(R.id.text_notification);
            textViewFriend = itemView.findViewById(R.id.text_friend);
            imageViewAvatar = itemView.findViewById(R.id.image_avatar);
        }
    }
}
