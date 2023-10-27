package com.safaricom.dxl.tempral_api;

import com.safaricom.dxl.tempral_api.workflows.data_converters.UserDtoDataConverter;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class TemporalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemporalApiApplication.class, args);
	}



}
