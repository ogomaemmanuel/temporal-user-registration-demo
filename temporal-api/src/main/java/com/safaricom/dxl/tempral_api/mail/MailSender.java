package com.safaricom.dxl.tempral_api.mail;

import javax.mail.MessagingException;

public interface MailSender {


    public void send(EmailDto emailDto) throws MessagingException;
}
