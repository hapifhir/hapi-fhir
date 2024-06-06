package ca.uhn.fhir.cr;

import org.cqframework.cql.cql2elm.CqlCompilerException;
import org.cqframework.cql.cql2elm.CqlCompilerOptions;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryBuilder;
import org.opencds.cqf.fhir.cql.CqlEngineOptions;
import org.opencds.cqf.fhir.cql.CqlOptions;


public class TestCqlProperties {

	//cql settings
	private CqlEngineOptions cqlEngineOptions = CqlEngineOptions.defaultOptions();
	private Boolean cql_use_embedded_libraries = true;
	private Boolean cql_runtime_debug_logging_enabled = false;
	private Boolean cql_runtime_enable_validation = false;
	private Boolean cql_runtime_enable_expression_caching = true;
	private Boolean cql_compiler_validate_units = true;
	private Boolean cql_compiler_verify_only = false;
	private String cql_compiler_compatibility_level = "1.3";
	private CqlCompilerException.ErrorSeverity cql_compiler_error_level = CqlCompilerException.ErrorSeverity.Info;
	private LibraryBuilder.SignatureLevel cql_compiler_signature_level = LibraryBuilder.SignatureLevel.All;
	private Boolean cql_compiler_analyze_data_requirements = false;
	private Boolean cql_compiler_collapse_data_requirements = false;
	private CqlTranslator.Format cql_compiler_translator_format = CqlTranslator.Format.JSON;
	private Boolean cql_compiler_enable_date_range_optimization = false;
	private Boolean cql_compiler_enable_annotations = false;
	private Boolean cql_compiler_enable_locators = false;
	private Boolean cql_compiler_enable_results_type = false;
	private Boolean cql_compiler_enable_detailed_errors = false;
	private Boolean cql_compiler_disable_list_traversal = false;
	private Boolean cql_compiler_disable_list_demotion = false;
	private Boolean cql_compiler_disable_list_promotion = false;
	private Boolean cql_compiler_enable_interval_demotion = false;
	private Boolean cql_compiler_enable_interval_promotion = false;
	private Boolean cql_compiler_disable_method_invocation = false;
	private Boolean cql_compiler_require_from_keyword = false;
	private Boolean cql_compiler_disable_default_model_info_load = false;

	// Care-gaps Settings
	private String caregaps_reporter = "default";
	private String caregaps_section_author = "default";

	public boolean isCqlUseEmbeddedLibraries() {
		return cql_use_embedded_libraries;
	}

	public void setCqlUseEmbeddedLibraries(boolean cql_use_embedded_libraries) {
		this.cql_use_embedded_libraries = cql_use_embedded_libraries;
	}

	public boolean isCqlRuntimeDebugLoggingEnabled() {
		return cql_runtime_debug_logging_enabled;
	}

	public void setCqlRuntimeDebugLoggingEnabled(boolean cqlRuntimeDebugLoggingEnabled) {
		this.cql_runtime_debug_logging_enabled = cqlRuntimeDebugLoggingEnabled;
	}

	public boolean isCqlCompilerValidateUnits() {
		return cql_compiler_validate_units;
	}

	public void setCqlCompilerValidateUnits(boolean cqlCompilerValidateUnits) {
		this.cql_compiler_validate_units = cqlCompilerValidateUnits;
	}

	public boolean isCqlCompilerVerifyOnly() {
		return cql_compiler_verify_only;
	}

	public void setCqlCompilerVerifyOnly(boolean cqlCompilerVerifyOnly) {
		this.cql_compiler_verify_only = cqlCompilerVerifyOnly;
	}

	public String getCqlCompilerCompatibilityLevel() {
		return cql_compiler_compatibility_level;
	}

	public void setCqlCompilerCompatibilityLevel(String cqlCompilerCompatibilityLevel) {
		this.cql_compiler_compatibility_level = cqlCompilerCompatibilityLevel;
	}

	public CqlCompilerException.ErrorSeverity getCqlCompilerErrorSeverityLevel() {
		return cql_compiler_error_level;
	}

	public void setCqlCompilerErrorSeverityLevel(CqlCompilerException.ErrorSeverity cqlCompilerErrorSeverityLevel) {
		this.cql_compiler_error_level = cqlCompilerErrorSeverityLevel;
	}

	public LibraryBuilder.SignatureLevel getCqlCompilerSignatureLevel() {
		return cql_compiler_signature_level;
	}

	public void setCqlCompilerSignatureLevel(LibraryBuilder.SignatureLevel cqlCompilerSignatureLevel) {
		this.cql_compiler_signature_level = cqlCompilerSignatureLevel;
	}

	public boolean isCqlCompilerAnalyzeDataRequirements() {
		return cql_compiler_analyze_data_requirements;
	}

	public void setCqlCompilerAnalyzeDataRequirements(boolean cqlCompilerAnalyzeDataRequirements) {
		this.cql_compiler_analyze_data_requirements = cqlCompilerAnalyzeDataRequirements;
	}

	public boolean isCqlCompilerCollapseDataRequirements() {
		return cql_compiler_collapse_data_requirements;
	}

	public void setCqlCompilerCollapseDataRequirements(boolean cqlCompilerCollapseDataRequirements) {
		this.cql_compiler_collapse_data_requirements = cqlCompilerCollapseDataRequirements;
	}

	public boolean isEnableDateRangeOptimization() {
		return cql_compiler_enable_date_range_optimization;
	}

	public void setEnableDateRangeOptimization(boolean enableDateRangeOptimization) {
		this.cql_compiler_enable_date_range_optimization = enableDateRangeOptimization;
	}

