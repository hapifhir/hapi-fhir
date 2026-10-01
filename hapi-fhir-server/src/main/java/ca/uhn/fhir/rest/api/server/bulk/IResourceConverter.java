package ca.uhn.fhir.rest.api.server.bulk;

import jakarta.annotation.Nonnull;

public interface IResourceConverter {
	/**
	 * Consumes the list of resources (already expanded; never a null or empty list; only one resource type)
	 * and the BulkExportJobParameters (defining things like export format, as well
	 * as search parameters).
	 * @param theResources - expanded resources to convert to writeable data.
	 *                      Resources are for reading and implementers shouldn't mutate it.
	 *                      The list will never be null or empty.
	 *                      All resources in the provided list will be of the same resource type
	 *                     (ie, never mixed types).
	 * @param theJobParameters - the bulk export parameters
	 * @return - the ConvertedFiles
	 */
	@Nonnull
	ConvertedFiles consume(
			@Nonnull BulkExportResourceList theResources, @Nonnull BulkExportJobParameters theJobParameters);
}
