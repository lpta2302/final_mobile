package com.dev.mail.lpta2302.final_mobile.post;

import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import lombok.Getter;

public class PostService {
    private PostService(){}
    @Getter
    private static final PostService instance = new PostService();
    private FirebaseFirestore db;
    public interface ReadCallback {
        void onSuccess(List<Post> posts);
        void onFailure(Exception e);
    }
    public interface CreateCallback {
        void onSuccess(Post post);
        void onFailure(Exception e);
    }
    public interface UpdateCallback {
        void onSuccess(Post post);
        void onFailure(Exception e);
    }
    public interface DeleteCallback {
        void onSuccess(Post post);
        void onFailure(Exception e);
    }
    public interface SearchCallback {
        void onSuccess(List<Post> posts);
        void onFailure(Exception e);
    }
    public void createPost(Post post, CreateCallback callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");
        post.setAuthor(AuthUser.getInstance().getUser());

        dbPosts.add(post)
            .addOnSuccessListener(documentReference -> {
                String generatedId = documentReference.getId(); // Lấy ID tự động sinh
                documentReference.update("id", generatedId);   // Cập nhật ID vào document

                post.setId(generatedId); // Cập nhật ID trong object post hiện tại
                callback.onSuccess(post);
            })
            .addOnFailureListener(callback::onFailure);
    }

    public void updatePost(Post updatePost, UpdateCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        String postId = updatePost.getId();

        dbPosts.document(postId)
                .set(updatePost)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dbPosts.get().addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                List<Post> updatedPosts = fetchTask.getResult().toObjects(Post.class);
                                callback.onSuccess(updatedPosts.get(0)); // Trả về danh sách các post sau khi cập nhật
                            }
                        });
                    } else {
                        callback.onFailure(task.getException()); // Trả về null nếu có lỗi
                    }
                });
    }


    public void deletePost(String postId, DeleteCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.document(postId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dbPosts.get().addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                List<Post> posts = fetchTask.getResult().toObjects(Post.class);
                                callback.onSuccess(posts.get(0)); // Trả về danh sách các bài viết sau khi xóa
                            }
                        });
                    } else {
                        callback.onFailure(task.getException()); // Trả về null nếu có lỗi
                    }
                });
    }


    public void readPosts(ReadCallback callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts
            .get()
            .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                    if(task.isSuccessful()){
                        List<Post> posts = task.getResult().toObjects( Post.class);
                        callback.onSuccess(posts);
                    }else callback.onFailure(task.getException());
                }
            );
    }

    public void searchPostsByCaption(String captionFragment, SearchCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.whereGreaterThanOrEqualTo("caption", captionFragment)  // Tìm kiếm caption bắt đầu bằng captionFragment
                .whereLessThan("caption", captionFragment + "\uf8ff")    // Tìm kiếm caption kết thúc sau captionFragment
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> posts = task.getResult().toObjects(Post.class);
                        callback.onSuccess(posts);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
    public void searchPostsByTags(List<String> tags, SearchCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.whereArrayContainsAny("tags", tags) // Tìm các bài viết có chứa tag cụ thể
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> posts = task.getResult().toObjects(Post.class);
                        callback.onSuccess(posts);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}
