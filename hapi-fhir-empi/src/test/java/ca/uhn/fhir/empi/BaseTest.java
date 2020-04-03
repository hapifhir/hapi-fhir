package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.json.*;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;

public abstract class BaseTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final double NO_MATCH_THRESHOLD = 0.60;
	public static final double MATCH_THRESHOLD = 0.80;
	public static final double EXPECTED_FIRST_NAME_WEIGHT = 0.63;
	public static final double EXPECTED_BOTH_NAMES_WEIGHT = 0.83;

	public static final double NAME_THRESHOLD = 0.8;
	public static final double NAME_DELTA = 0.0001;
	public static final String PATIENT_GIVEN = "patient-given";
	public static final String PATIENT_LAST = "patient-last";


	protected Patient myPatient1;
	protected Patient myPatient2;
	protected EmpiFieldMatchJson myGivenNameMatchField;
	private EmpiFieldMatchJson myLastNameMatchField;
	protected EmpiRulesJson myRules;
	protected String myBothNameFields;
	private EmpiResourceSearchParamJson myPatientBirthdayBlocking;
	private EmpiResourceSearchParamJson myPatientIdentifierBlocking;
	private EmpiFilterSearchParamJson myActivePatientsBlockingFilter;

	@Before
	public void before() {

		myActivePatientsBlockingFilter = new EmpiFilterSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_ACTIVE)
			.setFixedValue("true");

		myPatientBirthdayBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_BIRTHDATE);
		myPatientIdentifierBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_IDENTIFIER);

		myGivenNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMetric(DistanceMetricEnum.COSINE)
			.setMatchThreshold(NAME_THRESHOLD);

		myLastNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_LAST)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMetric(DistanceMetricEnum.JARO_WINKLER)
			.setMatchThreshold(NAME_THRESHOLD);

		myRules = new EmpiRulesJson();
		myRules.addResourceSearchParam(myPatientBirthdayBlocking);
		myRules.addResourceSearchParam(myPatientIdentifierBlocking);
		myRules.addFilterSearchParam(myActivePatientsBlockingFilter);
		myRules.addMatchField(myGivenNameMatchField);
		myRules.addMatchField(myLastNameMatchField);
		myRules.setMatchThreshold(MATCH_THRESHOLD);
		myRules.setNoMatchThreshold(NO_MATCH_THRESHOLD);

		myBothNameFields = String.join(",", PATIENT_GIVEN, PATIENT_LAST);
		myRules.putWeight(myBothNameFields, EXPECTED_BOTH_NAMES_WEIGHT);
		myRules.putWeight(PATIENT_GIVEN, EXPECTED_FIRST_NAME_WEIGHT);
		initializePatients();
	}

	private void initializePatients() {
		myPatient1 = new Patient();
		myPatient1.addName().addGiven("John");
		myPatient1.setId("Patient/1");
		myPatient2 = new Patient();
		myPatient2.addName().addGiven("Johny");
		myPatient2.setId("Patient/2");
	}
}
