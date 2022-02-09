package ca.uhn.fhir.jpa.batch2.config;

import ca.uhn.fhir.jpa.batch2.api.IJobDefinitionRegistry;
import ca.uhn.fhir.jpa.batch2.api.IJobInstancePersister;
import ca.uhn.fhir.jpa.batch2.impl.JobDefinitionRegistryImpl;
import ca.uhn.fhir.jpa.batch2.model.JobWorkNotification;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseBatch2AppCtx {

	public static final String CHANNEL_NAME = "batch2-work-notification";

	@Bean
	public IJobDefinitionRegistry jobDefinitionRegistry() {
		return new JobDefinitionRegistryImpl();
	}

	public abstract IJobInstancePersister batchJobInstancePersister();

	public abstract IChannelFactory batch2ChannelFactory();

	@Bean
	public IChannelProducer batchProcessingChannelProducer() {
		ChannelProducerSettings settings = new ChannelProducerSettings()
			.setConcurrentConsumers(1);
		return batch2ChannelFactory().getOrCreateProducer(CHANNEL_NAME, JobWorkNotification.class, settings);
	}

	@Bean
	public IChannelReceiver batchProcessingChannelReceiver() {
		ChannelConsumerSettings settings = new ChannelConsumerSettings()
			.setConcurrentConsumers(1);
		return batch2ChannelFactory().getOrCreateReceiver(CHANNEL_NAME, JobWorkNotification.class, settings);
	}

}
