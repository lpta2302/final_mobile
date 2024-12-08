package com.dev.mail.lpta2302.final_mobile.logic.comment;


import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class CommentService {
    private CommentService(){}
    @Getter
    public static final CommentService instance = new CommentService();

    FirebaseFirestore db;
    public void createComment(Comment comment, QueryCallback<Comment> callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");
        comment.setAuthorId(AuthUser.getInstance().getUser().getId());
        comment.setCreatedAt(new Date());

        dbComments.add(comment)
            .addOnSuccessListener(documentReference -> {
                String generatedId = documentReference.getId(); // Lấy ID tự động sinh
                documentReference.update("id", generatedId);   // Cập nhật ID vào document

                comment.setId(generatedId); // Cập nhật ID trong object comment hiện tại
                callback.onSuccess(comment);
            })
            .addOnFailureListener(callback::onFailure);
    }
    public void updateComment(Comment updateComment, QueryCallback<Comment> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        String commentId = updateComment.getId();
        updateComment.setAuthor(null);

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
    public void deleteComment(String commentId, QueryCallback<Boolean> callback) {
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        dbComments.document(commentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(task.isSuccessful());
                    } else {
                        callback.onFailure(task.getException()); // Trả về null nếu có lỗi
                    }
                });
    }
    public void readComments(QueryCallback<List<Comment>> callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        dbComments
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                            if(task.isSuccessful()){
                                List<Comment> comments = task.getResult().toObjects( Comment.class);
                                AtomicInteger totalComment = new AtomicInteger(comments.size());

                                comments.forEach(comment -> {
                                    UserService.getInstance().findById(comment.getAuthorId(),
                                            new QueryCallback<User>() {
                                                @Override
                                                public void onSuccess(User expectation) {
                                                    if(expectation != null){
                                                        comment.setAuthor((User) expectation);
                                                    }
                                                    if(totalComment.decrementAndGet() == 0){
                                                        callback.onSuccess(comments);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Exception exception) {

                                                }
                                            }
                                    );
                                });
                            }
                            else callback.onFailure(task.getException());
                        }
                );
    }
    public void readCommentsById(List<String> commentsId,QueryCallback<List<Comment>> callback){
        db = FirebaseFirestore.getInstance();
        CollectionReference dbComments = db.collection("comments");

        dbComments
                .whereArrayContainsAny("id", commentsId)
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                            if(task.isSuccessful()){
                                List<Comment> comments = task.getResult().toObjects( Comment.class);
                                AtomicInteger totalComment = new AtomicInteger(comments.size());

                                comments.forEach(comment -> {
                                    UserService.getInstance().findById(comment.getAuthorId(),
                                            new QueryCallback<User>() {
                                                @Override
                                                public void onSuccess(User expectation) {
                                                    if(expectation != null){
                                                        comment.setAuthor((User) expectation);
                                                    }
                                                    if(totalComment.decrementAndGet() == 0){
                                                        callback.onSuccess(comments);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Exception exception) {

                                                }
                                            }
                                    );
                                });
                            }
                            else callback.onFailure(task.getException());
                        }
                );
    }
}
