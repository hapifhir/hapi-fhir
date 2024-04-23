package ca.uhn.fhir.jpa.test.util;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Batch2JobAndSearchCacheManagerExtension implements AfterEachCallback, BeforeEachCallback {

	public interface ServiceSupplier {
		ISearchParamRegistry getSearchParamRegistry();

		IJobMaintenanceService getJobMaintenanceSvc();
	}

//	protected ISearchParamRegistry mySearchParamRegistry;
//
//	protected IJobMaintenanceService myIJobMaintenanceService;

	private ServiceSupplier mySupplier;

	public Batch2JobAndSearchCacheManagerExtension(ServiceSupplier theServiceSupplier) {
		mySupplier = theServiceSupplier;
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		// prevent any maintenance jobs from running
		mySupplier.getJobMaintenanceSvc().enableMaintenancePass(false);
		// prevent search parameter registry from refreshing
		mySupplier.getSearchParamRegistry().enableResourceChangeCache(false);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		mySupplier.getJobMaintenanceSvc().enableMaintenancePass(true);
		mySupplier.getSearchParamRegistry().enableResourceChangeCache(true);
	}
}
