package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FhirPatchR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatchR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testInvalidOperation() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("foo"));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals("Unknown patch operation type: foo", e.getMessage());
		}
	}

	@Test
	public void testInsertToInvalidIndex() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("insert"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.identifier"));
		operation
			.addPart()
			.setName("index")
			.setValue(new IntegerType(2));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals("Invalid insert index 2 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
		}
	}

	@Test
	public void testMoveFromInvalidIndex() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("move"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.identifier"));
		operation
			.addPart()
			.setName("source")
			.setValue(new IntegerType(2));
		operation
			.addPart()
			.setName("destination")
			.setValue(new IntegerType(1));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals("Invalid move source index 2 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
		}
	}

	@Test
	public void testMoveToInvalidIndex() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("sys");

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("move"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.identifier"));
		operation
			.addPart()
			.setName("source")
			.setValue(new IntegerType(0));
		operation
			.addPart()
			.setName("destination")
			.setValue(new IntegerType(1));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals("Invalid move destination index 1 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
		}
	}

	@Test
	public void testDeleteItemWithExtension() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().addExtension("http://foo", new StringType("abc"));
		patient.addIdentifier().setSystem("sys").setValue("val");

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("delete"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.identifier[0]"));

		svc.apply(patient, patch);

		assertEquals("{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}],\"active\":true}", ourCtx.newJsonParser().encodeResourceToString(patient));

	}

	@Test
	public void testGeneratePatch_ReplaceIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-1").setValue("value-1");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(2, diff.getParameter().size());
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[0].system", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("system-1", extractPartValuePrimitive(diff, 0, "operation", "value"));
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[0].value", extractPartValuePrimitive(diff, 1, "operation", "path"));
		assertEquals("value-1", extractPartValuePrimitive(diff, 1, "operation", "value"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testGeneratePatch_ReplaceChoice() {
		Patient oldValue = new Patient();
		oldValue.setDeceased(new BooleanType(true));

		Patient newValue = new Patient();
		newValue.setDeceased(new DateTimeType("2020-05-16"));


		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(1, diff.getParameter().size());
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.deceased", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("2020-05-16", extractPartValuePrimitive(diff, 0, "operation", "value"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testGeneratePatch_ReplaceChoice2() {
		Patient oldValue = new Patient();
		oldValue.setDeceased(new DateTimeType("2020-05-16"));

		Patient newValue = new Patient();
		newValue.setDeceased(new BooleanType(true));


		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(1, diff.getParameter().size());
		assertEquals("replace", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.deceased", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("true", extractPartValuePrimitive(diff, 0, "operation", "value"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testGeneratePatch_AddExtensionOnPrimitive() {
		Patient oldValue = new Patient();
		oldValue.setActive(true);

		Patient newValue = new Patient();
		newValue.setActive(true);
		newValue.getActiveElement().addExtension("http://foo", new StringType("a value"));

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(1, diff.getParameter().size());
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.active.extension", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("0", extractPartValuePrimitive(diff, 0, "operation", "index"));
		assertEquals("http://foo", extractPartValue(diff, 0, "operation", "value", Extension.class).getUrl());
		assertEquals("a value", extractPartValue(diff, 0, "operation", "value", Extension.class).getValueAsPrimitive().getValueAsString());

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testGeneratePatch_InsertIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-0").setValue("value-0");
		newValue.addIdentifier().setSystem("system-1").setValue("value-1");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(1, diff.getParameter().size());
		assertEquals("insert", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("1", extractPartValuePrimitive(diff, 0, "operation", "index"));
		assertEquals("Patient.identifier", extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("system-1", extractPartValue(diff, 0, "operation", "value", Identifier.class).getSystem());
		assertEquals("value-1", extractPartValue(diff, 0, "operation", "value", Identifier.class).getValue());

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	@Test
	public void testGeneratePatch_DeleteIdentifier() {
		Patient oldValue = new Patient();
		oldValue.addIdentifier().setSystem("system-0").setValue("value-0");
		oldValue.addIdentifier().setSystem("system-1").setValue("value-1");

		Patient newValue = new Patient();
		newValue.addIdentifier().setSystem("system-0").setValue("value-0");

		FhirPatch svc = new FhirPatch(ourCtx);
		Parameters diff = (Parameters) svc.diff(oldValue, newValue);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertEquals(1, diff.getParameter().size());
		assertEquals("delete", extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.identifier[1]", extractPartValuePrimitive(diff, 0, "operation", "path"));

		validateDiffProducesSameResults(oldValue, newValue, svc, diff);
	}

	public void validateDiffProducesSameResults(Patient theOldValue, Patient theNewValue, FhirPatch theSvc, Parameters theDiff) {
		theSvc.apply(theOldValue, theDiff);
		String expected = ourCtx.newJsonParser().encodeResourceToString(theNewValue);
		String actual = ourCtx.newJsonParser().encodeResourceToString(theOldValue);
		assertEquals(expected, actual);
	}

	public String extractPartValuePrimitive(Parameters theDiff, int theIndex, String theParameterName, String thePartName) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElseThrow(() -> new IllegalArgumentException());
		return ((IPrimitiveType) part.getValue()).getValueAsString();
	}

	public <T extends IBase> T extractPartValue(Parameters theDiff, int theIndex, String theParameterName, String thePartName, Class<T> theExpectedType) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assert theExpectedType.isAssignableFrom(part.getValue().getClass());
		return (T) part.getValue();
	}

}
