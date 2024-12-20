package com.dev.mail.lpta2302.final_mobile.activities.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.CommentFragment;
import com.dev.mail.lpta2302.final_mobile.activities.detail.DetailFragment;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.post.Post;
import com.dev.mail.lpta2302.final_mobile.logic.post.PostService;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> postList;
    FragmentManager fragmentManager;

    public PostAdapter(List<Post> postList, FragmentManager fragmentManager) {
        this.postList = postList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new PostViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        Context context = holder.context;
        // Sử dụng hình ảnh avatar1 cho tất cả các bài viết

        if (post.getAuthor().getAvatar() != null)
            Picasso.get().load(post.getAuthor().getAvatar()).into(holder.authorAvatar);
        Picasso.get().load(post.getImageUrl()).into(holder.postImage);
        holder.authorName.setText(post.getAuthor().getFullName());
        holder.createdAt.setText(post.formatedDate());
        holder.caption.setText(post.getCaption());
        holder.commentCount.setText(String.valueOf(post.getCommentIds().size()));
        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        if (AuthUser.getInstance().isAuthenticated() && AuthUser.getInstance().getUser().getId().equals(post.getAuthorId())){
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }

        holder.commentBtn.setOnClickListener((v)->{
            // Create a new instance of the CommentFragment
            CommentFragment commentFragment = new CommentFragment();

            // Pass the post data to the CommentFragment (optional, using arguments)
            Bundle args = new Bundle();
            args.putString("postId", post.getId()); // Pass post ID or other relevant data
            commentFragment.setArguments(args);

            // Replace the current fragment with CommentFragment
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, commentFragment);
            transaction.addToBackStack(null); // Add to back stack to allow "Back" navigation
            transaction.commit();
        });
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            String tagsText = post.getTags().stream()
                    .map(tag -> "#" + tag) // Prepend each tag with #
                    .collect(Collectors.joining()); // Join all tags without a separator
            holder.tag.setText(tagsText);
        }

//        like handle
        holder.likeBtn.setImageResource(
                post.getLikes().contains(AuthUser.getInstance().getUser().getId()) ?
                        R.drawable.liked :
                        R.drawable.like
        );
        holder.likeBtn.setOnClickListener(v->{
            User user = post.getAuthor();
            PostService.getInstance().toggleLikePost(post, new QueryCallback<Post>() {
                @Override
                public void onSuccess(Post expectation) {
                    // Update the data at the specific position
                    expectation.setAuthor(user);
                    postList.set(holder.getAdapterPosition(), expectation);
                    // Notify the adapter to refresh that item
                    notifyItemChanged(holder.getAdapterPosition());
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(holder.itemView.getContext(), "Failed to toggle like", Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.deleteBtn.setOnClickListener(v->{
            PostService.getInstance().deletePost(post.getId(), new QueryCallback<Post>() {
                @Override
                public void onSuccess(Post expectation) {
                    Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(holder.getAdapterPosition());
                }
                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(holder.itemView.getContext(), "Failed to toggle like", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView postImage, authorAvatar, editBtn, commentBtn, likeBtn, deleteBtn;
        TextView likeCount, commentCount, authorName, createdAt, caption, tag;

        public PostViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            postImage = itemView.findViewById(R.id.postImage);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            authorAvatar = itemView.findViewById(R.id.avatarIv);
            editBtn = itemView.findViewById(R.id.editBtn);
            authorName = itemView.findViewById((R.id.authorNameTv));
            createdAt = itemView.findViewById(R.id.createdAtTv);
            caption = itemView.findViewById(R.id.captionTv);
            commentBtn = itemView.findViewById(R.id.commentIcon);
            likeBtn = itemView.findViewById(R.id.likeIcon);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            tag = itemView.findViewById(R.id.tagTv);
        }
    }
}
