package com.safaricom.dxl.tempral_api.workflows;


import com.safaricom.dxl.tempral_api.dtos.UserDto;
import com.safaricom.dxl.tempral_api.workflows.activities.UserRegistrationActivities;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.Duration;

public class UserRegistrationWorkflowImpl implements UserRegistrationWorkflow {

    private int stage = 0;
    private UserDto user;

    private boolean completed = false;

    private boolean check = false;
    private final UserRegistrationActivities saveUserToDbActivity = Workflow.newActivityStub(UserRegistrationActivities.class,
            ActivityOptions.newBuilder()

                    .setRetryOptions(RetryOptions.newBuilder()
                            .setBackoffCoefficient(2)
                            .setInitialInterval(Duration.ofSeconds(2))
                            .setMaximumInterval(Duration.ofSeconds(1000))
                            .build())
                    .setStartToCloseTimeout(Duration.ofDays(1))

                    .build()
    );
    private final UserRegistrationActivities saveUserToDbActivity2 = Workflow.newActivityStub(UserRegistrationActivities.class,
            ActivityOptions.newBuilder()
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setBackoffCoefficient(1)
                            .build())
                    .setStartToCloseTimeout(Duration.ofDays(1))
                    .build()
    );

    @Override
    public void start(UserDto user) {
       // Saga userRegistrationSaga = new Saga(new Saga.Options.Builder().build());
        this.user = user;
        Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);
        Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);

        Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);
        //userRegistrationSaga.addCompensation(()->{});
        saveUserToDbActivity.save(user);
        stage = 2;

        Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);

        //userRegistrationSaga.addCompensation(()->{});
        saveUserToDbActivity2.sendVerificationEmail(user);
        Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);
        //userRegistrationSaga.addCompensation(()->{});
        saveUserToDbActivity.sendWelcomeEmail(user);
        Workflow.await(Duration.ofHours(2), () -> stage == 3);

        if (stage != 3) {

        } else {
            Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("At step {}", stage);

            saveUserToDbActivity.logEvent();
            Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("Nothing here... {}", stage);
            Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("Nothing here... {}", stage);
            Workflow.getLogger(UserRegistrationWorkflowImpl.class.getName()).info("Completed at stage {}", stage);
        }


    }

    @Override
    public void updateUser(UserDto user) {

    }

    @Override
    public UserDto queryUser() {
        return null;
    }

    @Override
    public void updateStep() {
        this.stage += 1;
        this.completed = stage == 5;
        this.check = true;
    }

    @Override
    public void decreaseStep() {
        this.stage -= 1;
        this.completed = stage == -1;
        this.check = true;
    }
}
