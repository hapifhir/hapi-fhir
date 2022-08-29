package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringParamTest {

	@Mock
	private FhirContext myContext;

	@Test
	public void testEquals() {
		StringParam input = new StringParam("foo", true);
		
		assertTrue(input.equals(input));
		assertFalse(input.equals(null));
		assertFalse(input.equals(""));
		assertFalse(input.equals(new StringParam("foo", false)));
	}

	@Test
	public void doSetValueAsQueryToken_withCustomSearchParameterAndNicknameQualifier_enablesNicknameExpansion(){
		String customSearchParamName = "firstName";
		StringParam stringParam = new StringParam();
		stringParam.doSetValueAsQueryToken(myContext, customSearchParamName, ":nickname", "John");
		assertNicknameQualifierSearchParameterIsValid(stringParam, "John");
	}

	@ParameterizedTest
	@ValueSource(strings = {"name", "given"})
	public void doSetValueAsQueryToken_withPredefinedSearchParametersAndNicknameQualifier_enablesNicknameExpansion(String theSearchParameterName){
		StringParam stringParam = new StringParam();
		stringParam.doSetValueAsQueryToken(myContext, theSearchParameterName, ":nickname", "John");
		assertNicknameQualifierSearchParameterIsValid(stringParam, "John");
	}

	private void assertNicknameQualifierSearchParameterIsValid(StringParam theStringParam, String theExpectedValue){
		assertTrue(theStringParam.isNicknameExpand());
		assertFalse(theStringParam.isExact());
		assertFalse(theStringParam.isContains());
		assertEquals(theExpectedValue, theStringParam.getValue());
	}
	
}
