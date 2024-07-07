/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.coordinator.JobCoordinatorImpl;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.coordinator.ReductionStepExecutorServiceImpl;
import ca.uhn.fhir.batch2.coordinator.SimpleJobPartitionProvider;
import ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseBatch2Config {

	public static final String CHANNEL_NAME = "batch2-work-notification";

	@Autowired
	IJobPersistence myPersistence;

	@Autowired
	IChannelFactory myChannelFactory;

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
		return new BatchJobSender(batch2ProcessingChannelProducer(myChannelFactory));
	}

	@Bean
	public IJobCoordinator batch2JobCoordinator(
			JobDefinitionRegistry theJobDefinitionRegistry,
			BatchJobSender theBatchJobSender,
			WorkChunkProcessor theExecutor,
			IJobMaintenanceService theJobMaintenanceService,
			IHapiTransactionService theTransactionService) {
		return new JobCoordinatorImpl(
				theBatchJobSender,
				batch2ProcessingChannelReceiver(myChannelFactory),
				myPersistence,
				theJobDefinitionRegistry,
				theExecutor,
				theJobMaintenanceService,
				theTransactionService);
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
	public IChannelProducer batch2ProcessingChannelProducer(IChannelFactory theChannelFactory) {
		ChannelProducerSettings settings =
				new ChannelProducerSettings().setConcurrentConsumers(getConcurrentConsumers());
		return theChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public IChannelReceiver batch2ProcessingChannelReceiver(IChannelFactory theChannelFactory) {
		ChannelConsumerSettings settings =
				new ChannelConsumerSettings().setConcurrentConsumers(getConcurrentConsumers());
		return theChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
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
	public IJobPartitionProvider jobPartitionProvider(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new SimpleJobPartitionProvider(theRequestPartitionHelperSvc);
	}
}
