package ca.uhn.fhir.cr.config.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.config.ProviderLoader;
import ca.uhn.fhir.cr.config.ProviderSelector;
import ca.uhn.fhir.cr.config.RepositoryConfig;
import ca.uhn.fhir.cr.dstu3.IMeasureServiceFactory;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.cr.r4.measure.CareGapsOperationProvider;
import ca.uhn.fhir.cr.r4.measure.SubmitDataProvider;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import java.util.Map;

@Configuration
@Import({RepositoryConfig.class})
public class Dstu3MeasureConfig {
	@Bean
	IMeasureServiceFactory dstu3MeasureServiceFactory(
			IRepositoryFactory theRepositoryFactory, MeasureEvaluationOptions theEvaluationOptions) {
		return rd -> new MeasureService(theRepositoryFactory.create(rd), theEvaluationOptions);
	}

	@Bean
	MeasureOperationsProvider dstu3MeasureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	@Bean
	public ProviderLoader dstu3PdLoader(
		ApplicationContext theApplicationContext,
		FhirContext theFhirContext,
		ResourceProviderFactory theResourceProviderFactory) {

		var selector = new ProviderSelector(
			theFhirContext,
			Map.of(
				 MeasureOperationsProvider.class, FhirVersionEnum.DSTU3
			));

		return new ProviderLoader(theApplicationContext, theResourceProviderFactory, selector);
	}
}
