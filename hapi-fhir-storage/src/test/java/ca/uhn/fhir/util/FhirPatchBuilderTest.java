package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FhirPatchBuilderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatchBuilderTest.class);
	private final FhirContext myContext = FhirContext.forR4Cached();
	private FhirPatch myPatchSvc;

	@BeforeEach
	void before() {
		myPatchSvc = new FhirPatch(myContext);
	}

	@Test
	void testAdd() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.add()
			.path("Patient")
			.name("identifier")
			.value(new Identifier().setSystem("http://system").setValue("value-new"));
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input = createPatientWith3Identifiers();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-0",
			"value-1",
			"value-2",
			"value-new"
		);
	}

	@Test
	void testInsert() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.insert()
			.path("Patient.identifier")
			.index(1)
			.value(new Identifier().setSystem("http://system").setValue("value-new"));
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input = createPatientWith3Identifiers();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-0",
			"value-new",
			"value-1",
			"value-2"
		);

	}

	@Test
	void testDelete() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.delete()
			.path("Patient.identifier[1]");
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input = createPatientWith3Identifiers();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-0",
			"value-2"
		);

	}

	@Test
	void testDeleteMulti() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.delete()
			.path("Patient.identifier.where(system='http://system0')")
			.allowMultipleMatches();
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input1 = new Patient();
		input1.addIdentifier().setSystem("http://system0").setValue("value-0");
		input1.addIdentifier().setSystem("http://system0").setValue("value-1");
		input1.addIdentifier().setSystem("http://system1").setValue("value-2");
		Patient input = input1;
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-2"
		);

	}

	@Test
	void testReplace() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.replace()
			.path("Patient.identifier[1]")
			.value(new Identifier().setSystem("http://system").setValue("value-new"));
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input = createPatientWith3Identifiers();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-0",
			"value-new",
			"value-2"
		);

	}

	@Test
	void testMove() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		builder
			.move()
			.path("Patient.identifier")
			.source(1)
			.destination(2);
		IBaseParameters patch = builder.build();
		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Patient input = createPatientWith3Identifiers();
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		List<String> actualIdentifier = input.getIdentifier().stream().map(t -> t.getValue()).toList();
		assertThat(actualIdentifier).containsExactly(
			"value-0",
			"value-2",
			"value-1"
		);

	}

	@Nonnull
	private static Patient createPatientWith3Identifiers() {
		Patient input = new Patient();
		input.addIdentifier().setValue("value-0");
		input.addIdentifier().setValue("value-1");
		input.addIdentifier().setValue("value-2");
		return input;
	}



	@Test
	void testDeleteThenAdd() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myContext);
		// Delete any existing LOINC codes
		builder
			.delete()
			.path("Observation.code.coding.where(system='http://loinc.org')")
			.allowMultipleMatches();
		// Add a specific LOINC code
		builder
			.add()
			.path("Observation.code")
			.name("coding")
			.value(new Coding("http://loinc.org", "85354-9", "Blood Pressure"));
		IBaseParameters patch = builder.build();

		ourLog.info("Patch:\n{}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		Observation input = new Observation();
		input
			.getCode()
			.addCoding(new Coding("http://loinc.org", "1", "Code 1"));
		input
			.getCode()
			.addCoding(new Coding("http://loinc.org", "2", "Code 2"));
		FhirPatch.PatchOutcome outcome = myPatchSvc.apply(input, patch);
		assertThat(outcome.getErrors()).isEmpty();

		assertEquals("http://loinc.org", input.getCode().getCoding().get(0).getSystem());
		assertEquals("85354-9", input.getCode().getCoding().get(0).getCode());
		assertEquals("Blood Pressure", input.getCode().getCoding().get(0).getDisplay());
		assertEquals(1, input.getCode().getCoding().size());
	}


}
