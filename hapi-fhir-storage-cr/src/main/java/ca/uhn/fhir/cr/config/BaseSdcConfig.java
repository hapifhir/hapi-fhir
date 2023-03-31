package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.opencds.cqf.cql.evaluator.spring.fhir.adapter.AdapterConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(AdapterConfiguration.class)
@Configuration
public class BaseSdcConfig extends BaseRepositoryConfig {
	@Bean
	SdcProviderFactory sdcProviderFactory() {
		return new SdcProviderFactory();
	}

	@Bean
	SdcProviderLoader sdcProviderLoader(FhirContext theFhirContext, ResourceProviderFactory theResourceProviderFactory, SdcProviderFactory theSdcProviderFactory) {
		return new SdcProviderLoader(theFhirContext, theResourceProviderFactory, theSdcProviderFactory);
	}
}
