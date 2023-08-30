package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.TestCqlProperties;
import ca.uhn.fhir.cr.TestCrConfig;


import ca.uhn.fhir.cr.config.dstu3.CrDstu3Config;


import org.cqframework.cql.cql2elm.CqlCompilerOptions;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;

import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.opencds.cqf.fhir.utility.ValidationProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


import java.util.EnumSet;
import java.util.Map;
import java.util.Set;


@Configuration
@Import({TestCrConfig.class, CrDstu3Config.class})
public class TestCrDstu3Config {

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
		var cqlEngineOptions = evaluationSettings.getEngineOptions();
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

		var cqlCompilerOptions = new CqlCompilerOptions();
//				theCqlProperties.getCqlTranslatorFormat(),
//			theCqlProperties.isEnableDateRangeOptimization(), theCqlProperties.isEnableAnnotations(),
//			theCqlProperties.isEnableLocators(), theCqlProperties.isEnableResultsType(),
//			theCqlProperties.isCqlCompilerVerifyOnly(), theCqlProperties.isEnableDetailedErrors(),
//			theCqlProperties.getCqlCompilerErrorSeverityLevel(), theCqlProperties.isDisableListTraversal(),
//			theCqlProperties.isDisableListDemotion(), theCqlProperties.isDisableListPromotion(),
//			theCqlProperties.isEnableIntervalDemotion(), theCqlProperties.isEnableIntervalPromotion(),
//			theCqlProperties.isDisableMethodInvocation(), theCqlProperties.isRequireFromKeyword(),
//			theCqlProperties.isCqlCompilerValidateUnits(), theCqlProperties.isDisableDefaultModelInfoLoad(),
//			theCqlProperties.getCqlCompilerSignatureLevel(), theCqlProperties.getCqlCompilerCompatibilityLevel()
//		);
		cqlCompilerOptions.setCompatibilityLevel("1.3");
		cqlCompilerOptions.setAnalyzeDataRequirements(theCqlProperties.isCqlCompilerAnalyzeDataRequirements());
		cqlCompilerOptions.setCollapseDataRequirements(theCqlProperties.isCqlCompilerCollapseDataRequirements());

		cqlOptions.setCqlCompilerOptions(cqlCompilerOptions);

		return evaluationSettings;
	}
}
