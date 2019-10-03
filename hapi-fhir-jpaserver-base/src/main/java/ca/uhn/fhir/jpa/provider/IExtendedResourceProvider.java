package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.servlet.http.HttpServletRequest;

public interface IExtendedResourceProvider<T extends IAnyResource> extends IResourceProvider {

	IFhirResourceDao<T> getDao();

	void endRequest(HttpServletRequest theRequest);

	void startRequest(HttpServletRequest theRequest);
}
