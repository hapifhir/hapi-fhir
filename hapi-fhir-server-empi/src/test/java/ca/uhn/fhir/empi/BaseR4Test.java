package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.empi.rules.json.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseR4Test {
	public static final String PATIENT_GIVEN = "patient-given";
	public static final String PATIENT_LAST = "patient-last";
	public static final String PATIENT_GENERAL_PRACTITIONER = "patient-practitioner";
	public static final double NAME_THRESHOLD = 0.8;
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	protected EmpiFieldMatchJson myGivenNameMatchField;
	protected EmpiFieldMatchJson myParentMatchField;
	protected String myBothNameFields;

	@BeforeEach
	public void before() {
		myGivenNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMetric(DistanceMetricEnum.COSINE)
			.setMatchThreshold(NAME_THRESHOLD);
		myBothNameFields = String.join(",", PATIENT_GIVEN, PATIENT_LAST);
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
		retval.putMatchResult(myBothNameFields, EmpiMatchResultEnum.MATCH);
		retval.putMatchResult(PATIENT_GIVEN, EmpiMatchResultEnum.POSSIBLE_MATCH);
		return retval;
	}

	protected EmpiResourceComparatorSvc buildComparator(EmpiRulesJson theEmpiRulesJson) {
		EmpiResourceComparatorSvc retval = new EmpiResourceComparatorSvc(ourFhirContext, new EmpiSettings().setEmpiRules(theEmpiRulesJson));
		retval.init();
		return retval;
	}
}
