package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.module.CdsHooksObjectMapperFactory;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsConfigServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig.CDS_HOOKS_OBJECT_MAPPER_FACTORY;

@Configuration
public class TestCrConfig {
	@Bean
	FhirContext fhirContext() {
		return FhirContext.forR4Cached();
	}

	@Bean(name = CDS_HOOKS_OBJECT_MAPPER_FACTORY)
	public ObjectMapper objectMapper(FhirContext theFhirContext) {
		return new CdsHooksObjectMapperFactory(theFhirContext).newMapper();
	}

	@Bean
	CdsCrSettings cdsCrSettings() { return CdsCrSettings.getDefault(); }

	@Bean
	public ICdsConfigService cdsConfigService(
		FhirContext theFhirContext,
		@Qualifier(CDS_HOOKS_OBJECT_MAPPER_FACTORY) ObjectMapper theObjectMapper,
		CdsCrSettings theCdsCrSettings) {
		return new CdsConfigServiceImpl(
			theFhirContext, theObjectMapper, theCdsCrSettings, null, null, null);
	}
}
