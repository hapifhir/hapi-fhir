package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.TestCqlProperties;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.common.CqlThreadFactory;
import ca.uhn.fhir.cr.config.r4.R4MeasureConfig;
import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.cql.evaluator.activitydefinition.r4.ActivityDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.fhir.util.ValidationProfile;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.opencds.cqf.cql.evaluator.measure.CareGapsProperties;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor;
import org.opencds.cqf.cql.evaluator.questionnaireresponse.r4.QuestionnaireResponseProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Import({TestCrConfig.class, R4MeasureConfig.class
})
public class TestCrR4Config {
	@Bean
	public ExecutorService cqlExecutor() {
		CqlThreadFactory factory = new CqlThreadFactory();
		ExecutorService executor = Executors.
			newFixedThreadPool(2
				,  factory);
		executor = new DelegatingSecurityContextExecutorService(executor);

		return executor;
	}
	@Bean
	CareGapsProperties careGapsProperties()  {
		var careGapsProperties = new CareGapsProperties();
		careGapsProperties.setThreadedCareGapsEnabled(false);
		careGapsProperties.setCareGapsReporter("Organization/alphora");
		careGapsProperties.setCareGapsCompositionSectionAuthor("Organization/alphora-author");
		return careGapsProperties;
	}

	@Bean
	MeasureEvaluationOptions measureEvaluationOptions(EvaluationSettings theEvaluationSettings, Map<String, ValidationProfile> theValidationProfiles){
		MeasureEvaluationOptions measureEvalOptions = new MeasureEvaluationOptions();
		measureEvalOptions.setEvaluationSettings(theEvaluationSettings);
		if(measureEvalOptions.isValidationEnabled()) {
			measureEvalOptions.setValidationProfiles(theValidationProfiles);
		}
		return measureEvalOptions;
	}
	@Bean
	IActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory(EvaluationSettings theEvaluationSettings) {
		return r -> new ActivityDefinitionProcessor(r, theEvaluationSettings);
	}

	@Bean
	public ActivityDefinitionOperationsProvider r4ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	@Bean
	IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(EvaluationSettings theEvaluationSettings) {
		return r -> new PlanDefinitionProcessor(r, theEvaluationSettings);
	}

	@Bean
	public PlanDefinitionOperationsProvider r4PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

	@Bean
	IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory() {
		return r -> new QuestionnaireProcessor(r);
	}

	@Bean
	public QuestionnaireOperationsProvider r4QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	@Bean
	IQuestionnaireResponseProcessorFactory r4QuestionnaireResponseProcessorFactory() {
		return r -> new QuestionnaireResponseProcessor(r);
	}

	@Bean
	public QuestionnaireResponseOperationsProvider r4QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}

	@Bean
	public EvaluationSettings evaluationSettings(TestCqlProperties theCqlProperties) {
		var evaluationSettings = EvaluationSettings.getDefault();
		var cqlEngineOptions = evaluationSettings.getCqlOptions().getCqlEngineOptions();
		Set<CqlEngine.Options> options = EnumSet.noneOf(CqlEngine.Options.class);
		if (theCqlProperties.isCqlRuntimeEnableExpressionCaching()) {
			options.add(CqlEngine.Options.EnableExpressionCaching);
		}
		if (theCqlProperties.isCqlRuntimeEnableValidation()) {
			options.add(CqlEngine.Options.EnableValidation);
		}
		cqlEngineOptions.setOptions(options);

		var cqlOptions = evaluationSettings.getCqlOptions();
		cqlOptions.setCqlEngineOptions(cqlEngineOptions);

		var cqlTranslatorOptions = new CqlTranslatorOptions(
			theCqlProperties.getCqlTranslatorFormat(),
			theCqlProperties.isEnableDateRangeOptimization(),
			theCqlProperties.isEnableAnnotations(),
			theCqlProperties.isEnableLocators(),
			theCqlProperties.isEnableResultsType(),
			theCqlProperties.isCqlCompilerVerifyOnly(),
			theCqlProperties.isEnableDetailedErrors(),
			theCqlProperties.getCqlCompilerErrorSeverityLevel(),
			theCqlProperties.isDisableListTraversal(),
			theCqlProperties.isDisableListDemotion(),
			theCqlProperties.isDisableListPromotion(),
			theCqlProperties.isEnableIntervalDemotion(),
			theCqlProperties.isEnableIntervalPromotion(),
			theCqlProperties.isDisableMethodInvocation(),
			theCqlProperties.isRequireFromKeyword(),
			theCqlProperties.isCqlCompilerValidateUnits(),
			theCqlProperties.isDisableDefaultModelInfoLoad(),
			theCqlProperties.getCqlCompilerSignatureLevel(),
			theCqlProperties.getCqlCompilerCompatibilityLevel()
		);
		theCqlProperties.setCqlUseEmbeddedLibraries(false);
		cqlTranslatorOptions.setCompatibilityLevel("1.5");
		cqlTranslatorOptions.setAnalyzeDataRequirements(theCqlProperties.isCqlCompilerAnalyzeDataRequirements());
		cqlTranslatorOptions.setCollapseDataRequirements(theCqlProperties.isCqlCompilerCollapseDataRequirements());
		cqlTranslatorOptions.setEnableCqlOnly(true);

		cqlOptions.setCqlTranslatorOptions(cqlTranslatorOptions);

		return evaluationSettings;
	}
}
