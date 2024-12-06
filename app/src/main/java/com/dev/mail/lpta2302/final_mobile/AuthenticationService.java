package com.dev.mail.lpta2302.final_mobile;

import com.google.firebase.firestore.FirebaseFirestore;

import org.springframework.security.crypto.bcrypt.BCrypt;

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
     * Nếu đúng thông tin đăng nhập thì kết quả trả về là một đối tượng User.
     *
     * @param email Email
     * @param password Mật khẩu
     * @param onResult Callback
     * @see ExpectationAndException
     */
    public void verifySignIn(String email, String password, ExpectationAndException onResult) {
        db.collection(userCollection)
                .whereEqualTo(emailField, email)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (!documentSnapshots.isEmpty()) {
                        String storedPassword = documentSnapshots.getDocuments().get(0).getString(passwordField);

                        if (storedPassword != null) {
                            boolean isPasswordValid = BCrypt.checkpw(password, storedPassword);
                            if (isPasswordValid) onResult.call(null, "CorrectPassword");
                            else onResult.call(new Exception("InvalidPassword"), null);
                        }
                        else {
                            onResult.call(new Exception("StoredPasswordIsNull"), null);
                        }
                    }
                    else {
                        onResult.call(new Exception("UserNotFound"), null);
                    }
                })
                .addOnFailureListener(e -> {
                    onResult.call(e, null);
                });
    }

    public void changePassword(String oldPassword, String newPassword) {

    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
