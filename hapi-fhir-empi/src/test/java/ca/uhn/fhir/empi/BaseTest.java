package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.json.*;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;

public abstract class BaseTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final String PATIENT_GIVEN = "patient-given";
	public static final String PATIENT_LAST = "patient-last";
	public static final double NAME_THRESHOLD = 0.8;
	public static final double NO_MATCH_THRESHOLD = 0.60;
	public static final double MATCH_THRESHOLD = 0.80;
	public static final double EXPECTED_FIRST_NAME_WEIGHT = 0.63;
	public static final double EXPECTED_BOTH_NAMES_WEIGHT = 0.83;
	protected EmpiFieldMatchJson myGivenNameMatchField;
	protected String myBothNameFields;

	@Before
	protected void before() {
		myGivenNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMetric(DistanceMetricEnum.COSINE)
			.setMatchThreshold(NAME_THRESHOLD);
	}

	protected Patient buildJohn() {
		Patient patient = new Patient();
		patient.addName().addGiven("John");
		patient.setId("Patient/1");
		return patient;
	}

	protected Patient buildJohny() {
		Patient patient = new Patient();
		patient.addName().addGiven("Johny");
		patient.setId("Patient/2");
		return patient;
	}

	protected EmpiRulesJson buildActiveBirthdateIdRules() {
		EmpiFilterSearchParamJson activePatientsBlockingFilter = new EmpiFilterSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_ACTIVE)
			.setFixedValue("true");

		EmpiResourceSearchParamJson patientBirthdayBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_BIRTHDATE);
		EmpiResourceSearchParamJson patientIdentifierBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_IDENTIFIER);


		EmpiFieldMatchJson lastNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_LAST)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMetric(DistanceMetricEnum.JARO_WINKLER)
			.setMatchThreshold(NAME_THRESHOLD);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.addResourceSearchParam(patientBirthdayBlocking);
		retval.addResourceSearchParam(patientIdentifierBlocking);
		retval.addFilterSearchParam(activePatientsBlockingFilter);
		retval.addMatchField(myGivenNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.setMatchThreshold(MATCH_THRESHOLD);
		retval.setNoMatchThreshold(NO_MATCH_THRESHOLD);

		myBothNameFields = String.join(",", PATIENT_GIVEN, PATIENT_LAST);
		retval.putWeight(myBothNameFields, EXPECTED_BOTH_NAMES_WEIGHT);
		retval.putWeight(PATIENT_GIVEN, EXPECTED_FIRST_NAME_WEIGHT);

		return retval;
	}
}
