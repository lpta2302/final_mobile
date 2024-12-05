package com.dev.mail.lpta2302.final_mobile;

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
    public void create(User user, ExpectationAndException onResult) {
        db.collection("users")
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
        db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
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
        db.collection("user")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        onResult.call(null, documentSnapshot.toObject(User.class));
                    } else {
                        onResult.call(new Exception("UserNotFound"), null);
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void update(User user, ExpectationAndException onResult) {
        db.collection("users")
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
        db.collection("users")
                .whereEqualTo("email", email)
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
        db.collection("users")
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
