package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

	@MockitoBean
	private IInterceptorService myInterceptorService;

	// These beans are only needed to satisfy the dependencies of the configurations under test. They can not be
	// declared on MyConfig, since @MockitoBean is not supported on @Configuration classes.
	@MockitoBean
	private IIdHelperService<JpaPid> myIdHelperService;
	@MockitoBean
	private StorageSettings myStorageSettings;
	@MockitoBean
	private ISearchParamProvider mySearchParamProvider;
	@MockitoBean
	private IValidationSupport myValidationSupport;
	@MockitoBean
	private SubscriptionChannelFactory mySubscriptionChannelFactory;
	@MockitoBean
	private DaoRegistry myDaoRegistry;
	@MockitoBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockitoBean
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@MockitoBean
	private PlatformTransactionManager myPlatformTransactionManager;
	@MockitoBean
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	@MockitoBean
	private IHapiTransactionService myHapiTransactionService;

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
	}


}
