package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.EmpiMatchFieldJson;
import ca.uhn.fhir.empi.rules.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.metric.EmpiResourceFieldComparator;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;

public abstract class BaseTest {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final double NAME_SIMILARITY = 0.8165;
	public static final double NAME_DELTA = 0.0001;

	protected Patient myPatient1;
	protected Patient myPatient2;
	protected EmpiMatchFieldJson myGivenNameMatchField;
	protected EmpiRulesJson myEmpiRules;

	@Before
	public void before() {
		myGivenNameMatchField = new EmpiMatchFieldJson("Patient", "name.given", DistanceMetricEnum.COSINE);
		myEmpiRules = new EmpiRulesJson();
		myEmpiRules.addMatchField(myGivenNameMatchField);
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
