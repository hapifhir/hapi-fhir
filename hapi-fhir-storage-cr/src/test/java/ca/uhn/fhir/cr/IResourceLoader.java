package ca.uhn.fhir.cr;

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This is a utility interface that allows a class that has a DaoRegistry to load Bundles and read Resources.
 * This is used primarily to set up integration tests for clinical reasoning operations since they often
 * require big bundles of content, such as Libraries, ValueSets, Measures, and so on.
 */
public interface IResourceLoader extends IDaoRegistryUser {
	/**
	 * Method to load bundles
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type bundle
	 * @param <T>
	 */
	default <T extends IBaseBundle> T loadBundle(Class<T> theType, String theLocation) {
		var bundle = readResource(theType, theLocation);
		getDaoRegistry().getSystemDao().transaction(new SystemRequestDetails(), bundle);

		return bundle;
	}

	/**
	 * Method to read resource
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type resource
	 * @param <T>
	 */
	default <T extends IBaseResource> T readResource(Class<T> theType, String theLocation) {
		return ClasspathUtil.loadResource(getFhirContext(), theType, theLocation);
	}

	/**
	 * Method to load resource
	 * @param theType, resource type
	 * @param theLocation, location of the resource
	 * @return of type resource
	 * @param <T>
	 */
	default <T extends IBaseResource> T loadResource(Class<T> theType, String theLocation, RequestDetails theRequestDetails) {
		var resource = readResource(theType, theLocation);
		getDaoRegistry().getResourceDao(theType).update(resource, theRequestDetails);

		return resource;
	}
}
