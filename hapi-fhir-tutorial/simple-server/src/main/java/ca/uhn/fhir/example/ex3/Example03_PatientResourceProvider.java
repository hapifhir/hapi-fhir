package ca.uhn.fhir.example.ex3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * This is the most basic resource provider, showing only a single
 * read method on a resource provider
 */
public class Example03_PatientResourceProvider implements IResourceProvider {

	private Map<Long, Patient> myPatients = new HashMap<Long, Patient>();
	private Long myNextId = 1L;
	
	/** Constructor */
	public Example03_PatientResourceProvider() {
		Long id = myNextId++;
		
		Patient pat1 = new Patient();
		pat1.setId(new IdDt(id));
		pat1.addIdentifier().setSystem("http://acme.com/MRNs").setValue("7000135");
		pat1.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		myPatients.put(id, pat1);
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

	/** Create/save a new resource */
	@Create
	public MethodOutcome create(@ResourceParam Patient thePatient) {
		// Give the resource the next sequential ID
		long id = myNextId++;
		thePatient.setId(new IdDt(id));
		
		// Store the resource in memory
		myPatients.put(id, thePatient);
		
		// Inform the server of the ID for the newly stored resource
		return new MethodOutcome(thePatient.getId());	
	}
	
	/** Simple "search" implementation **/
	@Search
	public List<Patient> search() {
		List<Patient> retVal = new ArrayList<Patient>();
		retVal.addAll(myPatients.values());
		return retVal;
	}
	
	/** A search with a parameter */
	@Search
	public List<Patient> search(@RequiredParam(name="family") StringParam theParam) {
		List<Patient> retVal = new ArrayList<Patient>();

		// Loop through the patients looking for matches
		for (Patient next : myPatients.values()) {
			String familyName = next.getNameFirstRep().getFamilyAsSingleString().toLowerCase();
			if (familyName.contains(theParam.getValue().toLowerCase()) == false) {
				continue;
			}
			retVal.add(next);
		}
		
		return retVal;		
	}

}
