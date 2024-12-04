package com.dev.mail.lpta2302.final_mobile;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthenticationService {
    // Nếu đúng thông tin đăng nhập thì kết quả trả về là một đối tượng User
    public static void verifyAccount(String email, String password, ExpectationAndException onResult) {
        UserRepository.singleton.findByEmail(email, (exception, expectation) -> {
            if (exception == null) {
                boolean isPasswordValid = BCrypt.checkpw(password, ((User) expectation).getPassword());
                if (isPasswordValid) onResult.call(null, expectation);
                else onResult.call(new Exception("InvalidPassword"), null);
            }
            else onResult.call(exception, null);
        });
    }
}
