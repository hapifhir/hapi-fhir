package ca.uhn.fhir.ws;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.ws.operations.GET;
import ca.uhn.fhir.ws.parameters.Required;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class PatientResource {

    @GET
    @ResourceName(value="Patient")
    public static Patient getPatient(@Required(name="mrn") String mrn) {
       return null;
    }
}
