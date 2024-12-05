package com.dev.mail.lpta2302.final_mobile;

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

    private Map<String, Object> toFriendshipMap(Friendship friendship) {
        Map<String, Object> friendshipMap = new HashMap<>();

        friendshipMap.put("userId1", friendship.getUser1().getId());
        friendshipMap.put("userId2", friendship.getUser2().getId());
        if (friendship.getCreatedAt() != null) {
            Timestamp time = new Timestamp(Date.from(friendship.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
            friendshipMap.put("createdAt", time);
        }
        friendshipMap.put("status", friendship.getStatus().toString());

        return friendshipMap;
    }

    public void create(Friendship friendship, ExpectationAndException onResult) {
        db.collection("friendships")
                .add(toFriendshipMap(friendship))
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

    public void findAll(User owner, int limit, int page, ExpectationAndException onResult) {
        db.collection("friendships")
                .whereEqualTo("userId1", owner.getId())
                .orderBy("createdAt")
                .limit(limit)
                .startAfter((page - 1) * limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Friendship> friendships = new ArrayList<>();

                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String userId1 = doc.getString("userId1");
                        String userId2 = doc.getString("userId2");
                        String statusString = doc.getString("status");
                        Timestamp createdAtTimestamp = doc.getTimestamp("createdAt");

                        User user1 = new User();
                        user1.setId(userId1);

                        User user2 = new User();
                        user2.setId(userId2);

                        FriendStatus status;
                        try {
                            status = FriendStatus.valueOf(statusString);
                        } catch (IllegalArgumentException | NullPointerException e) {
                            status = null;
                        }

                        LocalDateTime createdAt = createdAtTimestamp != null
                                ? createdAtTimestamp.toDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime() : null;

                        Friendship friendship = new Friendship(id, user1, user2, createdAt, status);
                        friendships.add(friendship);
                    }

                    onResult.call(null, friendships);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void update(Friendship friendship, ExpectationAndException onResult) {
        db.collection("friendships")
                .document(friendship.getId())
                .set(friendship, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void delete(Friendship friendship, ExpectationAndException onResult) {
        db.collection("friendships")
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
