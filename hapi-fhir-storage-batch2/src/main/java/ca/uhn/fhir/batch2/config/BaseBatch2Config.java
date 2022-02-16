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

import ca.uhn.fhir.batch2.api.IJobCleanerService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.impl.JobCleanerServiceImpl;
import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.impl.JobCoordinatorImpl;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
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

	@Bean
	public JobDefinitionRegistry batch2JobDefinitionRegistry() {
		return new JobDefinitionRegistry();
	}

	@Bean
	public IJobCoordinator batch2JobCoordinator(@Autowired IChannelFactory theChannelFactory, IJobPersistence theJobInstancePersister, JobDefinitionRegistry theJobDefinitionRegistry) {
		return new JobCoordinatorImpl(
			batch2ProcessingChannelProducer(theChannelFactory),
			batch2ProcessingChannelReceiver(theChannelFactory),
			theJobInstancePersister,
			theJobDefinitionRegistry
		);
	}

	@Bean
	public IJobCleanerService batch2JobCleaner(@Autowired ISchedulerService theSchedulerService, @Autowired IJobPersistence theJobPersistence) {
		return new JobCleanerServiceImpl(theSchedulerService, theJobPersistence);
	}

	@Bean
	public IChannelProducer batch2ProcessingChannelProducer(@Autowired IChannelFactory theChannelFactory) {
		ChannelProducerSettings settings = new ChannelProducerSettings()
			.setConcurrentConsumers(10);
		return theChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public IChannelReceiver batch2ProcessingChannelReceiver(@Autowired IChannelFactory theChannelFactory) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings()
			.setConcurrentConsumers(10);
		return theChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public Batch2JobRegisterer batch2JobRegisterer() {
		return new Batch2JobRegisterer();
	}


}
