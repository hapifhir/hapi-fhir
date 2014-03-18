package ca.uhn.fhir.rest.client;

import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Include;
import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;

public interface ITestClient extends IBasicClient {

	@Read(type=Patient.class)
	Patient getPatientById(@Read.IdParam IdDt theId);

	@Read(type=Patient.class)
	Patient getPatientByVersionId(@Read.IdParam IdDt theId, @Read.VersionIdParam IdDt theVersionId);

	@Search(type=Patient.class)
	Patient findPatientByMrn(@Required(name = Patient.SP_IDENTIFIER) IdentifierDt theId);
	
	@Search(type=Patient.class)
	Bundle findPatientByName(@Required(name = Patient.SP_FAMILY) StringDt theId, @Optional(name=Patient.SP_GIVEN) StringDt theGiven);
	
	@Search()
	public List<Patient> getPatientMultipleIdentifiers(@Required(name = "ids") CodingListParam theIdentifiers);

	@Search()
	public List<Patient> getPatientByDob(@Required(name=Patient.SP_BIRTHDATE) QualifiedDateParam theBirthDate);

	@Search()
	public Patient getPatientWithIncludes(@Required(name = "withIncludes") StringDt theString, @Include List<PathSpecification> theIncludes);

}
