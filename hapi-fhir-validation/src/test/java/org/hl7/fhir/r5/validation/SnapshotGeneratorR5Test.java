package org.hl7.fhir.r5.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SnapshotGeneratorR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(SnapshotGeneratorR5Test.class);

	@Test
	public void testGenerateSnapshot_ExtendPatient() {
		// Setup

		StructureDefinition sdPatient = new StructureDefinition();
		sdPatient.setId("sd-patient");
		sdPatient.setDerivation(StructureDefinition.TypeDerivationRule.SPECIALIZATION);
		sdPatient.setType("Patient");
		sdPatient.setUrl("http://sd-patient");
		sdPatient.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Patient");
		sdPatient.getDifferential()
			.addElement()
			.setPath("Patient.identifier")
			.addType(new ElementDefinition.TypeRefComponent("Identifier"))
			.setMin(2)
			.setMax("*")
			.setId("Patient.identifier");

		FhirContext ctx = FhirContext.forR5Cached();
		IValidationSupport vs = ctx.getValidationSupport();

		// Test
		StructureDefinition snapshot = (StructureDefinition) vs.generateSnapshot(new ValidationSupportContext(vs), sdPatient, null, null, null);
		ourLog.info(ctx.newJsonParser().setPrettyPrint(true).encodeToString(snapshot));

		// This was modified in the specialization
		assertNotNull(snapshot);
		assertEquals(2, findElementByPath(snapshot, "Patient.identifier").getMin());
		// This was not
		assertEquals(0, findElementByPath(snapshot, "Patient.active").getMin());


	}

	@Nonnull
	private static ElementDefinition findElementByPath(StructureDefinition snapshot, String path) {
		ElementDefinition element = snapshot.getSnapshot().getElement().stream().filter(t -> t.getPath().equals(path)).findFirst().orElseThrow();
		return element;
	}


}
