package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceMatcherSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public abstract class BaseR4Test {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	protected ISearchParamRetriever mySearchParamRetriever = mock(ISearchParamRetriever.class);

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

	protected EmpiResourceMatcherSvc buildMatcher(EmpiRulesJson theEmpiRulesJson) {
		EmpiResourceMatcherSvc retval = new EmpiResourceMatcherSvc(ourFhirContext, new EmpiSettings(new EmpiRuleValidator(ourFhirContext, mySearchParamRetriever)).setEmpiRules(theEmpiRulesJson));
		retval.init();
		return retval;
	}

	protected void assertMatch(EmpiMatchResultEnum theExpectedMatchEnum, EmpiMatchOutcome theMatchResult) {
		assertEquals(theExpectedMatchEnum, theMatchResult.getMatchResultEnum());
	}

	protected void assertMatchResult(EmpiMatchResultEnum theExpectedMatchEnum, long theExpectedVector, double theExpectedScore, boolean theExpectedNewPerson, boolean theExpectedEidMatch, EmpiMatchOutcome theMatchResult) {
		assertEquals(theExpectedScore, theMatchResult.score, 0.001);
		assertEquals(theExpectedVector, theMatchResult.vector);
		assertEquals(theExpectedEidMatch, theMatchResult.isEidMatch());
		assertEquals(theExpectedNewPerson, theMatchResult.isNewPerson());
		assertEquals(theExpectedMatchEnum, theMatchResult.getMatchResultEnum());
	}
}
