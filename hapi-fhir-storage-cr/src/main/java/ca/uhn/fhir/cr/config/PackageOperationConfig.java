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

public class PackageOperationConfig {
	@Bean
	ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider r4PlanDefinitionPackageProvider() {
		return new ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider r4QuestionnairePackageProvider() {
		return new ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider();
	}

	@Bean(name = "packageOperationLoader")
	public ProviderLoader packageOperationLoader(
		ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
			theFhirContext,
			Map.of(
				FhirVersionEnum.R4,
				Arrays.asList(ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider.class,
					ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider.class))
		);

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
