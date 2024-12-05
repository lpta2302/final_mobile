package com.dev.mail.lpta2302.final_mobile;

@FunctionalInterface
public interface ExpectationAndException {
    void call(Exception exception, Object expectation);
}
