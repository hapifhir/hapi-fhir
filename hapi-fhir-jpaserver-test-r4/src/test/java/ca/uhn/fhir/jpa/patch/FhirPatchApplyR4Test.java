package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

	@Test
	public void testAddDoesNotReplaceHighCardinalityItems() {
		FhirPatch svc = new FhirPatch(ourCtx);

		//Given: We create a patient with an identifier
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("first-system").setValue("first-value");

		//Given: We create a patch request to add a second identifier.
		Identifier theValue = new Identifier().setSystem("second-system").setValue("second-value");
		Parameters patch = new Parameters();
		patch.addParameter(createPatchAddOperation( "Patient", "identifier", theValue));

		//When: We apply the patch
		svc.apply(patient, patch);
		ourLog.info("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: New identifier is added, and does not overwrite.
		assertThat(patient.getIdentifier(), hasSize(2));
		assertThat(patient.getIdentifier().get(0).getSystem(), is(equalTo("first-system")));
		assertThat(patient.getIdentifier().get(0).getValue(), is(equalTo("first-value")));
		assertThat(patient.getIdentifier().get(1).getSystem(), is(equalTo("second-system")));
		assertThat(patient.getIdentifier().get(1).getValue(), is(equalTo("second-value")));
	}

	@Test
	public void testAddToHighCardinalityFieldSetsValueIfEmpty() {
		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();

		//Given: We create a patch request to add an identifier.
		Parameters patch = new Parameters();
		Identifier theValue = new Identifier().setSystem("third-system").setValue("third-value");
		patch.addParameter(createPatchAddOperation("Patient", "identifier", theValue));

		//When: We apply the patch
		svc.apply(patient, patch);
		ourLog.info("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it applies the new identifier correctly.
		assertThat(patient.getIdentifier(), hasSize(1));
		assertThat(patient.getIdentifier().get(0).getSystem(), is(equalTo("third-system")));
		assertThat(patient.getIdentifier().get(0).getValue(), is(equalTo("third-value")));
	}
	@Test
	public void testReplaceToHighCardinalityFieldRemovesAllAndSetsValue() {
		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("first-system").setValue("first-value");
		patient.addIdentifier().setSystem("second-system").setValue("second-value");

		//Given: We create a patch request to add an identifier.
		Identifier theValue = new Identifier().setSystem("third-system").setValue("third-value");
		Parameters patch = new Parameters();
		patch.addParameter(createPatchReplaceOperation("Patient.identifier",  theValue));

		//When: We apply the patch
		svc.apply(patient, patch);
		ourLog.info("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it applies the new identifier correctly.
		assertThat(patient.getIdentifier(), hasSize(1));
		assertThat(patient.getIdentifier().get(0).getSystem(), is(equalTo("third-system")));
		assertThat(patient.getIdentifier().get(0).getValue(), is(equalTo("third-value")));
	}

	//TODO: https://github.com/hapifhir/hapi-fhir/issues/3796
	@Test
	public void testAddToSingleCardinalityFieldFailsWhenExists() {
		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();
		patient.setActive(true);

		//Given: We create a patch request to add a new active statuts
		BooleanType theValue = new BooleanType(false);
		Parameters patch = new Parameters();
		patch.addParameter(createPatchAddOperation("Patient", "active", theValue));

		//When: We apply the patch
		svc.apply(patient, patch);
		ourLog.info("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		//TODO THIS SHOULD THROW AN EXCEPTION. you cannot `add` to a field that is already set.
	}

	private Parameters.ParametersParameterComponent createPatchAddOperation(String thePath, String theName, Type theValue) {
		return createPatchOperation("add", thePath, theName, theValue, null);
	}

	private Parameters.ParametersParameterComponent createPatchReplaceOperation(String thePath, Type theValue) {
		return createPatchOperation("replace", thePath, null , theValue, null);
	}

	private Parameters.ParametersParameterComponent createPatchInsertOperation(String thePath, Type theValue) {
		return createPatchOperation("replace", thePath, null , theValue, null);
	}


	@Nonnull
	private Parameters.ParametersParameterComponent createPatchOperation(String theType, String thePath, String theName, Type theValue, Integer theIndex) {
		Parameters.ParametersParameterComponent operation = new Parameters.ParametersParameterComponent();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType(theType));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType(thePath));

		if (theName != null) {
			operation
				.addPart()
				.setName("name")
				.setValue(new StringType(theName));
		}
		if (theValue != null) {
			operation.addPart()
				.setName("value")
				.setValue(theValue);
		}
		if (theIndex != null) {
			operation.addPart()
				.setName("index")
				.setValue(new IntegerType(theIndex));
		}
		return operation;
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
