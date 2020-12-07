package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
@Import(TestSubscriptionConfig.class)
public class TestSubscriptionDstu3Config {
	private static final Logger ourLog = LoggerFactory.getLogger(TestSubscriptionDstu3Config.class);

	private static final FhirContext ourFhirContext = FhirContext.forDstu3();

	@Bean
	public FhirContext fhirContext() {
		return ourFhirContext;
	}

	@Bean
	public IValidationSupport validationSupport(FhirContext theFhirContext) {
		return theFhirContext.getValidationSupport();
	}

	@Bean
	@Primary
	public ISearchParamProvider searchParamProvider() {
		return new MockFhirClientSearchParamProvider();
	}

	@Bean
	public ISchedulerService schedulerService() {
		return mock(ISchedulerService.class);
	}

	@Bean
	public DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

}
