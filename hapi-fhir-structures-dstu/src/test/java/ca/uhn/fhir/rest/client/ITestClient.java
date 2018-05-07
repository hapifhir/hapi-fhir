package ca.uhn.fhir.rest.client;

import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.*;

public interface ITestClient extends IBasicClient {

	@Create
	public MethodOutcome createPatient(@ResourceParam Patient thePatient);

	@Search()
	public List<Patient> getPatientByDateRange(@RequiredParam(name = "dateRange") DateRangeParam theIdentifiers);

	@Search(type=Observation.class)
	public Bundle getObservationByNameValueDate(@RequiredParam(name = Observation.SP_NAME_VALUE_DATE, compositeTypes= {StringParam.class,DateParam.class}) CompositeParam<StringParam, DateParam> theIdentifiers);

	@Search()
	public List<Patient> getPatientByDob(@RequiredParam(name=Patient.SP_BIRTHDATE) DateParam theBirthDate);

	@Search(type=ExtendedPatient.class)
	public List<IResource> getPatientByDobWithGenericResourceReturnType(@RequiredParam(name=Patient.SP_BIRTHDATE) DateParam theBirthDate);

	@Search()
	public List<Patient> getPatientMultipleIdentifiers(@RequiredParam(name = "ids") TokenOrListParam theIdentifiers);

	@Search(queryName="someQueryNoParams")
	public Patient getPatientNoParams();

	@Search(queryName="someQueryOneParam")
	public Patient getPatientOneParam(@RequiredParam(name="param1") StringParam theParam);

	@Search(type=Patient.class)
	public Bundle findPatient(@RequiredParam(name = "param") StringAndListParam theStrings);

	@Search()
	public Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") StringParam theString, @IncludeParam List<Include> theIncludes);
	
	@Update
	public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient);

	@Delete(type=DiagnosticReport.class)
	void deleteDiagnosticReport(@IdParam IdDt theId);

	@Delete(type=Patient.class)
	MethodOutcome deletePatient(@IdParam IdDt theId);

	@Search(type=Patient.class)
	Patient findPatientByMrn(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theId);

	@Search(type=Patient.class)
	Bundle findPatientByName(@RequiredParam(name = Patient.SP_FAMILY) StringParam theId, @OptionalParam(name=Patient.SP_GIVEN) StringParam theGiven);

	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdDt theId);

	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdDt theId, @Since InstantDt theSince, @Count IntegerDt theCount);
	
	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdDt theId, @Since Date theSince, @Count Integer theCount);
	
	@History(type=Patient.class)
	Bundle getHistoryPatientType();

	@History
	Bundle getHistoryServer();

	@Read(type=Patient.class)
	Patient getPatientById(@IdParam IdDt theId);

	@Validate(type=Patient.class)
	 MethodOutcome validatePatient(@ResourceParam Patient thePatient);

	@Search(type=Patient.class)
	Patient findPatientQuantity(@RequiredParam(name="quantityParam") QuantityParam theQuantityDt);

	@Search(compartmentName="compartmentName")
	public List<Patient> getPatientByCompartmentAndDob(@IdParam IdDt theIdDt, @RequiredParam(name=Patient.SP_BIRTHDATE) DateParam theBirthDate);


}
