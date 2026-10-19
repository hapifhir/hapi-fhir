package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.controller.TestServerAppCtx;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchDaoSvc;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * CDS Hooks can run without JPA storage, prefetching through a FHIR client only, so there is no DaoRegistry bean.
 */
// Created by Claude Opus 5.5
class CdsHooksConfigWithoutDaoRegistryTest {

	@Test
	void contextStarts_withoutDaoRegistry() {
		try (AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(NoStorageConfig.class, CdsHooksConfig.class)) {
			assertThat(context.getBean(CdsPrefetchDaoSvc.class)).isNotNull();
		}
	}

	@Test
	void daoPrefetch_withoutDaoRegistry_failsWithConfigurationException() {
		try (AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(NoStorageConfig.class, CdsHooksConfig.class)) {
			CdsPrefetchDaoSvc svc = context.getBean(CdsPrefetchDaoSvc.class);

			assertThatThrownBy(() -> svc.resourceFromUrl("Patient/123"))
					.isInstanceOf(ConfigurationException.class)
					.hasMessageContaining("HAPI-3054: ")
					.hasMessageContaining("DaoRegistry");
		}
	}

	@Configuration
	static class NoStorageConfig {
		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR4Cached();
		}

		@Bean
		CdsHooksContextBooter cdsHooksContextBooter() {
			CdsHooksContextBooter retVal = new CdsHooksContextBooter();
			retVal.setDefinitionsClass(TestServerAppCtx.class);
			retVal.start();
			return retVal;
		}

		@Bean
		ICdsHooksDaoAuthorizationSvc cdsHooksDaoAuthorizationSvc() {
			return theResource -> {};
		}
	}
}
