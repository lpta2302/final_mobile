package com.dev.mail.lpta2302.final_mobile.util;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import lombok.Getter;

public class ImageUploadService {
    @Getter
    private static final ImageUploadService instance = new ImageUploadService();

    public void uploadImage(Uri imageUri, OnImageUploadListener listener) {
        if (imageUri == null) {
            listener.onFailure((new RuntimeException("Image Uri is null")));
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        String fileExtension = getFileExtension(imageUri);

        StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpeg");

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                listener.onSuccess(uri.toString());
            });
        }).addOnFailureListener(listener::onFailure);
    }

    private String getFileExtension(Uri uri) {
        String fileExtension = null;
        try {
            String path = uri.getPath();
            if (path != null && path.contains(".")) {
                fileExtension = path.substring(path.lastIndexOf('.') + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileExtension != null ? fileExtension : "jpg"; // Default to jpg if no extension found
    }

    // Callback interface to handle the upload result
    public interface OnImageUploadListener {
        void onSuccess(String imageUrl);
        void onFailure(Exception e);
    }
}
