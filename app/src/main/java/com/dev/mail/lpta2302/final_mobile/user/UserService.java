package com.dev.mail.lpta2302.final_mobile.user;

import androidx.annotation.NonNull;

import com.dev.mail.lpta2302.final_mobile.post.Post;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;
import com.dev.mail.lpta2302.final_mobile.util.RemoveVietnameseDiacritics;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private UserService() {}
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName = "users";
    private final String emailField = "email";
    private final String passwordField = "password";
    private final String firstNameField = "firstName";
    private final String lastNameField = "lastName";
    private final String fullNameField = "fullName";
    private final String genderField = "gender";
    private final String dateOfBirthField = "dateOfBirth";

    public static UserService getInstance() {
        return new UserService();
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put(emailField, user.getEmail());
        userMap.put(firstNameField, user.getFirstName());
        userMap.put(lastNameField, user.getLastName());
        userMap.put(fullNameField, user.getFullName());
        userMap.put(genderField, user.getGender() == Gender.MALE);

        Date date = Date.from(user.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Timestamp timestamp = new Timestamp(date);
        userMap.put(dateOfBirthField, timestamp);

        return userMap;
    }

    public User toUser(DocumentSnapshot documentSnapshot) {
        String id = documentSnapshot.getId();
        String email = documentSnapshot.getString(emailField);
        String firstName = documentSnapshot.getString(firstNameField);
        String lastName = documentSnapshot.getString(lastNameField);
        String avatar = documentSnapshot.getString("avatar");
        Boolean genderBoolean = documentSnapshot.getBoolean(genderField);

        Gender gender;
        if (genderBoolean != null && genderBoolean) gender = Gender.MALE;
        else gender = Gender.FEMALE;

        Timestamp timestamp = documentSnapshot.getTimestamp(dateOfBirthField);
        LocalDate dateOfBirth;
        if (timestamp != null)
            dateOfBirth = timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        else
            dateOfBirth = null;

        return new User(id, email, firstName, lastName, gender,avatar, dateOfBirth);
    }
    public void readUsers(QueryCallback<List<User>> callback){
        CollectionReference dbPosts = db.collection("users");

        dbPosts
            .get()
            .addOnCompleteListener((@NonNull Task<QuerySnapshot> task)->{
                        if(task.isSuccessful()){
                            List<User> users = task.getResult().toObjects(User.class);
                            callback.onSuccess(users);
                        }
                        else
                            callback.onFailure(task.getException());
                    }
            );
    }

    public void create(User user, String password, QueryCallback<String> callback) {
        // Băm mật khẩu trước khi lưu vào DB.
        Map<String, Object> userMap = toMap(user);
        String hashedPassword = AuthenticationService.getInstance().hashPassword(password);
        userMap.put(passwordField, hashedPassword);

        db.collection(collectionName)
                .add(userMap)
                .addOnSuccessListener(newDocument -> {

                    String generatedId = newDocument.getId();
                    user.setId(generatedId);
                    newDocument.update("id",generatedId);

                    callback.onSuccess(generatedId);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void findById(String id, QueryCallback<User> callback) {
        db.collection(collectionName)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists())
                        callback.onSuccess(toUser(documentSnapshot));
                    else
                        callback.onFailure(new Exception("UserNotFound"));
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void findByEmail(String email, QueryCallback<User> callback) {
        db.collection(collectionName)
                .whereEqualTo(emailField, email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        callback.onSuccess(toUser(documentSnapshot));
                    } else {
                        callback.onFailure(new Exception("UserNotFound"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void searchByFullName(String keyword, QueryCallback<List<User>> callback) {
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String fullName = doc.getString(fullNameField);
                        if (fullName != null
                                && RemoveVietnameseDiacritics.removeDiacritics(fullName).trim()
                                .contains(RemoveVietnameseDiacritics.removeDiacritics(keyword).trim()))
                            users.add(toUser(doc));
                    }
                    callback.onSuccess(users);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void update(User user, QueryCallback<Void> callback) {
        db.collection(collectionName)
                .document(user.getId())
                .set(toMap(user), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
