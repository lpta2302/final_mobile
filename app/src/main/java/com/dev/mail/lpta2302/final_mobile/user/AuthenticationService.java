package com.dev.mail.lpta2302.final_mobile.user;

import com.dev.mail.lpta2302.final_mobile.ExpectationAndException;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthenticationService {
    /**
     * Kiểm tra thông tin đăng nhập.
     * Nếu đúng thông tin đăng nhập thì kết quả trả về là một đối tượng User.
     *
     * @param email Email
     * @param password Mật khẩu
     * @param onResult Callback
     */
    public static void verifyAccount(String email, String password, ExpectationAndException onResult) {
        UserRepository.instance.findByEmail(email, (exception, expectation) -> {
            if (exception == null) {
                boolean isPasswordValid = BCrypt.checkpw(password, ((User) expectation).getPassword());
                if (isPasswordValid) {
                    AuthUser.getInstance().setAuthenticated(true);
                    AuthUser.getInstance().setUser((User) expectation);
                    onResult.call(null, expectation);
                }
                else onResult.call(new Exception("InvalidPassword"), null);
            }
            else onResult.call(exception, null);
        });
    }
}
