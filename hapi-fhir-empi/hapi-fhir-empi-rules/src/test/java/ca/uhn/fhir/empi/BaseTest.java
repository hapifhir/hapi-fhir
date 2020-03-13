package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;

public abstract class BaseTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final double EXPECTED_FIRST_NAME_WEIGHT = 0.23;
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


	@Before
	public void before() {
		myGivenNameMatchField = new EmpiFieldMatchJson(PATIENT_GIVEN, "Patient", "name.given", DistanceMetricEnum.COSINE, NAME_THRESHOLD);
		myLastNameMatchField = new EmpiFieldMatchJson(PATIENT_LAST, "Patient", "name.family", DistanceMetricEnum.JARO_WINKLER, NAME_THRESHOLD);
		myRules = new EmpiRulesJson();
		myRules.addMatchField(myGivenNameMatchField);
		myRules.addMatchField(myLastNameMatchField);

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
