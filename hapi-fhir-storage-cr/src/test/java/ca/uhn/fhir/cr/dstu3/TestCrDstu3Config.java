package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.TestCqlProperties;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.config.dstu3.CrDstu3Config;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.cql.evaluator.fhir.util.ValidationProfile;
import org.opencds.cqf.cql.evaluator.library.EvaluationSettings;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Configuration
@Import({TestCrConfig.class, CrDstu3Config.class})
public class TestCrDstu3Config {

	@Bean
	MeasureEvaluationOptions measureEvaluationOptions(EvaluationSettings theEvaluationSettings, Map<String, ValidationProfile> theValidationProfiles){
		MeasureEvaluationOptions measureEvalOptions = new MeasureEvaluationOptions();
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
		cqlTranslatorOptions.setCompatibilityLevel("1.3");
		cqlTranslatorOptions.setAnalyzeDataRequirements(theCqlProperties.isCqlCompilerAnalyzeDataRequirements());
		cqlTranslatorOptions.setCollapseDataRequirements(theCqlProperties.isCqlCompilerCollapseDataRequirements());
		//cqlTranslatorOptions.set
		cqlOptions.setCqlTranslatorOptions(cqlTranslatorOptions);

		return evaluationSettings;
	}
}
