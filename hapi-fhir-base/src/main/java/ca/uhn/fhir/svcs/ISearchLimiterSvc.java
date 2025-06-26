package ca.uhn.fhir.svcs;

import jakarta.annotation.Nonnull;

import java.util.Set;

public interface ISearchLimiterSvc {

	/**
	 * Add a resource type to omit from all search results for the named operation.
	 * @param theOperationName operation name (eg: $everything, $export, etc)
	 * @param theResourceType the resource name to omit (eg: Group, List, etc)
	 */
	void addOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType);

	/**
	 * Get all omitted resources for the named operation.
	 */
	Set<String> getResourcesToOmitForOperationSearches(@Nonnull String theOperationName);

	/**
	 * Remove the resource type from the omission criteria.
	 * @param theOperationName the operation name
	 * @param theResourceType the resource type; if null, will remove all resources from omission list
	 */
	void removeOmittedResourceType(@Nonnull String theOperationName, String theResourceType);
}
