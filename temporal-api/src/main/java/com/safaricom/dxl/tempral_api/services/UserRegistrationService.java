package com.safaricom.dxl.tempral_api.services;
import com.safaricom.dxl.tempral_api.dtos.UserDto;
import com.safaricom.dxl.tempral_api.workflows.UserRegistrationWorkflow;
import com.safaricom.dxl.tempral_api.workflows.activities.UserRegistrationActivitiesImpl;
import com.safaricom.dxl.tempral_api.workflows.data_converters.UserDtoDataConverter;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    public static String USER_REG_TASK_QUEUE = "USER_REGISTRATION";
//    private final UserRegistrationActivitiesImpl userRegistrationActivities;
    private final WorkflowClient workflowClient;

    public UserRegistrationService( WorkflowClient workflowClient) {
//        this.userRegistrationActivities = userRegistrationActivities;
        this.workflowClient = workflowClient;
    }
    public void registerUser(UserDto user) {
        WorkflowOptions userRegistrationWorkflowOptions =
                WorkflowOptions.newBuilder()

                        .setWorkflowId(user.getUsername())
                        .setTaskQueue(USER_REG_TASK_QUEUE)

                        .build();
        var workflow = workflowClient.newWorkflowStub(UserRegistrationWorkflow.class, userRegistrationWorkflowOptions);
        WorkflowClient.start(workflow::start,user);
    }

    public void update(String username) {
        UserRegistrationWorkflow workFlow = workflowClient.newWorkflowStub(UserRegistrationWorkflow.class,username);
        workFlow.updateStep();

    }

    public void decrease(String username) {
        UserRegistrationWorkflow workFlow = workflowClient.newWorkflowStub(UserRegistrationWorkflow.class,username);
        workFlow.decreaseStep();
    }
}
