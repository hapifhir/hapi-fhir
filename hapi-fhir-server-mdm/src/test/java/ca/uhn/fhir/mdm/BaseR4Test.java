package ca.uhn.fhir.mdm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public abstract class BaseR4Test {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	protected ISearchParamRegistry mySearchParamRetriever = mock(ISearchParamRegistry.class);

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

	protected MdmResourceMatcherSvc buildMatcher(MdmRulesJson theMdmRulesJson) {
		MdmResourceMatcherSvc retval = new MdmResourceMatcherSvc(ourFhirContext, new MdmSettings(new MdmRuleValidator(ourFhirContext, mySearchParamRetriever)).setMdmRules(theMdmRulesJson));
		retval.init();
		return retval;
	}

	protected void assertMatch(MdmMatchResultEnum theExpectedMatchEnum, MdmMatchOutcome theMatchResult) {
		assertEquals(theExpectedMatchEnum, theMatchResult.getMatchResultEnum());
	}

	protected void assertMatchResult(MdmMatchResultEnum theExpectedMatchEnum, long theExpectedVector, double theExpectedScore, boolean theExpectedNewGoldenResource, boolean theExpectedEidMatch, MdmMatchOutcome theMatchResult) {
		assertEquals(theExpectedScore, theMatchResult.score, 0.001);
		assertEquals(theExpectedVector, theMatchResult.vector);
		assertEquals(theExpectedEidMatch, theMatchResult.isEidMatch());
		assertEquals(theExpectedNewGoldenResource, theMatchResult.isCreatedNewResource());
		assertEquals(theExpectedMatchEnum, theMatchResult.getMatchResultEnum());
	}
}
