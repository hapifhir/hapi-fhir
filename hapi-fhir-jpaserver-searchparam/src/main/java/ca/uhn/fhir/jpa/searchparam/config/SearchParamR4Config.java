package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryR4;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class SearchParamR4Config extends BaseSeachParamConfig {
	@Bean
	@Primary
	public FhirContext fhirContextR4() {
		FhirContext retVal = FhirContext.forR4();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryR4();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorR4 searchParamExtractor() {
		return new SearchParamExtractorR4();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainR4")
	public IValidationSupport validationSupportChainR4() {
		return new DefaultProfileValidationSupport();
	}
}
