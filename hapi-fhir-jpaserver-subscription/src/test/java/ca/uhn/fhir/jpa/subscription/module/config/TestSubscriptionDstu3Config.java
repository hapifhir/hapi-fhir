package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
@Import(TestSubscriptionConfig.class)
public class TestSubscriptionDstu3Config {

	@Bean
	public FhirContext fhirContext() {
		return FhirContext.forDstu3();
	}

	@Bean
	public IValidationSupport validationSupport() {
		return FhirContext.forDstu3().getValidationSupport();
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
