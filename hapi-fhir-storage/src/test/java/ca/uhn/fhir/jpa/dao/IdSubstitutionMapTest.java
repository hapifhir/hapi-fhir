package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class IdSubstitutionMapTest {

	private IdSubstitutionMap idSubstitutions;

	@BeforeEach
	void setUp() {
		idSubstitutions = new IdSubstitutionMap();
	}

	@ParameterizedTest
	@CsvSource({
		"Patient/123/_history/3, Patient/123/_history/2",
		"Patient/123/_history/3, Patient/123"
	})
	void testUpdateTargets_inputMatchesTarget_onlyMatchedTargetUpdated(String theInputId, String theTargetId) {
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType(theTargetId));
		idSubstitutions.put(new IdType("urn:uuid:5000"), new IdType("Patient/5000"));
		idSubstitutions.put(new IdType("urn:uuid:6000"), new IdType("Patient/6000_history/3"));

		idSubstitutions.updateTargets(new IdType(theInputId));

		assertEquals(theInputId, idSubstitutions.getForSource("urn:uuid:1234").getValue());
		assertEquals("Patient/5000", idSubstitutions.getForSource("urn:uuid:5000").getValue());
		assertEquals("Patient/6000_history/3", idSubstitutions.getForSource("urn:uuid:6000").getValue());
	}

	@Test
	void testUpdateTargets_inputMatchesAllTargets_allTargetsUpdated() {
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123/_history/1"));
		idSubstitutions.put(new IdType("urn:uuid:5000"), new IdType("Patient/123/_history/2"));
		idSubstitutions.put(new IdType("urn:uuid:6000"), new IdType("Patient/123/_history/4"));

		idSubstitutions.updateTargets(new IdType("Patient/123/_history/3"));

		assertEquals("Patient/123/_history/3", idSubstitutions.getForSource("urn:uuid:1234").getValue());
		assertEquals("Patient/123/_history/3", idSubstitutions.getForSource("urn:uuid:5000").getValue());
		assertEquals("Patient/123/_history/3", idSubstitutions.getForSource("urn:uuid:6000").getValue());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Patient/124", "Patient/124/_history/3", "Patient", ""})
	void testUpdateTargets_noMatchingTarget_noUpdate(String theInputId) {
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123/_history/3"));
		idSubstitutions.updateTargets(new IdType(theInputId));
		assertEquals("Patient/123/_history/3", idSubstitutions.getForSource("urn:uuid:1234").getValue());
	}

	@Test
	void testUpdateTargets_nullInputId_noExceptionAndNoUpdate() {
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123/_history/3"));
		idSubstitutions.updateTargets(null);
		assertEquals("Patient/123/_history/3", idSubstitutions.getForSource("urn:uuid:1234").getValue());
	}
}
