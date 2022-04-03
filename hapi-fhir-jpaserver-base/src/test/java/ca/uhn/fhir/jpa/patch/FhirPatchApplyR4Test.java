package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirPatchApplyR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatchApplyR4Test.class);

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
			assertEquals(Msg.code(1267) + "Unknown patch operation type: foo", e.getMessage());
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
			assertEquals(Msg.code(1270) + "Invalid insert index 2 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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
			assertEquals(Msg.code(1268) + "Invalid move source index 2 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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
			assertEquals(Msg.code(1269) + "Invalid move destination index 1 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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

	/**
	 * See #1999
	 */
	@Test
	public void testInsertToNonZeroIndex() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Questionnaire patient = new Questionnaire();
		Questionnaire.QuestionnaireItemComponent item = patient.addItem();
		item.setLinkId("1");
		item.addCode().setSystem("https://smilecdr.com/fhir/document-type").setCode("CLINICAL");
		item.setText("Test item");

		Parameters.ParametersParameterComponent operation;
		Parameters patch = new Parameters();
		operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("insert"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Questionnaire.item"));
		operation
			.addPart()
			.setName("index")
			.setValue(new IntegerType(1));
		operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("insert"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Questionnaire.item[1].linkId"));
		operation
			.addPart()
			.setName("index")
			.setValue(new IntegerType(0));
		operation
			.addPart()
			.setName("value")
			.setValue(new StringType("2"));
		operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("insert"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Questionnaire.item[1].code"));
		operation
			.addPart()
			.setName("index")
			.setValue(new IntegerType(0));
		operation
			.addPart()
			.setName("value")
			.setValue(new Coding("http://smilecdr.com/fhir/document-type", "ADMIN", null));
		operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("insert"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Questionnaire.item[1].text"));
		operation
			.addPart()
			.setName("index")
			.setValue(new IntegerType(0));
		operation
			.addPart()
			.setName("value")
			.setValue(new StringType("Test Item 2"));

		svc.apply(patient, patch);

		ourLog.info("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals("{\"resourceType\":\"Questionnaire\",\"item\":[{\"linkId\":\"1\",\"code\":[{\"system\":\"https://smilecdr.com/fhir/document-type\",\"code\":\"CLINICAL\"}],\"text\":\"Test item\"},{\"linkId\":\"2\",\"code\":[{\"system\":\"http://smilecdr.com/fhir/document-type\",\"code\":\"ADMIN\"}],\"text\":\"Test Item 2\"}]}", ourCtx.newJsonParser().encodeResourceToString(patient));


	}

	public static String extractPartValuePrimitive(Parameters theDiff, int theIndex, String theParameterName, String thePartName) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElseThrow(() -> new IllegalArgumentException());
		return ((IPrimitiveType) part.getValue()).getValueAsString();
	}

	@Nullable
	public static <T extends IBase> T extractPartValue(Parameters theDiff, int theIndex, String theParameterName, String thePartName, Class<T> theExpectedType) {
		Parameters.ParametersParameterComponent component = theDiff.getParameter().stream().filter(t -> t.getName().equals(theParameterName)).collect(Collectors.toList()).get(theIndex);
		Parameters.ParametersParameterComponent part = component.getPart().stream().filter(t -> t.getName().equals(thePartName)).findFirst().orElse(null);
		if (part == null) {
			return null;
		}

		if (IBaseResource.class.isAssignableFrom(theExpectedType)) {
			return (T) part.getResource();
		} else {
			assert theExpectedType.isAssignableFrom(part.getValue().getClass());
			return (T) part.getValue();
		}
	}

}
