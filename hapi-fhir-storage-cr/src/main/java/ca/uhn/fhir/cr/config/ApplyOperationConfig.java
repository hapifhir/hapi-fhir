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

public class ApplyOperationConfig {

	@Bean
	ca.uhn.fhir.cr.r4.IActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.activitydefinition.r4.ActivityDefinitionProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(
		IRepositoryFactory theRepositoryFactory,
		EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor(
			theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider r4ActivityDefinitionApplyProvider() {
		return new ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider r4PlanDefinitionApplyProvider() {
		return new ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider();
	}

	@Bean(name = "applyOperationLoader")
	public ProviderLoader applyOperationLoader(
		ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {

		var selector = new ProviderSelector(
			theFhirContext,
			Map.of(
				FhirVersionEnum.R4,
				Arrays.asList(
					ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider.class,
					ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider.class))
		);

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
