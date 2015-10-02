package ca.uhn.fhir.jaxrs.server.example;

import java.util.List;

import javax.ws.rs.core.Response;

import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

public interface IFhirPatientRestServer extends IResourceProvider {

    List<Patient> search(StringParam name);

    MethodOutcome update(IdDt theId, Patient patient)
                    throws Exception;

    Patient find(IdDt theId);

    Patient findHistory(IdDt theId);

    MethodOutcome create(Patient patient, String theConditional)
                    throws Exception;

    MethodOutcome delete(IdDt theId);

    Response operationLastGet(String resource)
                    throws Exception;

    Response operationLast(String resource)
                    throws Exception;

    Parameters last(StringDt dummyInput);

}
