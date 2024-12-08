package com.dev.mail.lpta2302.final_mobile.activities.addnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.post.PostService;
import com.dev.mail.lpta2302.final_mobile.util.ImageUploadService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddnewsFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private Uri selectedImageUri;
    private ImageView addImage;
    private EditText captionInput, tagsInput;
    private Post post;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    selectedImageUri = uri;
                    addImage.setImageURI(uri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_addnews, container, false);

        // Bind the UI components
        captionInput = rootView.findViewById(R.id.captionInput);
        tagsInput = rootView.findViewById(R.id.tagsInput);
        addImage = rootView.findViewById(R.id.postImage);
        Button postBtn = rootView.findViewById(R.id.postBtn);

        // Set the click listener for the post button
        postBtn.setOnClickListener(v -> postHandle());
        addImage.setOnClickListener(v -> mGetContent.launch("image/*"));

        return rootView;
    }

    private void postHandle() {
        String caption = captionInput.getText().toString().trim();
        String tagsString = tagsInput.getText().toString().trim();

        // Validate input fields
        if (caption.isEmpty()) {
            Toast.makeText(getContext(), "Caption cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tagsString.isEmpty()) {
            Toast.makeText(getContext(), "Please enter at least one tag", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse tags (comma-separated into a list)
        List<String> tags = Arrays.asList(tagsString.split(",\\s*"));

        // Create a Post object
        post = Post.builder()
                .caption(caption)
                .tags(tags)
                .commentIds(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();

        // Here, you can send the newPost to a database, server, or repository logic
        uploadImage();
    }

    private void uploadImage() {
        ImageUploadService.getInstance().uploadImage(selectedImageUri, new ImageUploadService.OnImageUploadListener() {
            @Override
            public void onSuccess(String imageUrl) {
                post.setImageUrl(imageUrl);
                createPost();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show()    ;
            }
        });
    }
    private void createPost(){
        PostService.getInstance().createPost(post, new QueryCallback<Post>() {
            @Override
            public void onSuccess(Post expectation) {
                Toast.makeText(getContext(), "Post created", Toast.LENGTH_SHORT).show();
                captionInput.setText("");
                tagsInput.setText("");
                post = null;
                selectedImageUri = null;
                addImage.setImageURI(null);
                addImage.setImageResource(R.drawable.outline_add_photo_alternate_24);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "Post Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
