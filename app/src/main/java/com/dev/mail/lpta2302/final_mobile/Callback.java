package com.dev.mail.lpta2302.final_mobile;

@FunctionalInterface
public interface Callback {
    void onResultOrError(Object result, Exception e);
}
