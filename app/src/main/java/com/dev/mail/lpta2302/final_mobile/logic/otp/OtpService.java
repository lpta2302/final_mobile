package com.dev.mail.lpta2302.final_mobile.logic.otp;

import com.dev.mail.lpta2302.final_mobile.logic.mail.Mail;
import com.dev.mail.lpta2302.final_mobile.logic.mail.MailService;
import com.dev.mail.lpta2302.final_mobile.logic.mail.SendMailCallback;

import java.util.Random;

public class OtpService {
    private OtpService() {}
    public static OtpService getInstance() {
        return new OtpService();
    }

    private OtpSession otpSession;

    public String generateOtp(int length) {
        Random random = new Random();

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            otp.append(digit);
        }
        return otp.toString();
    }

    public void startOtpSession(String emailTo, int timeoutInSeconds, OtpTimerCallback otpCallback, SendMailCallback sendMailCallback) {
        String otp = generateOtp(6);
        otpSession = new OtpSession(otp);
        otpSession.startSession(timeoutInSeconds, otpCallback);

        String subject = "Mã OTP";
        String content = "Mã xác thực của bạn là: " + otp;
        Mail mail = new Mail(subject, content, emailTo);

        MailService.getInstance().sendEmail(mail, sendMailCallback);
    }

    public boolean verifyOtp(String otp) {
        return otpSession.verify(otp);
    }
}
