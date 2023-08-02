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
import ca.uhn.fhir.rest.server.RestfulServer;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
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
			ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
				theFhirContext, Map.of(FhirVersionEnum.DSTU3, Arrays.asList((MeasureOperationsProvider.class))));

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
