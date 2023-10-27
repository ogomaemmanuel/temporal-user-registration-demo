package com.safaricom.dxl.userregistationworkflowworker;

import com.safaricom.dxl.tempral_api.mail.MailSender;
import com.safaricom.dxl.tempral_api.repositories.UserRepository;
import com.safaricom.dxl.tempral_api.services.UserRegistrationService;
import com.safaricom.dxl.tempral_api.workflows.activities.UserRegistrationActivitiesImpl;
import io.temporal.worker.WorkerFactory;
import org.springframework.stereotype.Component;
import com.safaricom.dxl.tempral_api.workflows.UserRegistrationWorkflowImpl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
public class UserRegistrationWorker {
    private final UserRepository userRepository;
    private final WorkerFactory workerFactory;

    private final MailSender mailSender;

    public UserRegistrationWorker(UserRepository userRepository, WorkerFactory workerFactory, MailSender mailSender) {
        this.userRepository = userRepository;
        this.workerFactory = workerFactory;
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void start() {
        var worker = workerFactory.newWorker(UserRegistrationService.USER_REG_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(UserRegistrationWorkflowImpl.class);
        worker.registerActivitiesImplementations(new UserRegistrationActivitiesImpl(userRepository, mailSender));
        workerFactory.start();
    }

    @PreDestroy
    public void destroy() {
        if (workerFactory != null) {
            workerFactory.shutdown();
        }
    }


}
