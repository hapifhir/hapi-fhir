/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.batch2.config;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.DefaultJobPartitionProvider;
import ca.uhn.fhir.batch2.coordinator.JobCoordinatorImpl;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.coordinator.ReductionStepExecutorServiceImpl;
import ca.uhn.fhir.batch2.coordinator.WorkChannelMessageListener;
import ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseBatch2Config {

	public static final String CHANNEL_NAME = "batch2-work-notification";

	@Autowired
	IJobPersistence myPersistence;

	@Autowired
	IBrokerClient myBrokerClient;

	@Autowired
	IHapiTransactionService myHapiTransactionService;

	@Bean
	public JobDefinitionRegistry batch2JobDefinitionRegistry() {
		return new JobDefinitionRegistry();
	}

	@Bean
	public WorkChunkProcessor jobStepExecutorService(BatchJobSender theBatchJobSender) {
		return new WorkChunkProcessor(myPersistence, theBatchJobSender, myHapiTransactionService);
	}

	@Bean
	public BatchJobSender batchJobSender() {
		return new BatchJobSender(batch2ProcessingChannelProducer(myBrokerClient));
	}

	@Bean
	public IJobCoordinator batch2JobCoordinator(
			JobDefinitionRegistry theJobDefinitionRegistry,
			IHapiTransactionService theTransactionService) {
		return new JobCoordinatorImpl(
                myPersistence, theJobDefinitionRegistry, theTransactionService);
	}

	@Bean
	public IReductionStepExecutorService reductionStepExecutorService(
			IJobPersistence theJobPersistence,
			IHapiTransactionService theTransactionService,
			JobDefinitionRegistry theJobDefinitionRegistry) {
		return new ReductionStepExecutorServiceImpl(theJobPersistence, theTransactionService, theJobDefinitionRegistry);
	}

	@Bean
	public IJobMaintenanceService batch2JobMaintenanceService(
			ISchedulerService theSchedulerService,
			JobDefinitionRegistry theJobDefinitionRegistry,
			JpaStorageSettings theStorageSettings,
			BatchJobSender theBatchJobSender,
			WorkChunkProcessor theExecutor,
			IReductionStepExecutorService theReductionStepExecutorService) {
		return new JobMaintenanceServiceImpl(
				theSchedulerService,
				myPersistence,
				theStorageSettings,
				theJobDefinitionRegistry,
				theBatchJobSender,
				theExecutor,
				theReductionStepExecutorService);
	}

	@Bean
	public IChannelProducer<JobWorkNotification> batch2ProcessingChannelProducer(IBrokerClient theBrokerClient) {
		ChannelProducerSettings settings =
				new ChannelProducerSettings().setConcurrentConsumers(getConcurrentConsumers());
		return theBrokerClient.getOrCreateProducer(CHANNEL_NAME, JobWorkNotification.class, settings);
	}

	@Bean
	public WorkChannelMessageListener workChannelMessageListener(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull BatchJobSender theBatchJobSender,
			@Nonnull WorkChunkProcessor theExecutorSvc,
			@Nonnull IJobMaintenanceService theJobMaintenanceService,
			IHapiTransactionService theHapiTransactionService) {
		return new WorkChannelMessageListener(
				theJobPersistence,
				theJobDefinitionRegistry,
				theBatchJobSender,
				theExecutorSvc,
				theJobMaintenanceService,
				theHapiTransactionService);
	}

	@Bean
	public IChannelConsumer<JobWorkNotification> batch2ProcessingChannelConsumer(
			IBrokerClient theBrokerClient, WorkChannelMessageListener theWorkChannelMessageListener) {
		ChannelConsumerSettings settings =
				new ChannelConsumerSettings().setConcurrentConsumers(getConcurrentConsumers());
		return theBrokerClient.getOrCreateConsumer(
				CHANNEL_NAME, JobWorkNotification.class, theWorkChannelMessageListener, settings);
	}

	@Bean
	public Batch2JobRegisterer batch2JobRegisterer() {
		return new Batch2JobRegisterer();
	}

	/**
	 * Can be overridden
	 */
	protected int getConcurrentConsumers() {
		return 4;
	}

	@Bean
	public IJobPartitionProvider jobPartitionProvider(
			FhirContext theFhirContext,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			MatchUrlService theMatchUrlService) {
		return new DefaultJobPartitionProvider(theFhirContext, theRequestPartitionHelperSvc, theMatchUrlService);
	}
}
