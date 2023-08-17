package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.TestCqlProperties;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.common.CqlThreadFactory;
import ca.uhn.fhir.cr.config.ApplyOperationConfig;
import ca.uhn.fhir.cr.config.ExtractOperationConfig;
import ca.uhn.fhir.cr.config.PackageOperationConfig;
import ca.uhn.fhir.cr.config.PopulateOperationConfig;
import ca.uhn.fhir.cr.config.r4.R4MeasureConfig;
import org.cqframework.cql.cql2elm.CqlCompilerOptions;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.cql.evaluator.fhir.util.ValidationProfile;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.opencds.cqf.cql.evaluator.measure.CareGapsProperties;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
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
@Import({
	TestCrConfig.class,
	R4MeasureConfig.class,
	ApplyOperationConfig.class,
	ExtractOperationConfig.class,
	PackageOperationConfig.class,
	PopulateOperationConfig.class
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
		cqlEngineOptions.setPageSize(1000);
		cqlEngineOptions.setMaxCodesPerQuery(10000);
		cqlEngineOptions.setShouldExpandValueSets(true);
		cqlEngineOptions.setQueryBatchThreshold(100000);

		var cqlOptions = evaluationSettings.getCqlOptions();
		cqlOptions.setCqlEngineOptions(cqlEngineOptions);

		var cqlCompilerOptions = new CqlCompilerOptions();
//			theCqlProperties.getCqlTranslatorFormat(),
//			theCqlProperties.isEnableDateRangeOptimization(),
//			theCqlProperties.isEnableAnnotations(),
//			theCqlProperties.isEnableLocators(),
//			theCqlProperties.isEnableResultsType(),
//			theCqlProperties.isCqlCompilerVerifyOnly(),
//			theCqlProperties.isEnableDetailedErrors(),
//			theCqlProperties.getCqlCompilerErrorSeverityLevel(),
//			theCqlProperties.isDisableListTraversal(),
//			theCqlProperties.isDisableListDemotion(),
//			theCqlProperties.isDisableListPromotion(),
//			theCqlProperties.isEnableIntervalDemotion(),
//			theCqlProperties.isEnableIntervalPromotion(),
//			theCqlProperties.isDisableMethodInvocation(),
//			theCqlProperties.isRequireFromKeyword(),
//			theCqlProperties.isCqlCompilerValidateUnits(),
//			theCqlProperties.isDisableDefaultModelInfoLoad(),
//			theCqlProperties.getCqlCompilerSignatureLevel(),
//			theCqlProperties.getCqlCompilerCompatibilityLevel()
//		);
		cqlCompilerOptions.setCompatibilityLevel(theCqlProperties.getCqlCompilerCompatibilityLevel());
		cqlCompilerOptions.setAnalyzeDataRequirements(theCqlProperties.isCqlCompilerAnalyzeDataRequirements());
		cqlCompilerOptions.setCollapseDataRequirements(theCqlProperties.isCqlCompilerCollapseDataRequirements());

		cqlOptions.setCqlCompilerOptions(cqlCompilerOptions);

		return evaluationSettings;
	}
}
