package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmpiResourceMatcherSvcR4Test extends BaseEmpiRulesR4Test {
	private EmpiResourceMatcherSvc myEmpiResourceMatcherSvc;
	private Patient myJohn;
	private Patient myJohny;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		when(mySearchParamRetriever.getActiveSearchParam("Patient", "birthdate")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "active")).thenReturn(mock(RuntimeSearchParam.class));

		myEmpiResourceMatcherSvc = buildMatcher(buildActiveBirthdateIdRules());

		myJohn = buildJohn();
		myJohny = buildJohny();
	}

	@Test
	public void testCompareFirstNameMatch() {
		EmpiMatchOutcome result = myEmpiResourceMatcherSvc.match(myJohn, myJohny);
		assertMatchResult(EmpiMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, result);

	}

	@Test
	public void testCompareBothNamesMatch() {
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		EmpiMatchOutcome result = myEmpiResourceMatcherSvc.match(myJohn, myJohny);
		assertMatchResult(EmpiMatchResultEnum.MATCH, 3L, 1.816, false, false, result);
	}

	@Test
	public void testMatchResult() {
		assertMatchResult(EmpiMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, myEmpiResourceMatcherSvc.getMatchResult(myJohn, myJohny));
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		assertMatchResult(EmpiMatchResultEnum.MATCH, 3L, 1.816, false, false, myEmpiResourceMatcherSvc.getMatchResult(myJohn, myJohny));
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertMatchResult(EmpiMatchResultEnum.NO_MATCH, 0L, 0.0, false, false, myEmpiResourceMatcherSvc.getMatchResult(myJohn, patient3));
	}
}
