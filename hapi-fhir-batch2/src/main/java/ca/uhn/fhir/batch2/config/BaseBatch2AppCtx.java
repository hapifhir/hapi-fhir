package ca.uhn.fhir.batch2.config;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.impl.JobCoordinatorImpl;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseBatch2AppCtx {

	public static final String CHANNEL_NAME = "batch2-work-notification";

	@Bean
	public JobDefinitionRegistry jobDefinitionRegistry() {
		return new JobDefinitionRegistry();
	}

	@Bean
	public abstract IJobPersistence batchJobInstancePersister();

	@Bean
	public IJobCoordinator jobCoordinator(@Autowired IChannelFactory theChannelFactory, IJobPersistence theJobInstancePersister, JobDefinitionRegistry theJobDefinitionRegistry) {
		return new JobCoordinatorImpl(
			batchProcessingChannelProducer(theChannelFactory),
			batchProcessingChannelReceiver(theChannelFactory),
			theJobInstancePersister,
			theJobDefinitionRegistry
		);
	}

	@Bean
	public IChannelProducer batchProcessingChannelProducer(@Autowired IChannelFactory theChannelFactory) {
		ChannelProducerSettings settings = new ChannelProducerSettings()
			.setConcurrentConsumers(1);
		return theChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public IChannelReceiver batchProcessingChannelReceiver(@Autowired IChannelFactory theChannelFactory) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings()
			.setConcurrentConsumers(1);
		return theChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public Batch2JobRegisterer batch2JobRegisterer() {
		return new Batch2JobRegisterer();
	}


}
