package ca.uhn.fhir.cr.config.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.config.RepositoryConfig;
import ca.uhn.fhir.cr.dstu3.IMeasureServiceFactory;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	RepositoryConfig.class
})
public class Dstu3MeasureConfig {
	@Bean
	IMeasureServiceFactory dstu3MeasureServiceFactory(IRepositoryFactory theRepositoryFactory, MeasureEvaluationOptions theEvaluationOptions) {
		return rd -> new MeasureService(theRepositoryFactory.create(rd), theEvaluationOptions);
	}

	@Bean
	MeasureOperationsProvider dstu3MeasureOperationsProvider(){return new MeasureOperationsProvider();}

	@Bean
	Dstu3MeasureProviderFactory dstu3MeasureProviderFactory() {
		return new Dstu3MeasureProviderFactory();
	}

	@Bean
	Dstu3MeasureProviderLoader dstu3MeasureProviderLoader(
		FhirContext theFhirContext,
		ResourceProviderFactory theResourceProviderFactory,
		Dstu3MeasureProviderFactory theDstu3MeasureProviderFactory) {
		return new Dstu3MeasureProviderLoader(theFhirContext, theResourceProviderFactory, theDstu3MeasureProviderFactory);
	}
}
