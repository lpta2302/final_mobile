package com.dev.mail.lpta2302.final_mobile.user;

import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Dịch vụ xác thực quyền. Liên quan đến xác thực mật khẩu, đổi mật khẩu.
 */
public class AuthenticationService {
    private AuthenticationService() {}
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String userCollection = "users";
    private final String emailField = "email";
    private final String passwordField = "password";

    public static AuthenticationService getInstance() {
        return new AuthenticationService();
    }

    /**
     * Kiểm tra thông tin đăng nhập.
     * Nếu đúng thông tin đăng nhập thì trả về một đối tượng User.
     * @param email Email
     * @param password Mật khẩu
     * @param callback Callback
     */
    public void verifySignIn(String email, String password, QueryCallback<User> callback) {
        db.collection(userCollection)
                .whereEqualTo(emailField, email)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = documentSnapshots.getDocuments().get(0);
                        String storedPassword = doc.getString(passwordField);

                        if (storedPassword != null) {
                            boolean isPasswordValid = BCrypt.checkpw(password, storedPassword);
                            if (isPasswordValid) {
                                User user = UserService.getInstance().toUser(doc);
                                callback.onSuccess(user);
                            }
                            else callback.onFailure(new Exception("InvalidPassword"));
                        }
                        else {
                            callback.onFailure(new Exception("StoredPasswordIsNull"));
                        }
                    }
                    else {
                        callback.onFailure(new Exception("UserNotFound"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Đổi mật khẩu.
     * @param user Đối tượng người dùng cần đổi mật khẩu
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @param callback Callback
     */
    public void changePassword(User user, String oldPassword, String newPassword, QueryCallback<Void> callback) {
        verifySignIn(user.getEmail(), oldPassword, new QueryCallback<>() {
            @Override
            public void onSuccess(User expectation) {
                db.collection(userCollection)
                        .document(user.getId())
                        .update(passwordField, newPassword)
                        .addOnSuccessListener(aVoid -> {
                            callback.onSuccess(null);
                        })
                        .addOnFailureListener(callback::onFailure);
            }

            @Override
            public void onFailure(Exception exception) {
                callback.onFailure(exception);
            }
        });
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
