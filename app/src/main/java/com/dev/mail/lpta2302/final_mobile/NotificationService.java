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

public class NotificationService {
    private NotificationService() {}
    public static final NotificationService instance;
    static {
        instance = new NotificationService();
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "notifications";
    private final String messageField = "message";
    private final String isReadField = "isRead";
    private final String createdAtField = "createdAt";
    private final String recipientIdField = "recipientId";

    private Map<String, Object> toMap(Notification notification) {
        Map<String, Object> notificationMap = new HashMap<>();

        notificationMap.put(messageField, notification.getMessage());
        notificationMap.put(isReadField, notification.getIsRead());
        Timestamp time = new Timestamp(Date.from(notification.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        notificationMap.put(createdAtField, time);
        notificationMap.put(recipientIdField, notification.getRecipient().getId());

        return notificationMap;
    }

    public void create(Notification notification, ExpectationAndException onResult) {
        db.collection(collectionName)
                .add(toMap(notification))
                .addOnSuccessListener(newDocument -> {
                    // Khi tạo mới một document trong collection thì gán id cho đối tượng notification và gọi callback với id đó
                    String generatedId = newDocument.getId();
                    notification.setId(generatedId);

                    onResult.call(null, generatedId);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void findAll(User owner, ExpectationAndException onResult) {
        db.collection(collectionName)
                .whereEqualTo(recipientIdField, owner.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Notification> notifications = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String id = doc.getId();
                        String message = doc.getString(messageField);
                        Boolean isRead = doc.getBoolean(isReadField);
                        Timestamp timestamp = doc.getTimestamp(createdAtField);
                        LocalDateTime createdAt = timestamp != null
                                ? timestamp.toDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime() : null;
                        String recipientId = doc.getString(recipientIdField);

                        UserRepository.instance.findById(recipientId, (exception, expectation) -> {
                            if (exception == null) {
                                User recipient = (User) expectation;
                                Notification notification = new Notification(message, isRead, createdAt, recipient);
                                notification.setId(id);
                                notifications.add(notification);

                                if (notifications.size() == queryDocumentSnapshots.size())
                                    onResult.call(null, notifications);
                            }
                            else {
                                onResult.call(exception, null);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void findById(String id, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String message = documentSnapshot.getString(messageField);
                        Boolean isRead = documentSnapshot.getBoolean(isReadField);
                        Timestamp timestamp = documentSnapshot.getTimestamp(createdAtField);
                        LocalDateTime createdAt = timestamp != null
                                ? timestamp.toDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime() : null;
                        String recipientId = documentSnapshot.getString(recipientIdField);

                        UserRepository.instance.findById(recipientId, (exception, expectation) -> {
                            if (exception == null) {
                                User recipient = (User) expectation;
                                Notification notification = new Notification(id, message, isRead, createdAt, recipient);
                                onResult.call(null, notification);
                            }
                            else {
                                onResult.call(exception, null);
                            }
                        });
                    }
                    else {
                        onResult.call(new Exception("NotificationNotFound"), null);
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void update(Notification notification, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(notification.getId())
                .set(toMap(notification), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void delete(Notification notification, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(notification.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }
}
