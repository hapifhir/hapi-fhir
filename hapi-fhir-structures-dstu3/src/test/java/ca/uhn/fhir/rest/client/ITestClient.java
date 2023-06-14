package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.DateParam;
import java.util.List;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ITestClient extends IBasicClient {

    @Search(type = ExtendedPatient.class)
    public List<IBaseResource> getPatientByDobWithGenericResourceReturnType(
            @RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);

    @Search(type = ExtendedPatient.class)
    public List<IAnyResource> getPatientByDobWithGenericResourceReturnType2(
            @RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);
}
