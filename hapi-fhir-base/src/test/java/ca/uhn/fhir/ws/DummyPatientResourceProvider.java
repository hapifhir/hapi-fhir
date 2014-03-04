package ca.uhn.fhir.ws;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.ws.operations.GET;
import ca.uhn.fhir.ws.parameters.Required;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DummyPatientResourceProvider implements IResourceProvider<Patient> {

    @GET
    @ResourceName(value="Patient")
    public Patient getPatient(@Required(name="mrn") String mrn) {
       return null;
    }

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Override
	public Patient getResourceById(long theId) {
		return null;
	}
}
