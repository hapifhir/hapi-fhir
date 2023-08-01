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

public class ExtractOperationConfig {
	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireResponseProcessorFactory r4QuestionnaireResponseProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.questionnaireresponse.r4.QuestionnaireResponseProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseExtractProvider r4QuestionnaireResponseExtractProvider() {
		return new ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseExtractProvider();
	}

	@Bean(name = "extractOperationLoader")
	public ProviderLoader extractOperationLoader(
		ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
			theFhirContext,
			Map.of(
				FhirVersionEnum.R4,
				Arrays.asList(ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseExtractProvider.class))
		);

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
