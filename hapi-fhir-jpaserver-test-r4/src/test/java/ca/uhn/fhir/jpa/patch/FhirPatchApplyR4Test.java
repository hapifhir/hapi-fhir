package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.XmlExpectationsHelper;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	public void testInsertToInvalidIndex_minimum() {
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
			.setValue(new IntegerType(-1));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1270) + "Invalid insert index -1 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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
	public void testMoveFromInvalidIndex_minimum() {
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
			.setValue(new IntegerType(-1));
		operation
			.addPart()
			.setName("destination")
			.setValue(new IntegerType(1));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1268) + "Invalid move source index -1 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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
	public void testMoveToInvalidIndex_minimum() {
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
			.setValue(new IntegerType(-1));

		try {
			svc.apply(patient, patch);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1269) + "Invalid move destination index -1 for path Patient.identifier - Only have 0 existing entries", e.getMessage());
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

		assertThat(patient.getIdentifier()).hasSize(1);

		assertEquals("{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}],\"active\":true}", ourCtx.newJsonParser().encodeResourceToString(patient));

	}

	@Test
	public void testDeleteExtensionFromListByFilter() {
		FhirPatch svc = new FhirPatch(ourCtx);

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addExtension().setUrl("url1")
			.addExtension(new Extension().setUrl("text").setValue(new StringType("first text")))
			.addExtension(new Extension().setUrl("code").setValue(new CodeableConcept().addCoding(new Coding("sys", "123", "Abc"))));
		patient.addExtension().setUrl("url2")
			.addExtension(new Extension().setUrl("code").setValue(new CodeableConcept().addCoding(new Coding("sys", "234", "Def"))))
			.addExtension(new Extension().setUrl("detail").setValue(new IntegerType(5)));
		patient.addExtension().setUrl("url3")
			.addExtension(new Extension().setUrl("text").setValue(new StringType("third text")))
			.addExtension(new Extension().setUrl("code").setValue(new CodeableConcept().addCoding(new Coding("sys", "345", "Ghi"))))
			.addExtension(new Extension().setUrl("detail").setValue(new IntegerType(12)));

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
			.setValue(new StringType("Patient.extension.where(url = 'url2')"));

		svc.apply(patient, patch);

		assertThat(patient.getExtension()).hasSize(2);

		assertEquals("{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"url1\",\"extension\":[{\"url\":\"text\",\"valueString\":\"first text\"},{\"url\":\"code\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"sys\",\"code\":\"123\",\"display\":\"Abc\"}]}}]},{\"url\":\"url3\",\"extension\":[{\"url\":\"text\",\"valueString\":\"third text\"},{\"url\":\"code\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"sys\",\"code\":\"345\",\"display\":\"Ghi\"}]}},{\"url\":\"detail\",\"valueInteger\":12}]}],\"active\":true}", ourCtx.newJsonParser().encodeResourceToString(patient));

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

		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
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
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: New identifier is added, and does not overwrite.
		assertThat(patient.getIdentifier()).hasSize(2);
		assertEquals("first-system", patient.getIdentifier().get(0).getSystem());
		assertEquals("first-value", patient.getIdentifier().get(0).getValue());
		assertEquals("second-system", patient.getIdentifier().get(1).getSystem());
		assertEquals("second-value", patient.getIdentifier().get(1).getValue());
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
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it applies the new identifier correctly.
		assertThat(patient.getIdentifier()).hasSize(1);
		assertEquals("third-system", patient.getIdentifier().get(0).getSystem());
		assertEquals("third-value", patient.getIdentifier().get(0).getValue());
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
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it applies the new identifier correctly.
		assertThat(patient.getIdentifier()).hasSize(1);
		assertEquals("third-system", patient.getIdentifier().get(0).getSystem());
		assertEquals("third-value", patient.getIdentifier().get(0).getValue());
	}

	//TODO: https://github.com/hapifhir/hapi-fhir/issues/3796
	@Test
	public void testAddToSingleCardinalityFieldFailsWhenExists() {
		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();
		patient.setActive(true);

		//Given: We create a patch request to add a new active status
		BooleanType theValue = new BooleanType(false);
		Parameters patch = new Parameters();
		patch.addParameter(createPatchAddOperation("Patient", "active", theValue));

		//When: We apply the patch
		svc.apply(patient, patch);
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		//TODO THIS SHOULD THROW AN EXCEPTION. you cannot `add` to a field that is already set.
	}

	@Test
	public void testAddExtensionWithChoiceType() {
		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent addOperation = createPatchAddOperation("Patient", "extension", null);
		addOperation
			.addPart()
			.setName("value")
			.addPart(
				new Parameters.ParametersParameterComponent()
					.setName("url")
					.setValue(new UriType("http://foo/fhir/extension/foo"))
			)
			.addPart(
				new Parameters.ParametersParameterComponent()
					.setName("value")
					.setValue(new StringType("foo"))
			);
		patch.addParameter(addOperation);

		svc.apply(patient, patch);
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it adds the new extension correctly.
		assertThat(patient.getExtension()).hasSize(1);
		assertEquals("http://foo/fhir/extension/foo", patient.getExtension().get(0).getUrl());
		assertEquals("foo", patient.getExtension().get(0).getValueAsPrimitive().getValueAsString());
	}

	@Test
	public void testAddExtensionWithExtension() {
		final String extensionUrl  = "http://foo/fhir/extension/foo";
		final String innerExtensionUrl  = "http://foo/fhir/extension/innerExtension";
		final String innerExtensionValue = "2021-07-24T13:23:30-04:00";

		FhirPatch svc = new FhirPatch(ourCtx);
		Patient patient = new Patient();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent addOperation = createPatchAddOperation("Patient", "extension", null);
		addOperation
			.addPart()
			.setName("value")
			.addPart(
				new Parameters.ParametersParameterComponent()
					.setName("url")
					.setValue(new UriType(extensionUrl))
			)
			.addPart(
				new Parameters.ParametersParameterComponent()
					.setName("extension")
					.addPart(
						new Parameters.ParametersParameterComponent()
							.setName("url")
							.setValue(new UriType(innerExtensionUrl))
					)
					.addPart(
						new Parameters.ParametersParameterComponent()
							.setName("value")
							.setValue(new DateTimeType(innerExtensionValue))
					)
			);

		patch.addParameter(addOperation);

		ourLog.info("Patch:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patch));

		svc.apply(patient, patch);
		ourLog.debug("Outcome:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		//Then: it adds the new extension correctly.
		assertThat(patient.getExtension()).hasSize(1);
		Extension extension = patient.getExtension().get(0);
		assertEquals(extensionUrl, extension.getUrl());
		Extension innerExtension = extension.getExtensionFirstRep();

		assertNotNull(innerExtension);
		assertEquals(innerExtensionUrl, innerExtension.getUrl());
		assertEquals(innerExtensionValue, innerExtension.getValue().primitiveValue());

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

	// This implements the official HL7 test suite, as defined in http://hl7.org/fhir/R4/test-cases.zip. There may be some overlap with the cases above.
	@ParameterizedTest
	@CsvSource(textBlock = """
		No Difference, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1920-01-01"/></Patient>, <Parameters xmlns="http://hl7.org/fhir"></Parameters>, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1920-01-01"/></Patient>
		Replace Primitive, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1920-01-01"/></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.birthDate"/></part><part><name value="value"/><valueDate value="1930-01-01"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1930-01-01"/></Patient>
		Delete Primitive, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1920-01-01"/></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.birthDate"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"></Patient>
		Add Primitive, <Patient xmlns="http://hl7.org/fhir"></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="add"/></part><part><name value="path"/><valueString value="Patient"/></part><part><name value="name"/><valueString value="birthDate"/></part><part><name value="value"/><valueDate value="1930-01-01"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1930-01-01"/></Patient>
		Delete Primitive #2, <Patient xmlns="http://hl7.org/fhir"><birthDate value="1920-01-01"/></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.birthDate"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"></Patient>
		Replace Nested Primitive #1, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="male"/></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.contact[0].gender"/></part><part><name value="value"/><valueCode value="female"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="female"/></contact></Patient>
		Replace Nested Primitive #2, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="male"/></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.contact[0].name.text"/></part><part><name value="value"/><valueString value="the name"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="the name"/></name><gender value="male"/></contact></Patient>
		Delete Nested Primitive #1, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="male"/></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.contact[0].gender"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name></contact></Patient>
		Delete Nested Primitive #2, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="male"/></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.contact[0].name.text"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><gender value="male"/></contact></Patient>
		Add Nested Primitive, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="add"/></part><part><name value="path"/><valueString value="Patient.contact[0]"/></part><part><name value="name"/><valueString value="gender"/></part><part><name value="value"/><valueCode value="male"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name><gender value="male"/></contact></Patient>
		Add Complex, <Patient xmlns="http://hl7.org/fhir"></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="add"/></part><part><name value="path"/><valueString value="Patient"/></part><part><name value="name"/><valueString value="maritalStatus"/></part><part><name value="value"/><valueCodeableConcept><text value="married"/></valueCodeableConcept></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><maritalStatus><text value="married"/></maritalStatus></Patient>
		Replace Complex, <Patient xmlns="http://hl7.org/fhir"><maritalStatus id="1"><text value="married"/></maritalStatus></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.maritalStatus"/></part><part><name value="value"/><valueCodeableConcept id="2"><text value="not married"/></valueCodeableConcept></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><maritalStatus id="2"><text value="not married"/></maritalStatus></Patient>
		Delete Complex, <Patient xmlns="http://hl7.org/fhir"><maritalStatus><text value="married"/></maritalStatus></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.maritalStatus"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"></Patient>
		Add Anonymous Type, <Patient xmlns="http://hl7.org/fhir"></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="add"/></part><part><name value="path"/><valueString value="Patient"/></part><part><name value="name"/><valueString value="contact"/></part><part><name value="value"/><part><name value="name"/><valueHumanName><text value="a name"/></valueHumanName></part></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name></contact></Patient>
		Delete Anonymous Type, <Patient xmlns="http://hl7.org/fhir"><contact><name><text value="a name"/></name></contact></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.contact[0]"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"></Patient>
		List unchanged, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>
		List unchanged contents changed, <Patient xmlns="http://hl7.org/fhir"><identifier id="a"><system value="http://example.org"/><value value="value 1"/></identifier><identifier id="b"><system value="http://example.org"/><value value="value 2"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.identifier[0].value"/></part><part><name value="value"/><valueString value="value 2"/></part></parameter><parameter><name value="operation"/><part><name value="type"/><valueCode value="replace"/></part><part><name value="path"/><valueString value="Patient.identifier[1].value"/></part><part><name value="value"/><valueString value="value 1"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier id="a"><system value="http://example.org"/><value value="value 2"/></identifier><identifier id="b"><system value="http://example.org"/><value value="value 1"/></identifier></Patient>
		Add to list, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="insert"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="index"/><valueInteger value="2"/></part><part><name value="value"/><valueIdentifier><system value="http://example.org"/><value value="value 3"/></valueIdentifier></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Insert in list #1, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="insert"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="index"/><valueInteger value="1"/></part><part><name value="value"/><valueIdentifier><system value="http://example.org"/><value value="value 3"/></valueIdentifier></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>
		Insert in list #2, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="insert"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="index"/><valueInteger value="0"/></part><part><name value="value"/><valueIdentifier><system value="http://example.org"/><value value="value 3"/></valueIdentifier></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>
		Delete from List #1, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.identifier[0]"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Delete from List #2, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.identifier[1]"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Delete from List #3, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="delete"/></part><part><name value="path"/><valueString value="Patient.identifier[2]"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier></Patient>
		Reorder List #1, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="1"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Reorder List #2, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="0"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 4"/></identifier><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Reorder List #3, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="2"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier></Patient>
		Reorder List #4, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="0"/></part><part><name value="destination"/><valueInteger value="3"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier><identifier><system value="http://example.org"/><value value="value 1"/></identifier></Patient>
		Reorder List #5, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="1"/></part><part><name value="destination"/><valueInteger value="0"/></part></parameter><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="2"/></part><part><name value="destination"/><valueInteger value="1"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>
		Reorder List #6, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 1"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 4"/></identifier></Patient>, <Parameters xmlns="http://hl7.org/fhir"><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="0"/></part></parameter><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="1"/></part></parameter><parameter><name value="operation"/><part><name value="type"/><valueCode value="move"/></part><part><name value="path"/><valueString value="Patient.identifier"/></part><part><name value="source"/><valueInteger value="3"/></part><part><name value="destination"/><valueInteger value="2"/></part></parameter></Parameters>, <Patient xmlns="http://hl7.org/fhir"><identifier><system value="http://example.org"/><value value="value 4"/></identifier><identifier><system value="http://example.org"/><value value="value 3"/></identifier><identifier><system value="http://example.org"/><value value="value 2"/></identifier><identifier><system value="http://example.org"/><value value="value 1"/></identifier></Patient>
		""")
	public void testHL7Cases(String theName, String theInputResource, String thePatch, String theOutputResource) throws Exception {
		IParser parser = ourCtx.newXmlParser();
		Patient patient = parser.parseResource(Patient.class, theInputResource);
		Parameters parameters = parser.parseResource(Parameters.class, thePatch);
		Patient expectedPatient = parser.parseResource(Patient.class, theOutputResource);

		FhirPatch svc = new FhirPatch(ourCtx);
		svc.apply(patient, parameters);

		new XmlExpectationsHelper().assertXmlEqual(theOutputResource, parser.encodeResourceToString(patient));
		assertThat(expectedPatient.equalsDeep(patient)).as(theName).isTrue();
	}
}
