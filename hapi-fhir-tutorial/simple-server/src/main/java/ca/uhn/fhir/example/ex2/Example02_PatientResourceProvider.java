package ca.uhn.fhir.example.ex2;

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
 * This is the most basic resource provider, showing only a single
 * read method on a resource provider
 */
public class Example02_PatientResourceProvider implements IResourceProvider {

	private Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	
	/** Constructor */
	public Example02_PatientResourceProvider() {
		Patient pat1 = new Patient();
		pat1.addIdentifier().setSystem("http://acme.com/MRNs").setValue("7000135");
		pat1.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		myPatients.put(1L, pat1);
	}
	
	/** All Resource Providers must implement this method */
	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}

	/** Simple implementation of the "read" method */
	@Read()
	public Patient read(@IdParam IdDt theId) {
		Patient retVal = myPatients.get(theId.getIdPartAsLong());
		if (retVal == null) {
			throw new ResourceNotFoundException(theId);
		}
		return retVal;
	}

}
