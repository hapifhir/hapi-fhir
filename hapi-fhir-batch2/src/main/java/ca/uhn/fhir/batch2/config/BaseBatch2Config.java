package ca.uhn.fhir.batch2.config;

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
	public abstract IJobPersistence batch2JobInstancePersister();

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
			.setConcurrentConsumers(1);
		return theChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public IChannelReceiver batch2ProcessingChannelReceiver(@Autowired IChannelFactory theChannelFactory) {
		ChannelConsumerSettings settings = new ChannelConsumerSettings()
			.setConcurrentConsumers(1);
		return theChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, settings);
	}

	@Bean
	public Batch2JobRegisterer batch2JobRegisterer() {
		return new Batch2JobRegisterer();
	}


}
