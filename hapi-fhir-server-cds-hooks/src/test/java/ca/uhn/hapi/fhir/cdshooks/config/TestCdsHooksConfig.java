package ca.uhn.hapi.fhir.cdshooks.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.controller.TestServerAppCtx;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrSettings;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCdsHooksConfig {
	@Bean
	FhirContext fhirContext() {
		return FhirContext.forR4Cached();
	}

	@Bean
	CdsCrSettings cdsCrSettings() { return CdsCrSettings.getDefault(); }

	@Bean
	public CdsHooksContextBooter cdsHooksContextBooter() {
		CdsHooksContextBooter retVal = new CdsHooksContextBooter();
		retVal.setDefinitionsClass(TestServerAppCtx.class);
		retVal.start();
		return retVal;
	}

	@Bean
	DaoRegistry daoRegistry() {
		return new DaoRegistry();
	}

	@Bean
	ICdsHooksDaoAuthorizationSvc cdsHooksDaoAuthorizationSvc() {
		return new TestCdsHooksDaoAuthorizationSvc();
	}

	private static class TestCdsHooksDaoAuthorizationSvc implements ICdsHooksDaoAuthorizationSvc {
		@Override
		public void authorizePreShow(IBaseResource theResource) {
			// nothing
		}
	}
}
