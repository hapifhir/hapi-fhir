package ca.uhn.fhir.jpa.searchparam.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu2;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu2;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import org.hl7.fhir.instance.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class SearchParamDstu2Config {
	@Bean
	@Primary
	public FhirContext fhirContextDstu2() {
		FhirContext retVal = FhirContext.forDstu2();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu2();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorDstu2 searchParamExtractor() {
		return new SearchParamExtractorDstu2();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainDstu2")
	public IValidationSupport validationSupportChainDstu2() {
		return new DefaultProfileValidationSupport();
	}
}
