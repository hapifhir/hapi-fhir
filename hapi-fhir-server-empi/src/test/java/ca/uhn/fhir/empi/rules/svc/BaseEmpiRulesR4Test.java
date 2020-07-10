package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseEmpiRulesR4Test extends BaseR4Test {
	public static final String PATIENT_GIVEN = "patient-given";
	public static final String PATIENT_LAST = "patient-last";

	public static final double NAME_THRESHOLD = 0.8;
	protected EmpiFieldMatchJson myGivenNameMatchField;
	protected String myBothNameFields;

	@BeforeEach
	public void before() {
		myGivenNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMetric(EmpiMetricEnum.COSINE)
			.setMatchThreshold(NAME_THRESHOLD);
		myBothNameFields = String.join(",", PATIENT_GIVEN, PATIENT_LAST);
	}

	protected EmpiRulesJson buildActiveBirthdateIdRules() {
		EmpiFilterSearchParamJson activePatientsBlockingFilter = new EmpiFilterSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_ACTIVE)
			.setFixedValue("true");

		EmpiResourceSearchParamJson patientBirthdayBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.addSearchParam(Patient.SP_BIRTHDATE);
		EmpiResourceSearchParamJson patientIdentifierBlocking = new EmpiResourceSearchParamJson()
			.setResourceType("Patient")
			.addSearchParam(Patient.SP_IDENTIFIER);


		EmpiFieldMatchJson lastNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_LAST)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMetric(EmpiMetricEnum.JARO_WINKLER)
			.setMatchThreshold(NAME_THRESHOLD);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.setVersion("test version");
		retval.addResourceSearchParam(patientBirthdayBlocking);
		retval.addResourceSearchParam(patientIdentifierBlocking);
		retval.addFilterSearchParam(activePatientsBlockingFilter);
		retval.addMatchField(myGivenNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.putMatchResult(myBothNameFields, EmpiMatchResultEnum.MATCH);
		retval.putMatchResult(PATIENT_GIVEN, EmpiMatchResultEnum.POSSIBLE_MATCH);
		return retval;
	}
}
