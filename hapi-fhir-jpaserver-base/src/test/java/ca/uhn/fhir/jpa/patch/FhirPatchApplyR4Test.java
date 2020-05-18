package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FhirPatchApplyR4Test {

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

	public static String extractPartValuePrimitive(Parameters theDiff, int theIndex, String theParameterName, String thePartName) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElseThrow(() -> new IllegalArgumentException());
		return ((IPrimitiveType) part.getValue()).getValueAsString();
	}

	public static <T extends IBase> T extractPartValue(Parameters theDiff, int theIndex, String theParameterName, String thePartName, Class<T> theExpectedType) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElseThrow(() -> new IllegalArgumentException());

		if (IBaseResource.class.isAssignableFrom(theExpectedType)) {
			return (T) part.getResource();
		} else {
			assert theExpectedType.isAssignableFrom(part.getValue().getClass());
			return (T) part.getValue();
		}
	}

}
