package com.dev.mail.lpta2302.final_mobile.friend;

import com.dev.mail.lpta2302.final_mobile.ExpectationAndException;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserRepository;
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

public class FriendService {
    private FriendService() {}
    static {
        instance = new FriendService();
    }
    public static final FriendService instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "friendships";
    private final String userId1Field = "userId1";
    private final String userId2Field = "userId2";
    private final String createdAtField = "createdAt";
    private final String statusField = "status";

    private Map<String, Object> toMap(Friendship friendship) {
        Map<String, Object> friendshipMap = new HashMap<>();

        friendshipMap.put(userId1Field, friendship.getUser1().getId());
        friendshipMap.put(userId2Field, friendship.getUser2().getId());
        Timestamp time = new Timestamp(Date.from(friendship.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        friendshipMap.put(createdAtField, time);
        friendshipMap.put(statusField, friendship.getStatus().toString());

        return friendshipMap;
    }

    public void create(Friendship friendship, ExpectationAndException onResult) {
        db.collection(collectionName)
                .add(toMap(friendship))
                .addOnSuccessListener(newDocument -> {
                    // Khi tạo mới một document trong collection thì gán id cho đối tượng friendship và gọi callback với id đó
                    String generatedId = newDocument.getId();
                    friendship.setId(generatedId);

                    onResult.call(null, generatedId);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void findAll(User owner, ExpectationAndException onResult) {
        db.collection(collectionName)
                .whereEqualTo(userId1Field, owner.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Friendship> friendships = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String id = doc.getId();
                        String userId1 = doc.getString(userId1Field);
                        String userId2 = doc.getString(userId2Field);
                        String statusString = doc.getString(statusField);
                        Timestamp timestamp = doc.getTimestamp(createdAtField);

                        UserRepository.instance.findById(userId1, (exception1, expectation1) -> {
                            if (exception1 == null) {
                                UserRepository.instance.findById(userId2, (exception2, expectation2) -> {
                                    if (exception2 == null) {
                                        FriendStatus status;
                                        try {
                                            status = FriendStatus.valueOf(statusString);
                                        } catch (IllegalArgumentException |
                                                 NullPointerException e) {
                                            status = null;
                                        }

                                        LocalDateTime createdAt = timestamp != null
                                                ? timestamp.toDate().toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime() : null;

                                        User user1 = (User) expectation1;
                                        User user2 = (User) expectation2;
                                        Friendship friendship = new Friendship(id, user1, user2, createdAt, status);
                                        friendships.add(friendship);

                                        if (friendships.size() == queryDocumentSnapshots.size())
                                            onResult.call(null, friendships);
                                    }
                                    else {
                                        onResult.call(exception2, null);
                                    }
                                });
                            }
                            else {
                                onResult.call(exception1, null);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void update(Friendship friendship, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(friendship.getId())
                .set(toMap(friendship), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void delete(Friendship friendship, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(friendship.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }
}
