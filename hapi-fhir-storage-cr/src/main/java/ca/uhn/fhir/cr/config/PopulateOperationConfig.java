package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;

public class PopulateOperationConfig {
	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePopulateProvider r4QuestionnairePopulateProvider() {
		return new ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePopulateProvider();
	}

	@Bean(name = "populateOperationLoader")
	public ProviderLoader populateOperationLoader(
		ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
			theFhirContext,
			Map.of(
				FhirVersionEnum.R4,
				Arrays.asList(ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePopulateProvider.class))
		);

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
