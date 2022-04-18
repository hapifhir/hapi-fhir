package ca.uhn.fhir.batch2.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.impl.BatchJobSender;
import ca.uhn.fhir.batch2.impl.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.impl.JobCoordinatorImpl;
import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseBatch2Config {

	public static final String CHANNEL_NAME = "batch2-work-notification";

	@Bean
	public JobDefinitionRegistry batch2JobDefinitionRegistry() {
		return new JobDefinitionRegistry();
	}

	@Bean
	public BatchJobSender batchJobSender(IChannelFactory theChannelFactory) {
		return new BatchJobSender(batch2ProcessingChannelProducer(theChannelFactory));
	}

	@Bean
	public IJobCoordinator batch2JobCoordinator(IChannelFactory theChannelFactory, IJobPersistence theJobInstancePersister, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		return new JobCoordinatorImpl(
			theBatchJobSender,
			batch2ProcessingChannelReceiver(theChannelFactory),
			theJobInstancePersister,
			theJobDefinitionRegistry
		);
	}

	@Bean
	public IJobMaintenanceService batch2JobMaintenanceService(ISchedulerService theSchedulerService, IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		return new JobMaintenanceServiceImpl(theSchedulerService, theJobPersistence, theJobDefinitionRegistry, theBatchJobSender);
	}

	@Bean
	public IChannelProducer batch2ProcessingChannelProducer(IChannelFactory theChannelFactory) {
		ChannelProducerSettings settings = new ChannelProducerSettings()
			.setConcurrentConsumers(getConcurrentConsumers());
		return theChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public IChannelReceiver batch2ProcessingChannelReceiver(IChannelFactory theChannelFactory) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings()
			.setConcurrentConsumers(getConcurrentConsumers());
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

}
