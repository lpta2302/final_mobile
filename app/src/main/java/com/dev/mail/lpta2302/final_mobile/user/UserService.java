package com.dev.mail.lpta2302.final_mobile.user;

import com.dev.mail.lpta2302.final_mobile.ExpectationAndException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private UserService() {}
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "users";
    private final String emailField = "email";
    private final String passwordField = "password";
    private final String firstNameField = "firstName";
    private final String lastNameField = "lastName";

    public static UserService getInstance() {
        return new UserService();
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put(emailField, user.getEmail());
        userMap.put(firstNameField, user.getFirstName());
        userMap.put(lastNameField, user.getLastName());

        return userMap;
    }

    public User toUser(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getId();
        String email = documentSnapshot.getString(emailField);
        String firstName = documentSnapshot.getString(firstNameField);
        String lastName = documentSnapshot.getString(lastNameField);

        return new User(id, email, firstName, lastName);
    }

    public void create(User user, String password, ExpectationAndException onResult) {
        // Băm mật khẩu trước khi lưu vào DB.
        Map<String, Object> userMap = toMap(user);
        String hashedPassword = AuthenticationService.getInstance().hashPassword(password);
        userMap.put(passwordField, hashedPassword);

        db.collection(collectionName)
                .add(userMap)
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
                    if (documentSnapshot.exists())
                        onResult.call(null, toUser(documentSnapshot));
                    else
                        onResult.call(new Exception("UserNotFound"), null);
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
                        onResult.call(null, toUser(documentSnapshot));
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
                .set(toMap(user), SetOptions.merge())
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
