package com.safaricom.dxl.tempral_api.workflows.activities;

import com.safaricom.dxl.tempral_api.dtos.UserDto;
import io.temporal.activity.ActivityInterface;

import javax.mail.MessagingException;

@ActivityInterface
public interface UserRegistrationActivities {

    public void save(UserDto user);

    public  void sendVerificationEmail(UserDto user);

    public void sendWelcomeEmail(UserDto user);

    public  void logEvent();

}
