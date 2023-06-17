package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.FqlDataTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class FqlFhirPathParserTest {

	@ParameterizedTest
	@CsvSource(value = {
		// Good
		"Patient     , Patient.name.family                               ,   STRING",
		"Patient     , Patient.name.given.getValue().is(System.string)   ,   STRING",
		"Patient     , Patient.identifier.where(system='foo').system     ,   STRING",
		"Observation , Observation.value.ofType(Quantity).value          ,   DECIMAL",
		"Patient     , name.family                                       ,   STRING",
		"Patient     , name.given.getValue().is(System.string)           ,   STRING",
		"Patient     , identifier.where(system='foo').system             ,   STRING",
		"Observation , value.ofType(Quantity).value                      ,   DECIMAL",
		// Bad
		"Patient     , Patient.identifier                                ,   ",
		"Patient     , foo                                               ,   ",
	})
	public void testDetermineDatatypeForPath(String theResourceType, String theFhirPath, FqlDataTypeEnum theExpectedType) {
		FqlFhirPathParser svc = new FqlFhirPathParser(FhirContext.forR4Cached());
		FqlDataTypeEnum actual = svc.determineDatatypeForPath(theResourceType, theFhirPath);
		assertEquals(theExpectedType, actual);
	}
}
