package ca.uhn.fhir.cr.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.config.RepositoryConfig;
import ca.uhn.fhir.cr.r4.IMeasureServiceFactory;
import ca.uhn.fhir.cr.r4.ISubmitDataProcessorFactory;
import ca.uhn.fhir.cr.r4.measure.CareGapsOperationProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.cr.r4.measure.SubmitDataProvider;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.opencds.cqf.cql.evaluator.measure.r4.R4SubmitDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

@Configuration
@Import({
	RepositoryConfig.class
})
public class R4MeasureConfig {
	@Bean
	@Named("r4MeasureServiceFactory")
	IMeasureServiceFactory r4MeasureServiceFactory(IRepositoryFactory theRepositoryFactory, MeasureEvaluationOptions theEvaluationOptions) {
		return rd -> new MeasureService(theRepositoryFactory.create(rd), theEvaluationOptions);
	}
	@Bean
	CareGapsOperationProvider r4CareGapsOperationProvider() {
		return new CareGapsOperationProvider();
	}

	@Bean
	ISubmitDataProcessorFactory r4SubmitDataProcessorFactory(){
		return r -> new R4SubmitDataService(r);
	}

	@Bean
	SubmitDataProvider r4SubmitDataProvider(){
		return new SubmitDataProvider();
	}

	@Bean
	MeasureOperationsProvider r4MeasureOperationsProvider(){return new MeasureOperationsProvider();}

	@Bean
	R4MeasureProviderFactory r4MeasureProviderFactory() {
		return new R4MeasureProviderFactory();
	}

	@Bean
	R4MeasureProviderLoader r4MeasureProviderLoader(
		FhirContext theFhirContext,
		ResourceProviderFactory theResourceProviderFactory,
		R4MeasureProviderFactory theR4MeasureProviderFactory) {
		return new R4MeasureProviderLoader(theFhirContext, theResourceProviderFactory, theR4MeasureProviderFactory);
	}
}
