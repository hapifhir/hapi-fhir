package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This interface is a parameter type for the {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRESHOW_RESOURCE}
 * hook.
 */
public interface IPreResourceShowDetails {

	/**
	 * @return Returns the number of resources being shown
	 */
	int size();

	/**
	 * @return Returns the resource at the given index. If you wish to make modifications
	 * to any resources
	 */
	IBaseResource getResource(int theIndex);

	/**
	 * Replace the resource being returned at index
	 *
	 * @param theIndex    The resource index
	 * @param theResource The resource at index
	 */
	void setResource(int theIndex, IBaseResource theResource);

	/**
	 * Indicates that data is being masked from within the resource at the given index.
	 * This generally flags to the rest of the stack that the resource should include
	 * a SUBSET tag as an indication to consumers that some data has been removed.
	 *
	 * @param theIndex The resource index
	 */
	void markResourceAtIndexAsSubset(int theIndex);

}