	public boolean isEnableAnnotations() {
		return cql_compiler_enable_annotations;
	}

	public void setEnableAnnotations(boolean enableAnnotations) {
		this.cql_compiler_enable_annotations = enableAnnotations;
	}

	public boolean isEnableLocators() {
		return cql_compiler_enable_locators;
	}

	public void setEnableLocators(boolean enableLocators) {
		this.cql_compiler_enable_locators = enableLocators;
	}

	public boolean isEnableResultsType() {
		return cql_compiler_enable_results_type;
	}

	public void setEnableResultsType(boolean enableResultsType) {
		this.cql_compiler_enable_results_type = enableResultsType;
	}

	public boolean isEnableDetailedErrors() {
		return cql_compiler_enable_detailed_errors;
	}

	public void setEnableDetailedErrors(boolean enableDetailedErrors) {
		this.cql_compiler_enable_detailed_errors = enableDetailedErrors;
	}

	public boolean isDisableListTraversal() {
		return cql_compiler_disable_list_traversal;
	}

	public void setDisableListTraversal(boolean disableListTraversal) {
		this.cql_compiler_disable_list_traversal = disableListTraversal;
	}

	public boolean isDisableListDemotion() {
		return cql_compiler_disable_list_demotion;
	}

	public void setDisableListDemotion(boolean disableListDemotion) {
		this.cql_compiler_disable_list_demotion = disableListDemotion;
	}

	public boolean isDisableListPromotion() {
		return cql_compiler_disable_list_promotion;
	}

	public void setDisableListPromotion(boolean disableListPromotion) {
		this.cql_compiler_disable_list_promotion = disableListPromotion;
	}

	public boolean isEnableIntervalPromotion() {
		return cql_compiler_enable_interval_promotion;
	}

	public void setEnableIntervalPromotion(boolean enableIntervalPromotion) {
		this.cql_compiler_enable_interval_promotion = enableIntervalPromotion;
	}

	public boolean isEnableIntervalDemotion() {
		return cql_compiler_enable_interval_demotion;
	}

	public void setEnableIntervalDemotion(boolean enableIntervalDemotion) {
		this.cql_compiler_enable_interval_demotion = enableIntervalDemotion;
	}

	public boolean isDisableMethodInvocation() {
		return cql_compiler_disable_method_invocation;
	}

	public void setDisableMethodInvocation(boolean disableMethodInvocation) {
		this.cql_compiler_disable_method_invocation = disableMethodInvocation;
	}

	public boolean isRequireFromKeyword() {
		return cql_compiler_require_from_keyword;
	}

	public void setRequireFromKeyword(boolean requireFromKeyword) {
		this.cql_compiler_require_from_keyword = requireFromKeyword;
	}

	public boolean isDisableDefaultModelInfoLoad() {
		return cql_compiler_disable_default_model_info_load;
	}

	public void setDisableDefaultModelInfoLoad(boolean disableDefaultModelInfoLoad) {
		this.cql_compiler_disable_default_model_info_load = disableDefaultModelInfoLoad;
	}

	public boolean isCqlRuntimeEnableExpressionCaching() {
		return cql_runtime_enable_expression_caching;
	}

	public void setCqlRuntimeEnableExpressionCaching(boolean cqlRuntimeEnableExpressionCaching) {
		this.cql_runtime_enable_expression_caching = cqlRuntimeEnableExpressionCaching;
	}

	public boolean isCqlRuntimeEnableValidation() {
		return cql_runtime_enable_validation;
	}

	public void setCqlRuntimeEnableValidation(boolean cqlRuntimeEnableValidation) {
		this.cql_runtime_enable_validation = cqlRuntimeEnableValidation;
	}

	public CqlTranslator.Format getCqlTranslatorFormat() {
		return cql_compiler_translator_format;
	}

	public void setCqlTranslatorFormat(CqlTranslator.Format cqlTranslatorFormat) {
		this.cql_compiler_translator_format = cqlTranslatorFormat;
	}

	private CqlCompilerOptions cqlCompilerOptions = new CqlCompilerOptions();

	public CqlCompilerOptions getCqlCompilerOptions() {
		return this.cqlCompilerOptions;
	}

	public void setCqlCompilerOptions(CqlCompilerOptions compilerOptions) {
		this.cqlCompilerOptions = compilerOptions;
	}

	public CqlEngineOptions getCqlEngineOptions() {
		return this.cqlEngineOptions;
	}

	public void setCqlEngineOptions(CqlEngineOptions engine) {
		this.cqlEngineOptions = engine;
	}

	public CqlOptions getCqlOptions() {
		CqlOptions cqlOptions = new CqlOptions();
		cqlOptions.setUseEmbeddedLibraries(this.cql_use_embedded_libraries);
		cqlOptions.setCqlEngineOptions(this.getCqlEngineOptions());
		cqlOptions.setCqlCompilerOptions(this.getCqlCompilerOptions());
		//cqlOptions.setCqlCompilerOptions(this.getCqlCompilerOptions());
		return cqlOptions;
	}

	public String getCareGapsReporter() {
		return caregaps_reporter;
	}
	public String getCareGapsSectionAuthor() {
		return caregaps_section_author;
	}

	public void setCareGapsSectionAuthor(String theCareGapsSectionAuthor) {this.caregaps_section_author = theCareGapsSectionAuthor;}
	public void setCareGapsReporter(String theCareGapsReporter) {
		this.caregaps_reporter = theCareGapsReporter;
	}

}
