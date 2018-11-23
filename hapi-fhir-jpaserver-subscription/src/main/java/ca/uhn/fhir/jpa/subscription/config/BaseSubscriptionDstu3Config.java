package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryDstu3;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

// From BaseDstu3Config
public class BaseSubscriptionDstu3Config extends BaseSubscriptionConfig {
	@Override
	public FhirContext fhirContext() {
		return fhirContextDstu3();
	}

	@Bean
	@Primary
	public FhirContext fhirContextDstu3() {
		FhirContext retVal = FhirContext.forDstu3();

		// Don't strip versions in some places
		ParserOptions parserOptions = retVal.getParserOptions();
		parserOptions.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");

		return retVal;
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryDstu3(searchParamProvider());
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public SearchParamExtractorDstu3 searchParamExtractor() {
		return new SearchParamExtractorDstu3();
	}

	@Primary
	@Bean(autowire = Autowire.BY_NAME, name = "myJpaValidationSupportChainDstu3")
	public IValidationSupport validationSupportChainDstu3() {
		return new DefaultProfileValidationSupport();
	}
}
