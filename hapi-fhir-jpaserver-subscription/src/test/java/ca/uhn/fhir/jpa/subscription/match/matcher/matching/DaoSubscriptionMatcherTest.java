package ca.uhn.fhir.jpa.subscription.match.matcher.matching;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionQueryValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	SubscriptionProcessorConfig.class,
	SearchParamConfig.class,
	DaoSubscriptionMatcherTest.MyConfig.class
})
public class DaoSubscriptionMatcherTest {

	@Autowired(required = false)
	private PlatformTransactionManager myTxManager;
	@MockBean
	private JpaStorageSettings myStorageSettings;
	@MockBean
	private ISearchParamProvider mySearchParamProvider;
	@MockBean
	private IInterceptorService myInterceptorService;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private IValidationSupport myValidationSupport;
	@MockBean
	private SubscriptionChannelFactory mySubscriptionChannelFactory;
	@MockBean
	private SubscriptionQueryValidator mySubscriptionQueryValidator;

	/**
	 * Make sure that if we're only running the {@link SubscriptionSubmitterConfig}, we don't need
	 * a transaction manager
	 */
	@Test
	public void testSubmitterCanRunWithoutTransactionManager() {
		assertNull(myTxManager);
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}

		@Bean
		public FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean
		public IResourceVersionSvc resourceVersionSvc() {
			return mock(IResourceVersionSvc.class, RETURNS_DEEP_STUBS);
		}

		@Bean
		public IRequestPartitionHelperSvc requestPartitionHelperSvc() {
			return mock(IRequestPartitionHelperSvc.class);
		}

		@Bean
		public IEmailSender emailSender(){
			return mock(IEmailSender.class);
		}
	}

}
