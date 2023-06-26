package ca.uhn.fhir.rest.client;

import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.*;

public interface ITestClient extends IBasicClient {

	@Create
	MethodOutcome createPatient(@ResourceParam Patient thePatient);

	@Search()
	List<Patient> getPatientByDateRange(@RequiredParam(name = "dateRange") DateRangeParam theIdentifiers);

	@Search(type=Observation.class)
	Bundle getObservationByNameValueDate(@RequiredParam(name = Observation.SP_CODE_VALUE_DATE, compositeTypes = {StringParam.class, DateParam.class}) CompositeParam<StringParam, DateParam> theIdentifiers);

	@Search()
	List<Patient> getPatientByDob(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);

	@Search(type=ExtendedPatient.class)
	List<IBaseResource> getPatientByDobWithGenericResourceReturnType(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);

   @Search(type=ExtendedPatient.class)
	List<IAnyResource> getPatientByDobWithGenericResourceReturnType2(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);

   @Search()
	List<Patient> getPatientMultipleIdentifiers(@RequiredParam(name = "ids") TokenOrListParam theIdentifiers);

	@Search(queryName="someQueryNoParams")
	Patient getPatientNoParams();

	@Search(queryName="someQueryOneParam")
	Patient getPatientOneParam(@RequiredParam(name = "param1") StringParam theParam);

	@Search(type=Patient.class)
	Bundle findPatient(@RequiredParam(name = "param") StringAndListParam theStrings);

	@Search()
	Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") StringParam theString, @IncludeParam List<Include> theIncludes);
	
	@Update
	MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient);

	@Delete(type=DiagnosticReport.class)
	void deleteDiagnosticReport(@IdParam IdType theId);

	@Delete(type=Patient.class)
	MethodOutcome deletePatient(@IdParam IdType theId);

	@Search(type=Patient.class)
	Patient findPatientByMrn(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theId);

	@Search(type=Patient.class)
	Bundle findPatientByName(@RequiredParam(name = Patient.SP_FAMILY) StringParam theId, @OptionalParam(name=Patient.SP_GIVEN) StringParam theGiven);

	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdType theId);

	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdType theId, @Since InstantType theSince, @Count IntegerType theCount);
	
	@History(type=Patient.class)
	Bundle getHistoryPatientInstance(@IdParam IdType theId, @Since Date theSince, @Count Integer theCount);
	
	@History(type=Patient.class)
	Bundle getHistoryPatientType();

	@History
	Bundle getHistoryServer();

	@Read(type=Patient.class)
	Patient getPatientById(@IdParam IdType theId);

	@Validate(type=Patient.class)
	 MethodOutcome validatePatient(@ResourceParam Patient thePatient);

	@Search(type=Patient.class)
	Patient findPatientQuantity(@RequiredParam(name="quantityParam") QuantityParam theQuantityType);

	@Search(compartmentName="compartmentName")
	List<Patient> getPatientByCompartmentAndDob(@IdParam IdType theIdType, @RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);

	@Search
	Patient getPatientWithAt(@At InstantType theInstantType);
}
