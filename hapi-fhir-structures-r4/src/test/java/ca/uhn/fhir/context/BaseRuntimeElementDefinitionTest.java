package ca.uhn.fhir.context;

import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class BaseRuntimeElementDefinitionTest {

	private static FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	public void testNewInstance_InvalidArgumentType() {

		BaseRuntimeElementDefinition<?> def = ourFhirContext.getElementDefinition("string");

		try {
			def.newInstance(123);
			fail();		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1696) + "Failed to instantiate type:org.hl7.fhir.r4.model.StringType", e.getMessage());
		}
	}

	@Test
	void mutator_remove() {
		Patient patient = new Patient();
		patient.addName().setFamily("A1");
		patient.addName().setFamily("A2");

		assertThat(patient.getName()).hasSize(2);
		assertEquals("A1", patient.getName().get(0).getFamily());
		RuntimeResourceDefinition def = ourFhirContext.getResourceDefinition(patient);
		BaseRuntimeChildDefinition child = def.getChildByName("name");
		BaseRuntimeChildDefinition.IMutator mutator = child.getMutator();

		mutator.remove(patient, 0);
		assertThat(patient.getName()).hasSize(1);
		assertEquals("A2", patient.getName().get(0).getFamily());
	}

	@Test
	void mutator_remov_nonList() {
		Patient patient = new Patient();
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		RuntimeResourceDefinition def = ourFhirContext.getResourceDefinition(patient);
		BaseRuntimeChildDefinition child = def.getChildByName("gender");
		BaseRuntimeChildDefinition.IMutator mutator = child.getMutator();
		try {
			mutator.remove(patient, 0);
			fail();		} catch (UnsupportedOperationException e) {
			assertEquals("HAPI-2142: Remove by index can only be called on a list-valued field.  'gender' is a single-valued field.", e.getMessage());
		}
	}
}
