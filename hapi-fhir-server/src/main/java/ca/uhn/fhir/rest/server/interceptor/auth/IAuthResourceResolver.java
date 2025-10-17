package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IAuthResourceResolver {
	IBaseResource resolveCompartmentById(IIdType theCompartmentId);

	List<IBaseResource> resolveCompartmentByIds(List<String> theCompartmentId, String theResourceType);
}
