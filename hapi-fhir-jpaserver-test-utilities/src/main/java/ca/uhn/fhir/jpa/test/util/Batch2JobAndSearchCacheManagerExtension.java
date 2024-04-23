package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class Batch2JobAndSearchCacheManagerExtension implements AfterEachCallback, BeforeEachCallback {

	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	protected IJobMaintenanceService myIJobMaintenanceService;

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		// prevent any maintenance jobs from running
		myIJobMaintenanceService.enableMaintenancePass(false);
		// prevent search parameter registry from refreshing
		mySearchParamRegistry.enableResourceChangeCache(false);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myIJobMaintenanceService.enableMaintenancePass(true);
		mySearchParamRegistry.enableResourceChangeCache(true);
	}
}
