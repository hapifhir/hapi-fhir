package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HfqlFhirPathParserTest {

	@ParameterizedTest
	@CsvSource(value = {
		// Good
		"Patient     , Patient.name.family                                       ,   JSON",
		"Patient     , Patient.name[0].family                                    ,   STRING",
		"Patient     , Patient.name.family[0]                                    ,   JSON",
		"Patient     , Patient.name[0].family[0]                                 ,   STRING",
		"Patient     , Patient.name.given.getValue().is(System.string)           ,   JSON",
		"Patient     , Patient.name.given.getValue().is(System.string).first()   ,   STRING",
		"Patient     , Patient.identifier.where(system='foo').system             ,   JSON",
		"Patient     , Patient.identifier.where(system='foo').first().system     ,   STRING",
		"Observation , Observation.value.ofType(Quantity).value                  ,   DECIMAL",
		"Patient     , name.family                                               ,   JSON",
		"Patient     , name[0].family[0]                                         ,   STRING",
		"Patient     , name.given.getValue().is(System.string)                   ,   JSON",
		"Patient     , identifier.where(system='foo').system                     ,   JSON",
		"Patient     , identifier[0].where(system='foo').system                  ,   STRING",
		"Observation , value.ofType(Quantity).value                              ,   DECIMAL",
		"Patient     , Patient.meta.versionId.toInteger()                        ,   INTEGER",
		"Patient     , Patient.identifier                                        ,   JSON",
		// Bad
		"Patient     , foo                                                       ,   ",
	})
	void testDetermineDatatypeForPath(String theResourceType, String theFhirPath, HfqlDataTypeEnum theExpectedType) {
		HfqlFhirPathParser svc = new HfqlFhirPathParser(FhirContext.forR4Cached());
		HfqlDataTypeEnum actual = svc.determineDatatypeForPath(theResourceType, theFhirPath);
		assertEquals(theExpectedType, actual);
	}


	@Test
	void testAllFhirDataTypesHaveMappings() {
		FhirContext ctx = FhirContext.forR5Cached();
		int foundCount = 0;
		for (BaseRuntimeElementDefinition<?> next : ctx.getElementDefinitions()) {
			if (next instanceof RuntimePrimitiveDatatypeDefinition) {
				assertThat(HfqlFhirPathParser.getHfqlDataTypeForFhirType(next.getName())).as(() -> "No mapping for type: " + next.getName()).isNotNull();
				foundCount++;
			}
		}
		assertEquals(21, foundCount);
	}


}
