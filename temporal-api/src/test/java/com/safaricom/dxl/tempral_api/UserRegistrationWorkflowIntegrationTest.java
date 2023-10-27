package com.safaricom.dxl.tempral_api;

import com.safaricom.dxl.tempral_api.dtos.UserDto;
import com.safaricom.dxl.tempral_api.workflows.UserRegistrationWorkflow;
import com.safaricom.dxl.tempral_api.workflows.UserRegistrationWorkflowImpl;
import com.safaricom.dxl.tempral_api.workflows.activities.UserRegistrationActivitiesImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.safaricom.dxl.tempral_api.services.UserRegistrationService.USER_REG_TASK_QUEUE;
import static org.mockito.Mockito.mock;

public class UserRegistrationWorkflowIntegrationTest {

    private TestWorkflowEnvironment testEnvironment;
    WorkflowClient client;

    Worker worker;

    @BeforeEach
    public void setup() {
        testEnvironment = TestWorkflowEnvironment.newInstance();
        worker = testEnvironment.newWorker(USER_REG_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(UserRegistrationWorkflowImpl.class);
        client = testEnvironment.getWorkflowClient();
    }

    @Test
    public void testStart(){
    UserRegistrationActivitiesImpl userRegistrationActivities=
            mock(UserRegistrationActivitiesImpl.class);

        worker.registerActivitiesImplementations(userRegistrationActivities);
        testEnvironment.start();
        UserRegistrationWorkflow workflow = client.newWorkflowStub(UserRegistrationWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(USER_REG_TASK_QUEUE).build());
        UserDto user = new UserDto();
        user.setEmail("user@example.com");
        workflow.start(user);
    }
    @AfterEach
    public void tearDown() {
        if(testEnvironment!=null) {
            testEnvironment.close();
        }
    }
}
