package ca.uhn.fhir.rest.client;

import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;

public interface ITestClient extends IBasicClient {

	@Read(type=Patient.class)
	Patient getPatientById(@IdParam IdDt theId);

	@Delete(resourceType=Patient.class)
	MethodOutcome deletePatient(@IdParam IdDt theId);

	@Delete(resourceType=DiagnosticReport.class)
	void deleteDiagnosticReport(@IdParam IdDt theId);

	@Read(type=Patient.class)
	Patient getPatientByVersionId(@IdParam IdDt theId, @VersionIdParam IdDt theVersionId);

	@Search(type=Patient.class)
	Patient findPatientByMrn(@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theId);
	
	@Search(type=Patient.class)
	Bundle findPatientByName(@RequiredParam(name = Patient.SP_FAMILY) StringDt theId, @OptionalParam(name=Patient.SP_GIVEN) StringDt theGiven);
	
	@Search()
	public List<Patient> getPatientMultipleIdentifiers(@RequiredParam(name = "ids") CodingListParam theIdentifiers);

	@Search()
	public List<Patient> getPatientByDateRange(@RequiredParam(name = "dateRange") DateRangeParam theIdentifiers);

	@Search()
	public List<Patient> getPatientByDob(@RequiredParam(name=Patient.SP_BIRTHDATE) QualifiedDateParam theBirthDate);

	@Search()
	public Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") StringDt theString, @IncludeParam List<PathSpecification> theIncludes);

	@Search(queryName="someQueryNoParams")
	public Patient getPatientNoParams();

	@Search(queryName="someQueryOneParam")
	public Patient getPatientOneParam(@RequiredParam(name="param1") StringDt theParam);

	@Create
	public MethodOutcome createPatient(@ResourceParam Patient thePatient);

	@Update
	public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient);

	@Update
	public MethodOutcome updatePatient(@IdParam IdDt theId, @VersionIdParam IdDt theVersion, @ResourceParam Patient thePatient);

}
