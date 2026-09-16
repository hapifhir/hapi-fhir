package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;

public interface IResourceConverter {
	/**
	 * Consumes the list of resources (already expanded)
	 * and the BulkExportJobParameters (defining things like export format, as well
	 * as search parameters)
	 * @param theResources - expanded resources to convert to writeable data
	 * @param theJobParameters - the bulk export parameters
	 * @return - the ConvertedFiles
	 */
	ConvertedFiles consume(BulkExportResourceList theResources, BulkExportJobParameters theJobParameters);
}
