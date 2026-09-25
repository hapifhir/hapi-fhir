package ca.uhn.fhir.rest.api.server.bulk;

import jakarta.annotation.Nonnull;

public interface IResourceConverter {
	/**
	 * Consumes the list of resources (already expanded)
	 * and the BulkExportJobParameters (defining things like export format, as well
	 * as search parameters)
	 * @param theResources - expanded resources to convert to writeable data
	 * @param theJobParameters - the bulk export parameters
	 * @return - the ConvertedFiles
	 */
	@Nonnull
	ConvertedFiles consume(
			@Nonnull BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters);
}
