package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.cr.config.BaseRepositoryConfig;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsHooksContextBooter;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionR4;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionR5;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionStu3;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BaseRepositoryConfig.class, CdsHooksConfig.class})
public class TestCrConfig {
	@Autowired
	DaoRegistry myDaoRegistry;

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

	@Bean
	public CdsHooksContextBooter cdsHooksContextBooter() {
		CdsHooksContextBooter retVal = new CdsHooksContextBooter();
		retVal.start();
		return retVal;
	}

	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings storageSettings = new JpaStorageSettings();
		storageSettings.setAllowExternalReferences(true);
		storageSettings.setEnforceReferentialIntegrityOnWrite(false);
		storageSettings.setEnforceReferenceTargetTypes(false);
		storageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		//storageSettings.setResourceServerIdStrategy(Id);
		return storageSettings;
	}
}
