package com.safaricom.dxl.tempral_api.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSenderImpl implements MailSender{
   private final JavaMailSender  mailSender;

    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    public void send(EmailDto emailDto) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("noreply@eogoma.com");
        helper.setTo(emailDto.getRecipient());
        helper.setSubject(emailDto.getSubject());
        if(!emailDto.isHtml()) {
            helper.setText(emailDto.getContent());
        }else{

        }
        this.mailSender.send(mimeMessage);

    }
}
