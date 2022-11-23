package ca.uhn.fhir.cr;

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Library;

public interface IResourceLoader extends IDaoRegistryUser {
	default <T extends IBaseBundle> T loadBundle(Class<T> theType, String theLocation) {
		var bundle = readResource(theType, theLocation);
		getDaoRegistry().getSystemDao().transaction(new SystemRequestDetails(), bundle);

		return bundle;
	}

	default <T extends IBaseResource> T readResource(Class<T> theType, String theLocation) {
		return ClasspathUtil.loadResource(getFhirContext(), theType, theLocation);
	}

	default <T extends IBaseResource> T loadResource(Class<T> theType, String theLocation, RequestDetails theRequestDetails) {
		var resource = readResource(theType, theLocation);
		getDaoRegistry().getResourceDao(theType).update(resource, theRequestDetails);
		
		return resource;
	}
}
