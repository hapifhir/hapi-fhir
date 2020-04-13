package ca.uhn.fhir.jpa.subscription.match.matcher.matching;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
	SubscriptionProcessorConfig.class,
	SearchParamConfig.class,
	DaoSubscriptionMatcherTest.MyConfig.class
})
public class DaoSubscriptionMatcherTest {

	@Autowired(required = false)
	private PlatformTransactionManager myTxManager;
	@Autowired
	private DaoSubscriptionMatcher mySvc;
	@MockBean
	private ModelConfig myModelConfig;
	@MockBean
	private DaoConfig myDaoConfig;
	@MockBean
	private ISearchParamProvider mySearchParamProvider;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IInterceptorService myInterceptorService;
	@MockBean
	private DaoRegistry myDaoRegistry;
	@MockBean
	private IValidationSupport myValidationSupport;
	@MockBean
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

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
		public FhirContext fhirContext() {
			return FhirContext.forR4();
		}

	}

}
