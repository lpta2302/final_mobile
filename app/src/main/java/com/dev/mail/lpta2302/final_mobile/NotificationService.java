package com.dev.mail.lpta2302.final_mobile;

import android.content.Context;

import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;
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

public class NotificationService {
    private NotificationService(Context context) {
        this.context = context;
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "notifications";
    private final String messageField = "message";
    private final String isReadField = "isRead";
    private final String createdAtField = "createdAt";
    private final String recipientIdField = "recipientId";

    private final Context context;

    public static NotificationService getInstance(Context context) {
        return new NotificationService(context);
    }

    private Map<String, Object> toMap(Notification notification) {
        Map<String, Object> notificationMap = new HashMap<>();

        notificationMap.put(messageField, notification.getMessage());
        notificationMap.put(isReadField, notification.getIsRead());
        Timestamp time = new Timestamp(Date.from(notification.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        notificationMap.put(createdAtField, time);
        notificationMap.put(recipientIdField, notification.getRecipient().getId());

        return notificationMap;
    }

    private void toNotification(DocumentSnapshot documentSnapshot, QueryCallback<Notification> callback) {
        String id = documentSnapshot.getId();
        String message = documentSnapshot.getString(messageField);
        Boolean isRead = documentSnapshot.getBoolean(isReadField);
        Timestamp timestamp = documentSnapshot.getTimestamp(createdAtField);

        LocalDateTime createdAt = timestamp != null
                ? timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime() : null;

        String recipientId = documentSnapshot.getString(recipientIdField);

        UserService.getInstance(context).findById(recipientId, new QueryCallback<>() {
            @Override
            public void onSuccess(User expectation) {
                Notification notification = new Notification(id, message, isRead, createdAt, expectation);
                callback.onSuccess(notification);
            }

            @Override
            public void onFailure(Exception exception) {
                callback.onFailure(exception);
            }
        });
    }

    public void create(Notification notification, QueryCallback<String> callback) {
        db.collection(collectionName)
                .add(toMap(notification))
                .addOnSuccessListener(newDocument -> {

                    String generatedId = newDocument.getId();
                    notification.setId(generatedId);

                    callback.onSuccess(generatedId);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void findAll(User owner, QueryCallback<List<Notification>> callback) {
        db.collection(collectionName)
                .whereEqualTo(recipientIdField, owner.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Notification> notifications = new ArrayList<>();
                    AtomicInteger processedCount = new AtomicInteger(0);
                    AtomicBoolean hasError = new AtomicBoolean(false);
                    int totalDocuments = queryDocumentSnapshots.size();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        if (hasError.get()) return;

                        toNotification(doc, new QueryCallback<>() {
                            @Override
                            public void onSuccess(Notification expectation) {
                                notifications.add(expectation);
                                if (processedCount.incrementAndGet() == totalDocuments)
                                    callback.onSuccess(notifications);
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

    public void findById(String id, QueryCallback<Notification> callback) {
        db.collection(collectionName)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists())
                        toNotification(documentSnapshot, new QueryCallback<>() {
                            @Override
                            public void onSuccess(Notification expectation) {
                                callback.onSuccess(expectation);
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                callback.onFailure(exception);
                            }
                        });
                    else
                        callback.onFailure(new Exception("NotificationNotFound"));
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void update(Notification notification, QueryCallback<Void> callback) {
        db.collection(collectionName)
                .document(notification.getId())
                .set(toMap(notification), SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void delete(Notification notification, QueryCallback<Void> callback) {
        db.collection(collectionName)
                .document(notification.getId())
                .delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}
