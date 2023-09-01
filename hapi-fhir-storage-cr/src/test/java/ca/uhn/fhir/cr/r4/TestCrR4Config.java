package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.TestCqlProperties;
import ca.uhn.fhir.cr.TestCrConfig;
import ca.uhn.fhir.cr.common.CqlThreadFactory;
import ca.uhn.fhir.cr.config.ApplyOperationConfig;
import ca.uhn.fhir.cr.config.ExtractOperationConfig;
import ca.uhn.fhir.cr.config.PackageOperationConfig;
import ca.uhn.fhir.cr.config.PopulateOperationConfig;
import ca.uhn.fhir.cr.config.r4.CrR4Config;
import org.cqframework.cql.cql2elm.CqlCompilerOptions;
import org.cqframework.cql.cql2elm.model.CompiledLibrary;
import org.cqframework.cql.cql2elm.model.Model;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.elm.r1.VersionedIdentifier;
import org.opencds.cqf.cql.engine.execution.CqlEngine;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.opencds.cqf.fhir.cr.measure.CareGapsProperties;
import org.opencds.cqf.fhir.cr.measure.MeasureEvaluationOptions;
import org.opencds.cqf.fhir.utility.ValidationProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Import({TestCrConfig.class, CrR4Config.class,
	ApplyOperationConfig.class,
	ExtractOperationConfig.class,
	PackageOperationConfig.class,
	PopulateOperationConfig.class
})
public class TestCrR4Config {
	@Primary
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
	public EvaluationSettings evaluationSettings(TestCqlProperties theCqlProperties, Map<VersionedIdentifier, CompiledLibrary> theGlobalLibraryCache, Map<ModelIdentifier, Model> theGlobalModelCache) {
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

		cqlCompilerOptions.setCompatibilityLevel("1.5");
		cqlOptions.setUseEmbeddedLibraries(true);

		cqlCompilerOptions.setAnalyzeDataRequirements(theCqlProperties.isCqlCompilerAnalyzeDataRequirements());
		cqlCompilerOptions.setCollapseDataRequirements(theCqlProperties.isCqlCompilerCollapseDataRequirements());

		cqlOptions.setCqlCompilerOptions(cqlCompilerOptions);
		evaluationSettings.setLibraryCache(theGlobalLibraryCache);
		evaluationSettings.setModelCache(theGlobalModelCache);
		return evaluationSettings;
	}


}
