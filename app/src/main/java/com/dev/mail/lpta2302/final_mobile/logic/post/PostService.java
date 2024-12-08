package com.dev.mail.lpta2302.final_mobile.logic.post;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class PostService {
    private PostService(){}
    @Getter
    private static final PostService instance = new PostService();
    private FirebaseFirestore db;
    public void createPost(Post post, QueryCallback<Post> callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");
        post.setAuthorId(AuthUser.getInstance().getUser().getId());
        post.setCreatedAt(new Date());

        dbPosts.add(post)
                .addOnSuccessListener(documentReference -> {
                    String generatedId = documentReference.getId(); // Lấy ID tự động sinh
                    documentReference.update("id", generatedId);   // Cập nhật ID vào document
                    Log.d("add", "ok");

                    post.setId(generatedId); // Cập nhật ID trong object post hiện tại
                    callback.onSuccess(post);
                })
                .addOnFailureListener(callback::onFailure);
    }
    public void updatePost(Post updatePost, QueryCallback<Post> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        String postId = updatePost.getId();
        updatePost.setAuthor(null);

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
    public void deletePost(String postId, QueryCallback<Post> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");
        CollectionReference dbComments = db.collection("comments");

        DocumentReference postRef = db.collection("posts").document(postId);
        postRef.get().addOnCompleteListener(
                (@NonNull Task<DocumentSnapshot> task) -> {
                    if(task.isSuccessful()){

                        Post post = task.getResult().toObject(Post.class);
                        if(post == null)throw new RuntimeException("Post is null");
                        List<String> commentIds = post.getCommentIds();
                        AtomicInteger totalComment = new AtomicInteger(commentIds.size());
                        commentIds.forEach(id->{
                            dbComments.document(id)
                                    .delete()
                                    .addOnCompleteListener(
                                            (@NonNull Task<Void> t) -> {
                                                totalComment.getAndDecrement();
                                                if(totalComment.get() == 0){
                                                    db.collection("posts").document(postId);
                                                }
                                            }

                                    );

                        });
                    }
                }
        );

    }
    public void readPosts(QueryCallback<List<Post>> callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                            addAuthorToPosts(task,callback);
                        }
                );
    }
    public void searchPostByUser(String userId, QueryCallback<List<Post>> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.whereEqualTo("authorId", userId)
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                    if(task.isSuccessful()){
                        List<Post> posts = task.getResult().toObjects( Post.class);

                        UserService.getInstance().findById(userId, new QueryCallback<User>() {
                            @Override
                            public void onSuccess(User expectation) {
                                posts.forEach(post->{
                                    post.setAuthor( expectation);
                                });
                                callback.onSuccess(posts);
                            }

                            @Override
                            public void onFailure(Exception exception) {

                            }
                        });
                    }else callback.onFailure(task.getException());
                });
    }
    public void searchPostsByCaption(String captionFragment, QueryCallback<List<Post>> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.whereGreaterThanOrEqualTo("caption", captionFragment)  // Tìm kiếm caption bắt đầu bằng captionFragment
                .whereLessThan("caption", captionFragment + "\uf8ff")    // Tìm kiếm caption kết thúc sau captionFragment
                .get()
                .addOnCompleteListener(task -> {
                    addAuthorToPosts(task, callback);
                });
    }
    public void searchPostsByTags(List<String> tags, QueryCallback<List<Post>> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbPosts = db.collection("posts");

        dbPosts.whereArrayContainsAny("tags", tags) // Tìm các bài viết có chứa tag cụ thể
                .get()
                .addOnCompleteListener(task -> {
                    addAuthorToPosts(task, callback);
                });
    }
    public void toggleLikePost(Post post, QueryCallback<Post> callback){
        List<String> likedUsers = post.getLikes();
        String currentUserId = AuthUser.getInstance().getUser().getId();
        if(!likedUsers.remove(currentUserId))
            likedUsers.add(currentUserId);

        updatePost(post,callback);
    }
    private void addAuthorToPosts(Task<QuerySnapshot> task, QueryCallback<List<Post>> callback){
        if(task.isSuccessful()){
            List<Post> posts = task.getResult().toObjects( Post.class);
            AtomicInteger totalPost = new AtomicInteger(posts.size());
            posts.forEach(post->{
                UserService.getInstance().findById(post.getAuthorId(), new QueryCallback<User>() {
                    @Override
                    public void onSuccess(User expectation) {
                        if(expectation != null){
                            post.setAuthor(expectation);
                            if(totalPost.decrementAndGet() == 0){
                                callback.onSuccess(posts);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            });
        }else callback.onFailure(task.getException());
    }
}
