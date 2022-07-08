package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.mdm.BaseR4Test;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmFilterSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseMdmRulesR4Test extends BaseR4Test {
	public static final String PATIENT_GIVEN = "patient-given";
	public static final String PATIENT_GIVEN_FIRST = "patient-given-first";
	public static final String PATIENT_FAMILY = "patient-last";
	public static final String PATIENT_EID_FOR_TEST = "http://hello.com/naming/patient-eid";
	public static final String MEDICATION_EID_FOR_TEST= "http://hello.com/naming/medication-eid";
	public static final String PRACTITIONER_EID_FOR_TEST = "http://hello.com/naming/practitioner-eid";

	public static final double NAME_THRESHOLD = 0.8;
	protected MdmFieldMatchJson myGivenNameMatchField;
	protected String myBothNameFields;
	protected MdmRulesJson myMdmRulesJson;

	@BeforeEach
	public void before() {
		myMdmRulesJson = new MdmRulesJson();

		ArrayList<String> myLegalMdmTypes = new ArrayList<>();
		myLegalMdmTypes.add("Patient");
		myMdmRulesJson.setMdmTypes(myLegalMdmTypes);

		myGivenNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setSimilarity(new MdmSimilarityJson().setAlgorithm(MdmSimilarityEnum.COSINE).setMatchThreshold(NAME_THRESHOLD));

		myBothNameFields = String.join(",", PATIENT_GIVEN, PATIENT_FAMILY);
	}

	protected MdmRulesJson buildActiveBirthdateIdRules() {
		MdmFilterSearchParamJson activePatientsBlockingFilter = new MdmFilterSearchParamJson()
			.setResourceType("Patient")
			.setSearchParam(Patient.SP_ACTIVE)
			.setFixedValue("true");

		MdmResourceSearchParamJson patientBirthdayBlocking = new MdmResourceSearchParamJson()
			.setResourceType("Patient")
			.addSearchParam(Patient.SP_BIRTHDATE);
		MdmResourceSearchParamJson patientIdentifierBlocking = new MdmResourceSearchParamJson()
			.setResourceType("Patient")
			.addSearchParam(Patient.SP_IDENTIFIER);


		MdmFieldMatchJson lastNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_FAMILY)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setSimilarity(new MdmSimilarityJson().setAlgorithm(MdmSimilarityEnum.JARO_WINKLER).setMatchThreshold(NAME_THRESHOLD));

		MdmRulesJson retval = new MdmRulesJson();
		retval.setVersion("test version");
		retval.addResourceSearchParam(patientBirthdayBlocking);
		retval.addResourceSearchParam(patientIdentifierBlocking);
		retval.addFilterSearchParam(activePatientsBlockingFilter);
		retval.addMatchField(myGivenNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.setMdmTypes(Arrays.asList("Patient", "Practitioner", "Medication"));
		retval.putMatchResult(myBothNameFields, MdmMatchResultEnum.MATCH);
		retval.putMatchResult(PATIENT_GIVEN, MdmMatchResultEnum.POSSIBLE_MATCH);

		retval.addEnterpriseEIDSystem("Patient", PATIENT_EID_FOR_TEST);
		retval.addEnterpriseEIDSystem("Medication", MEDICATION_EID_FOR_TEST);
		retval.addEnterpriseEIDSystem("Practitioner", PRACTITIONER_EID_FOR_TEST);
		return retval;
	}
}
