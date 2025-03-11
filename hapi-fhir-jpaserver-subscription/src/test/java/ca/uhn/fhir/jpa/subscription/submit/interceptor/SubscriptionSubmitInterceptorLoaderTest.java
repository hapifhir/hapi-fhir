package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.hl7.fhir.dstu2.model.Subscription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	SubscriptionSubmitterConfig.class,
	SearchParamConfig.class,
	SubscriptionSubmitInterceptorLoaderTest.MyConfig.class
})
public class SubscriptionSubmitInterceptorLoaderTest {

	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@MockBean
	private IInterceptorService myInterceptorService;

	/**
	 * It should be possible to run only the {@link SubscriptionSubmitterConfig} without the
	 * {@link ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig}
	 */
	@Test
	public void testLoaderCanRunWithoutProcessorConfigLoaded() {
		verify(myInterceptorService, times(1)).registerInterceptor(eq(mySubscriptionMatcherInterceptor));
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean
		public PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}

		@Bean
		public SubscriptionSettings subscriptionSettings() {
			SubscriptionSettings subscriptionSettings = new SubscriptionSettings();
			subscriptionSettings.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.RESTHOOK);
			return subscriptionSettings;
		}

		@MockBean
		public StorageSettings myStorageSettings;
		@MockBean
		private ISearchParamProvider mySearchParamProvider;
		@MockBean
		private IValidationSupport myValidationSupport;
		@MockBean
		private SubscriptionChannelFactory mySubscriptionChannelFactory;
		@MockBean
		private DaoRegistry myDaoRegistry;
		@MockBean
		private IResourceVersionSvc myResourceVersionSvc;
		@MockBean
		private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
		@MockBean
		private PlatformTransactionManager myPlatformTransactionManager;
		@MockBean
		private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
		@MockBean
		private IHapiTransactionService myHapiTransactionService;

	}


}
