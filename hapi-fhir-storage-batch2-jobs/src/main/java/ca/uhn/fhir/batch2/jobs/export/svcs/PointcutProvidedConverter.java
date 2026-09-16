package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;

public class PointcutProvidedConverter implements IResourceConverter {

	private final IInterceptorService myInterceptorService;

	public PointcutProvidedConverter(IInterceptorService theIInterceptorService) {
		myInterceptorService = theIInterceptorService;
	}

	@Override
	public ConvertedFiles consume(BulkExportResourceList theResources, BulkExportJobParameters theJobParameters) {
		HookParams params = new HookParams();
		params.add(BulkExportResourceList.class, theResources);
		params.add(BulkExportJobParameters.class, theJobParameters);

		ConvertedFiles files = (ConvertedFiles) myInterceptorService.callHooksAndReturnObject(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_CONVERT, params);
		return files;
	}
}
