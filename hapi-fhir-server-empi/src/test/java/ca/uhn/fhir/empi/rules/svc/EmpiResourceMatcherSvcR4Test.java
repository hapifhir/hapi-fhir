package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "active")).thenReturn(mock(RuntimeSearchParam.class));

		myEmpiResourceMatcherSvc = buildMatcher(buildActiveBirthdateIdRules());

		myJohn = buildJohn();
		myJohny = buildJohny();
	}

	@Test
	public void testCompareFirstNameMatch() {
		EmpiMatchResultEnum result = myEmpiResourceMatcherSvc.match(myJohn, myJohny).getMatchResultEnum();
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, result);
	}

	@Test
	public void testCompareBothNamesMatch() {
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		EmpiMatchResultEnum result = myEmpiResourceMatcherSvc.match(myJohn, myJohny).getMatchResultEnum();
		assertEquals(EmpiMatchResultEnum.MATCH, result);
	}

	@Test
	public void testMatchResult() {
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, myEmpiResourceMatcherSvc.getMatchResult(myJohn, myJohny).getMatchResultEnum());
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		assertEquals(EmpiMatchResultEnum.MATCH, myEmpiResourceMatcherSvc.getMatchResult(myJohn, myJohny).getMatchResultEnum());
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myEmpiResourceMatcherSvc.getMatchResult(myJohn, patient3).getMatchResultEnum());
	}
}
