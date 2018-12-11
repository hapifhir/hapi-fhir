package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.dbmatcher.SubscriptionMatcherCompositeInMemoryDatabase;
import ca.uhn.fhir.jpa.subscription.dbmatcher.SubscriptionMatcherDatabase;
import ca.uhn.fhir.jpa.subscription.matcher.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.matcher.SubscriptionMatcherInMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class TestJPAConfig {

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig daoConfig = new DaoConfig();
		return daoConfig;
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

	@Bean
	public UnregisterScheduledProcessor unregisterScheduledProcessor(Environment theEnv) {
		return new UnregisterScheduledProcessor(theEnv);
	}

	@Bean
	public SubscriptionMatcherInMemory subscriptionMatcherInMemory() {
		return new SubscriptionMatcherInMemory();
	}

	@Bean
	public SubscriptionMatcherDatabase subscriptionMatcherDatabase() {
		return new SubscriptionMatcherDatabase();
	}

	@Bean
	@Primary
	public ISubscriptionMatcher subscriptionMatcherCompositeInMemoryDatabase() {
		return new SubscriptionMatcherCompositeInMemoryDatabase(subscriptionMatcherDatabase(), subscriptionMatcherInMemory());
	}

	@Lazy
	@Bean
	public SubscriptionTestUtil subscriptionInterceptorRegistrationUtil() {
		return new SubscriptionTestUtil();
	}
}
