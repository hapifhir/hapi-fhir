package ca.uhn.fhir.example.ex1;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * Note, this is an incomplete example of a resource provider. It shows a
 * read method which reads from a HashMap, but does not have any way
 * of putting things in that HashMap.
 */
public class Example02_PatientResourceProvider implements IResourceProvider {

	private Map<IdDt, Patient> myPatients = new HashMap<IdDt, Patient>();
	
	/** Constructor */
	public Example02_PatientResourceProvider() {
//		Patient
	}
	
	/** All Resource Providers must implement this method */
	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}

	/** Simple implementation of the "read" method */
	@Read()
	public Patient read(@IdParam IdDt theId) {
		Patient retVal = myPatients.get(theId);
		if (retVal == null) {
			throw new ResourceNotFoundException(theId);
		}
		return retVal;
	}
	
}
