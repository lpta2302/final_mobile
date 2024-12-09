package com.dev.mail.lpta2302.final_mobile.logic.friend;

import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class FriendService {
    private FriendService() {

    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "friendships";
    private final String user1IdField = "userId1";
    private final String user2IdField = "userId2";
    private final String createdAtField = "createdAt";
    private final String statusField = "status";

    @Getter
    public static FriendService instance = new FriendService();

    private Map<String, Object> toMap(Friendship friendship) {
        Map<String, Object> friendshipMap = new HashMap<>();

        friendshipMap.put(user1IdField, friendship.getUser1().getId());
        friendshipMap.put(user2IdField, friendship.getUser2().getId());
        Timestamp time = new Timestamp(Date.from(friendship.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        friendshipMap.put(createdAtField, time);
        friendshipMap.put(statusField, friendship.getStatus().toString());

        return friendshipMap;
    }

    private void toFriendship(DocumentSnapshot documentSnapshot, QueryCallback<Friendship> callback) {
        String id = documentSnapshot.getId();
        String user1Id = documentSnapshot.getString(user1IdField);
        String user2Id = documentSnapshot.getString(user2IdField);

        // Parse status kiểu String từ DB sang FriendStatus.
        String statusString = documentSnapshot.getString(statusField);
        FriendStatus status;
        if (FriendStatus.canParseEnum(statusString))
            status = FriendStatus.valueOf(statusString);
        else
            status = FriendStatus.PENDING;

        // Parse createdAt kiểu Timestamp từ DB sang LocalDateTime.
        Timestamp timestamp = documentSnapshot.getTimestamp(createdAtField);
        LocalDateTime createdAt = timestamp != null
                ? timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime() : null;

        UserService.getInstance().findById(user1Id, new QueryCallback<>() {
            @Override
            public void onSuccess(User expectation) {
                User user1 = expectation;
                UserService.getInstance().findById(user2Id, new QueryCallback<>() {
                    @Override
                    public void onSuccess(User expectation) {
                        Friendship friendship = new Friendship(id, user1, expectation, createdAt, status);
                        callback.onSuccess(friendship);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        callback.onFailure(exception);
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                callback.onFailure(exception);
            }
        });
    }

    public void create(Friendship friendship, QueryCallback<String> callback) {
        db.collection(collectionName)
                .add(toMap(friendship))
                .addOnSuccessListener(newDocument -> {

                    String generatedId = newDocument.getId();
                    friendship.setId(generatedId);

                    callback.onSuccess(generatedId);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void findAll(User owner, QueryCallback<List<Friendship>> callback) {
        db.collection(collectionName)
                .whereEqualTo(user1IdField, owner.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Friendship> friendships = new ArrayList<>();

                    AtomicInteger processedCount = new AtomicInteger(0);

                    AtomicBoolean hasError = new AtomicBoolean(false);
                    int totalDocuments = queryDocumentSnapshots.size();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        if (hasError.get()) return;

                        toFriendship(doc, new QueryCallback<>() {
                            @Override
                            public void onSuccess(Friendship expectation) {
                                friendships.add(expectation);

                                if (processedCount.incrementAndGet() == totalDocuments)
                                    callback.onSuccess(friendships);
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                hasError.set(true);
                                callback.onFailure(exception);
                            }
                        });
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void update(Friendship friendship, QueryCallback<Void> callback) {
        db.collection(collectionName)
                .document(friendship.getId())
                .set(toMap(friendship), SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void delete(Friendship friendship, QueryCallback<Void> callback) {
        db.collection(collectionName)
                .document(friendship.getId())
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}
