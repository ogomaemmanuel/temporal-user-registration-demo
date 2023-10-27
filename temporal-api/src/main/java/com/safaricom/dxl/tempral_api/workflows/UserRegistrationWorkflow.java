package com.safaricom.dxl.tempral_api.workflows;


import com.safaricom.dxl.tempral_api.dtos.UserDto;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import javax.mail.MessagingException;

@WorkflowInterface
public interface UserRegistrationWorkflow {
    @WorkflowMethod
    public void start(UserDto user);
    @SignalMethod
    public void updateUser(UserDto user);

    @QueryMethod
    public UserDto queryUser();

    @SignalMethod
    public void updateStep();

    @SignalMethod
    public void decreaseStep();
    //@UpdateMethod latest version of temporal
}
