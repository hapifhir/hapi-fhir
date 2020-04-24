package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@Import({
	SubscriptionSubmitterConfig.class,
	SubscriptionProcessorConfig.class,
	SubscriptionChannelConfig.class
})
public class TestJPAConfig {

	@Bean
	public DaoConfig daoConfig() {
		return new DaoConfig();
	}

	@Bean
	public ModelConfig modelConfig() {
		return daoConfig().getModelConfig();
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	@Lazy
	@Bean
	public SubscriptionTestUtil subscriptionTestUtil() {
		return new SubscriptionTestUtil();
	}

	@Bean
	@Primary
	public SubscriptionDeliveringRestHookSubscriber stoppableSubscriptionDeliveringRestHookSubscriber() {
		return new StoppableSubscriptionDeliveringRestHookSubscriber();
	}
}
