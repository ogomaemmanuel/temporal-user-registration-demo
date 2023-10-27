package com.safaricom.dxl.tempral_api.config;

import com.google.protobuf.util.Durations;
import com.safaricom.dxl.tempral_api.workflows.data_converters.UserDtoDataConverter;
import io.grpc.StatusRuntimeException;
import io.temporal.api.workflowservice.v1.RegisterNamespaceRequest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class TemporalConfig {
    @Value("${temporal.namespace:default}")
    private String namespace;
    @Value("${temporal.endpoint:127.0.0.1:7233}")
    private String temporalEndpoint;

    @Bean
    WorkflowClient workflowClient() {
        UserDtoDataConverter dataConverter = new UserDtoDataConverter("Zr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?");
        WorkflowServiceStubs service = WorkflowServiceStubs
                .newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(temporalEndpoint).build());
        registerNewNameSpace(service);
        var clientOptions = WorkflowClientOptions.newBuilder()
//                .setDataConverter(
//
//                )
                .setNamespace(namespace)

                .build();
        WorkflowClient client = WorkflowClient.newInstance(service, clientOptions);
        return client;
    }

    @Bean
    WorkerFactory workerFactory() {

        WorkerFactory factory = WorkerFactory.newInstance(workflowClient());
        return factory;
    }

    private void registerNewNameSpace(WorkflowServiceStubs serviceStubs) {
        try {
            if (namespace != "default") {
                RegisterNamespaceRequest request =
                        RegisterNamespaceRequest.newBuilder()
                                .setNamespace(namespace)

                                .setWorkflowExecutionRetentionPeriod(Durations.fromDays(7))
                                .build();
                serviceStubs.blockingStub().registerNamespace(request);
            }
        } catch (StatusRuntimeException e) {
            log.info("Failed to register new namespace with name {} : {}", namespace, e.getMessage());
        }
    }
}
