package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.servlet.http.HttpServletRequest;

public interface IResourceProvider<T extends IAnyResource> {

	IFhirResourceDao<T> getDao();

	void endRequest(HttpServletRequest theRequest);

	void startRequest(HttpServletRequest theRequest);
}
