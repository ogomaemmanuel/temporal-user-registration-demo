package com.safaricom.dxl.tempral_api.workflows.activities;

import com.safaricom.dxl.tempral_api.dtos.UserDto;
import com.safaricom.dxl.tempral_api.entities.User;
import com.safaricom.dxl.tempral_api.mail.EmailDto;
import com.safaricom.dxl.tempral_api.mail.MailSender;
import com.safaricom.dxl.tempral_api.repositories.UserRepository;
import io.temporal.activity.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@Slf4j
public class UserRegistrationActivitiesImpl implements UserRegistrationActivities {

    private final UserRepository userRepository;

    private final MailSender mailSender;

    public UserRegistrationActivitiesImpl(UserRepository userRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void save(UserDto userDto) {
        User user = userDto.toEntity();
        this.userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(UserDto user) {
        var emailDto = EmailDto
                .builder()
                .recipient(user.getEmail())
                .isHtml(false)
                .content(String.format("Hello %s \n You have registered your account with TestApp. Before being able to use your account, You need to verify that this is your email address by clicking the link", user.getUsername()))
                .subject("Verification")
                .build();
        try {
            this.mailSender.send(emailDto);
        } catch (Exception e) {
            throw Activity.wrap(e);
        }
    }

    @Override
    public void sendWelcomeEmail(UserDto user) {

    }

    @Override
    public void logEvent() {
        log.info("Activity log");
    }


}
