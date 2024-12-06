package com.dev.mail.lpta2302.final_mobile.comment;


import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import lombok.Getter;

public class CommentService {
    private CommentService(){}
    @Getter
    public static final CommentService instance = new CommentService();

    FirebaseFirestore db;
    public interface ReadCallback {
        void onSuccess(List<Comment> comments);
        void onFailure(Exception e);
    }
    public interface CreateCallback {
        void onSuccess(Comment comment);
        void onFailure(Exception e);
    }
    public interface UpdateCallback {
        void onSuccess(Comment comment);
        void onFailure(Exception e);
    }
    public interface DeleteCallback {
        void onSuccess(Comment comment);
        void onFailure(Exception e);
    }
    public interface SearchCallback {
        void onSuccess(List<Comment> comments);
        void onFailure(Exception e);
    }
    public void createComment(Comment comment, CreateCallback callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");
        comment.setAuthor(AuthUser.getInstance().getUser());

        dbComments.add(comment)
            .addOnSuccessListener(documentReference -> {
                String generatedId = documentReference.getId(); // Lấy ID tự động sinh
                documentReference.update("id", generatedId);   // Cập nhật ID vào document

                comment.setId(generatedId); // Cập nhật ID trong object comment hiện tại
                callback.onSuccess(comment);
            })
            .addOnFailureListener(callback::onFailure);
    }

    public void updateComment(Comment updateComment, UpdateCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        String commentId = updateComment.getId();

        dbComments.document(commentId)
                .set(updateComment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dbComments.get().addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                List<Comment> updatedComments = fetchTask.getResult().toObjects(Comment.class);
                                callback.onSuccess(updatedComments.get(0)); // Trả về danh sách các comment sau khi cập nhật
                            }
                        });
                    } else {
                        callback.onFailure(task.getException()); // Trả về null nếu có lỗi
                    }
                });
    }


    public void deleteComment(String commentId, DeleteCallback callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        dbComments.document(commentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dbComments.get().addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                List<Comment> comments = fetchTask.getResult().toObjects(Comment.class);
                                callback.onSuccess(comments.get(0)); // Trả về danh sách các bài viết sau khi xóa
                            }
                        });
                    } else {
                        callback.onFailure(task.getException()); // Trả về null nếu có lỗi
                    }
                });
    }


    public void readComments(ReadCallback callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        dbComments
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                            if(task.isSuccessful()){
                                List<Comment> comments = task.getResult().toObjects( Comment.class);
                                callback.onSuccess(comments);
                            }else callback.onFailure(task.getException());
                        }
                );
    }
}
