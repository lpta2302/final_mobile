package com.dev.mail.lpta2302.final_mobile.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Mail {
    private String subject;
    private String content;
    private String to;
}