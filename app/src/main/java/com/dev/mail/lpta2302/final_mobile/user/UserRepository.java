package com.dev.mail.lpta2302.final_mobile.user;

import com.dev.mail.lpta2302.final_mobile.ExpectationAndException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class UserRepository {
    private UserRepository() {}
    public static final UserRepository instance;
    static {
        instance = new UserRepository();
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "users";
    private final String emailField = "email";

    public void create(User user, ExpectationAndException onResult) {
        db.collection(collectionName)
                .add(user)
                .addOnSuccessListener(newDocument -> {
                    // Khi tạo mới một document trong collection thì gán id cho đối tượng user và gọi callback với id đó
                    String generatedId = newDocument.getId();
                    user.setId(generatedId);

                    onResult.call(null, generatedId);
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
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) user.setId(documentSnapshot.getId());
                        onResult.call(null, user);
                    }
                    else {
                        onResult.call(new Exception("UserNotFound"), null);
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void findByEmail(String email, ExpectationAndException onResult) {
        db.collection(collectionName)
                .whereEqualTo(emailField, email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) user.setId(documentSnapshot.getId());
                        onResult.call(null, user);
                    } else {
                        onResult.call(new Exception("UserNotFound"), null);
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void update(User user, ExpectationAndException onResult) {
        db.collection(collectionName)
                .document(user.getId())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    onResult.call(null, null);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void isEmailExisting(String email, ExpectationAndException onResult) {
        db.collection(collectionName)
                .whereEqualTo(emailField, email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean exists = !querySnapshot.isEmpty();
                    onResult.call(null, exists);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void isUserNameExisting(String userName, ExpectationAndException onResult) {
        db.collection(collectionName)
                .whereEqualTo("userName", userName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean exists = !querySnapshot.isEmpty();
                    onResult.call(null, exists);
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }
}
