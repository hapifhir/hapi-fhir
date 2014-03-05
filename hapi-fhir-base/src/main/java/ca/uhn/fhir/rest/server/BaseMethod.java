package ca.uhn.fhir.rest.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

abstract class BaseMethod {

	public enum ReturnTypeEnum {
		RESOURCE, BUNDLE
	}

	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public abstract ReturnTypeEnum getReturnType();

	public abstract boolean matches(String theResourceName, IdDt theId, IdDt theVersion, Set<String> theParameterNames);

	public void setResource(Resource theResource) {
		this.resource = theResource;
	}

	public abstract List<IResource> invoke(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> theParameterValues) throws InvalidRequestException,
			InternalErrorException;

	protected static List<IResource> toResourceList(Object response) throws InternalErrorException {
		if (response == null) {
			return Collections.emptyList();
		} else if (response instanceof IResource) {
			return Collections.singletonList((IResource) response);
		} else if (response instanceof Collection) {
			List<IResource> retVal = new ArrayList<IResource>();
			for (Object next : ((Collection<?>) response)) {
				retVal.add((IResource) next);
			}
			return retVal;
		} else {
			throw new InternalErrorException("Unexpected return type: " + response.getClass().getCanonicalName());
		}
	}
}
