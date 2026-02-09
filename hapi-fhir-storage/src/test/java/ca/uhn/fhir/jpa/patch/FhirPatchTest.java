package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.FhirPatchBuilder;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.StringType;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPatchTest implements ITestDataBuilder {
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final IParser myParser = myFhirContext.newJsonParser();
	org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPatchTest.class);
	private FhirPatch myPatch;

	@BeforeEach
	public void before() {
		myPatch = new FhirPatch(myFhirContext);
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@ParameterizedTest
	@MethodSource("patchParams")
	public void patchApply_withValidParams_works(Parameters thePatch, String theReplacedPatientId, List<String> theExpectedContainedResources) {
		// setup
		String originalPatientId = "Patient/p1";

		ourLog.info("\n" + thePatch.getParameter().get(0).getPart().stream().filter(f -> f.getName().equals("path")).findFirst().get().getValue());

		Appointment appointment;
		{
			@Language("JSON")
			String appointmentStr = """
				{
				  "resourceType": "Appointment",
				  "id": "a1",
				  "status": "booked",
				  "participant": [
					{
					  "actor": {
						"reference": "Patient/p1"
					  },
					  "status": "accepted"
					},
					{
					  "actor": {
					    "reference": "Location/l1"
					  },
					  "status": "accepted"
					},
					{
					  "actor": {
					    "reference": "Patient/p3"
					  }
					}
				  ]
				}
				""";
			appointment = myParser.parseResource(Appointment.class, appointmentStr);
		}

		Predicate<Appointment.AppointmentParticipantComponent> originalPatientPred = p -> {
			return p.getActor() != null && p.getActor().getReference().equals(originalPatientId);
		};
		Predicate<Appointment.AppointmentParticipantComponent> newPatientPred = p -> {
			return p.getActor() != null && p.getActor().getReference().equals(theReplacedPatientId);
		};

		// precheck
		// should be no replacement id, but should have the original
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(originalPatientPred));
		assertFalse(appointment.getParticipant()
			.stream().anyMatch(newPatientPred));

		// test
		myPatch.apply(appointment, thePatch);

		// verify
		ourLog.info("\n" + myParser.encodeResourceToString(appointment));

		List<String> original = new ArrayList<>();
		original.add("Patient/p1");
		original.add("Location/l1");
		original.add("Patient/p3");
		for (String expectedParticipant : theExpectedContainedResources) {
			original.remove(expectedParticipant);
			assertTrue(
				appointment.getParticipant().stream().anyMatch(p -> {
						return p.getActor().getReference().equals(expectedParticipant);
					}
				), "Does not contain " + expectedParticipant
			);
		}
		// and verify any original values that were replaced are not there
		for (String orig : original) {
			assertFalse(
				appointment.getParticipant().stream().anyMatch(p -> {
					return p.getActor().getReference().equals(orig);
				}), "Contains " + orig + " when it shouldn't"
			);
		}
	}

	@Test
	public void patchApply_singleThatIsntASingleTarget_throws() {
		// setup
		Appointment appointment;
		Parameters parameters;
		{
			@Language("JSON")
			String appointmentStr = """
				{
				  "resourceType": "Appointment",
				  "id": "a1",
				  "status": "booked",
				  "participant": [
					{
					  "actor": {
						"reference": "Patient/p1"
					  },
					  "status": "accepted"
					},
					{
					  "actor": {
					  	"reference": "Patient/p2"
					  },
					  "status": "accepted"
					}
				  ]
				}
				""";
			appointment = myParser.parseResource(Appointment.class, appointmentStr);

			@Language("JSON")
			String patchStr = """
				{
				  "resourceType": "Parameters",
				  "parameter": [
					{
					  "name": "operation",
					  "part": [
						{
						  "name": "type",
						  "valueCode": "replace"
						},
						{
						  "name": "path",
						  "valueString": "Appointment.participant.actor.reference.where(startsWith('Patient')).single()"
						},
						{
						  "name": "value",
						  "valueString": "Patient/p2"
						}
					  ]
					}
				  ]
				}
				""";
			parameters = myParser.parseResource(Parameters.class, patchStr);
		}

		// test
		try {
			myPatch.apply(appointment, parameters);
		} catch (InvalidRequestException ex) {
			assertTrue(ex.getMessage().contains("List contains more than a single element"));
		}
	}

	/**
	 * Test case directly from the FHIR Patch spec
	 */
	@Test
	void testAdd_ObjectWithExtension() {
		String patchXml = """
			<Parameters xmlns="http://hl7.org/fhir">
			  <parameter>
			      <name value="operation"/>
			      <part>
			        <name value="type"/>
			        <valueString value="add"/>
			      </part>
			      <part>
			        <name value="path"/>
			        <valueString value="Specimen"/>
			      </part>
			      <part>
			        <name value="name"/>
			        <valueString value="processing"/>
			      </part>
			      <part>
			        <name value="value" />
			        <part>
			          <name value="description" />
			          <valueString value="test" />
			        </part>
			        <part>
			          <name value="time" />
			          <valueDateTime value="2021-08-13T07:44:38.342+00:00" />
			        </part>
			        <part>
			          <name value="extension" />
			          <part>
			            <name value="url" />
			            <valueUri value="http://example.org/fhir/DeviceExtension" />
			          </part>
			          <part>
			            <name value="value" />
			            <valueReference>
			              <reference value="Device/1"/>
			            </valueReference>
			          </part>
			        </part>
			      </part>
			  </parameter>
			</Parameters>""";
		Parameters patch = myFhirContext.newXmlParser().parseResource(Parameters.class, patchXml);

		Specimen input = new Specimen();
		myPatch.apply(input, patch);

		ourLog.info("Result:\n{}", myParser.encodeResourceToString(input));

		assertEquals("test", input.getProcessingFirstRep().getDescription());
		assertEquals("2021-08-13T07:44:38.342+00:00", input.getProcessingFirstRep().getTimeDateTimeType().getValueAsString());
		assertEquals(1, input.getProcessingFirstRep().getExtension().size());
		assertEquals("http://example.org/fhir/DeviceExtension", input.getProcessingFirstRep().getExtension().get(0).getUrl());
		assertEquals("Device/1", ((Reference) input.getProcessingFirstRep().getExtension().get(0).getValue()).getReference());
	}

	@Test
	public void testInsert_InvalidPath_NoDots() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myFhirContext);
		builder
			.insert()
			.path("Patient")
			.index(1)
			.value(new Identifier().setValue("value-new"));
		IBaseParameters patch = builder.build();

		Patient input = new Patient();
		input.addIdentifier().setValue("value-0");
		input.addIdentifier().setValue("value-1");
		input.addIdentifier().setValue("value-2");

		assertThatThrownBy(() -> myPatch.apply(input, patch))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Invalid path for insert operation (must point to a repeatable element): Patient");
	}

	@Test
	void testReplace_PathEndingInFilter() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myFhirContext);
		IBaseParameters patch = builder
			.replace()
			.path("Observation.code.coding.where(system='http://loinc.org')")
			.value(new Coding().setSystem("http://foo").setCode("code-new"))
			.andThen()
			.build();

		Observation input = (Observation) buildObservation(
			withObservationCode("http://loinc.org", "code"),
			withObservationCode("http://foo", "code")
		);

		myPatch.apply(input, patch);

		assertEquals("code-new", input.getCode().getCoding().get(0).getCode());
		assertEquals("code", input.getCode().getCoding().get(1).getCode());
	}

	@Test
	void fail_Delete_MatchesMultipleElements() {
		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.delete()
			.path("Patient.identifier")
			.andThen()
			.build();

		IBaseResource patient = buildPatient(
			withIdentifier("http://system0", "value0"),
			withIdentifier("http://system1", "value1"));

		assertThatThrownBy(() -> myPatch.apply(patient, patch))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Multiple elements found at Patient.identifier when deleting");

	}

	@Test
	void fail_Delete_MatchesMultipleElements_WithFilter() {
		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.delete()
			.path("Patient.identifier.where(system='http://system0')")
			.andThen()
			.build();

		IBaseResource patient = buildPatient(
			withIdentifier("http://system0", "value0"),
			withIdentifier("http://system0", "value1"),
			withIdentifier("http://system1", "value2"));

		assertThatThrownBy(()->myPatch.apply(patient, patch)).hasMessageContaining(
			"Multiple elements found at Patient.identifier.where(system='http://system0') when deleting"
		);
	}

	@Test
	void fail_Patch_PathResolvesToMultipleElements() {
		FhirPatchBuilder builder = new FhirPatchBuilder(myFhirContext);
		IBaseParameters patch = builder
			.replace()
			.path("Patient.name.given")
			.value(new StringType("Bart"))
			.andThen()
			.build();

		Patient input = (Patient) buildPatient(
			withGiven("Homer"),
			withGiven("Jay")
		);

		assertThatThrownBy(()->myPatch.apply(input, patch))
			.hasMessageContaining("FhirPath returns more than 1 element: Patient.name.given");

	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Appointment.participant.actor.reference.where(startsWith('Patient')",
		"Appointment.participant.actor.reference.where(contains(')')"
	})
	void fail_invalidPaths_throw(String thePath) {
		// setup
		Appointment appointment;
		{
			@Language("JSON")
			String apStr = """
				{
				      "participant": [
				          {
				              "actor": {
				                  "reference": "Patient/1"
				              },
				              "required": "required",
				              "status": "accepted"
				          }
				      ],
				      "resourceType": "Appointment"
				}
				""";
			appointment = myParser.parseResource(Appointment.class, apStr);
		}

		Parameters patch;
		{
			@Language("JSON")
			String patchStr = """
					{
				     "resourceType":"Parameters",
				     "parameter":[
				       {
				         "name":"operation",
				         "part":[
				           {
				             "name":"type",
				             "valueCode":"replace"
				           },
				           {
				             "name":"path",
				             "valueString": "FINDME"
				           },
				           {
				             "name":"value",
				             "valueString":"Patient/2"
				           }
				         ]
				       }
				     ]
				   }
				""".replace("FINDME", thePath);
			patch = myParser.parseResource(Parameters.class, patchStr);
		}

		// test
		assertThatThrownBy(() -> myPatch.apply(appointment, patch))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("is not a valid fhir path");

	}

	@Test
	void testValidate() {
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("foo");

		FhirPatch.PatchOutcome outcome = myPatch.validate(parameters);
		assertThat(outcome.getErrors()).
			containsExactly(
				"Unknown patch parameter name: foo"
			);
	}

	@Test
	void testReplace_ExtensionFilterSyntax_StringValue() {
		// Setup: Patient with an extension containing a string value
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName")
			.setValue(new StringType("One"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName').value")
			.value(new StringType("Two"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		Extension ext = input.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName");
		assertThat(ext).isNotNull();
		assertThat(ext.getValue()).isInstanceOf(StringType.class);
		assertThat(((StringType) ext.getValue()).getValue()).isEqualTo("Two");
	}

	@Test
	void testReplace_ExtensionFilterSyntax_DateTimeValue() {
		// Setup: EpisodeOfCare with an extension containing a dateTime value (mirrors original bug report)
		EpisodeOfCare input = new EpisodeOfCare();
		input.addExtension()
			.setUrl("http://example.org/fhir/StructureDefinition/episodeOfCare-nextReviewDate")
			.setValue(new DateTimeType("2025-01-01"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("EpisodeOfCare.extension('http://example.org/fhir/StructureDefinition/episodeOfCare-nextReviewDate').value")
			.value(new DateTimeType("2026-06-15"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		Extension ext = input.getExtensionByUrl("http://example.org/fhir/StructureDefinition/episodeOfCare-nextReviewDate");
		assertThat(ext).isNotNull();
		assertThat(ext.getValue()).isInstanceOf(DateTimeType.class);
		assertThat(((DateTimeType) ext.getValue()).getValueAsString()).isEqualTo("2026-06-15");
	}

	@Test
	void testReplace_ExtensionFilterSyntax_MultipleExtensions() {
		// Setup: Patient with multiple extensions, replace only the targeted one
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-a")
			.setValue(new StringType("ValueA"));
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-b")
			.setValue(new StringType("ValueB"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://example.org/fhir/ext-b').value")
			.value(new StringType("ValueB-Updated"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify: ext-a unchanged, ext-b updated
		assertThat(((StringType) input.getExtensionByUrl("http://example.org/fhir/ext-a").getValue()).getValue())
			.isEqualTo("ValueA");
		assertThat(((StringType) input.getExtensionByUrl("http://example.org/fhir/ext-b").getValue()).getValue())
			.isEqualTo("ValueB-Updated");
	}

	@Test
	void testReplace_ExtensionFilterSyntax_ReferenceValue() {
		// Setup: Patient with an extension containing a Reference value
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-ref")
			.setValue(new Reference("Practitioner/old"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://example.org/fhir/ext-ref').value")
			.value(new Reference("Practitioner/new"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		Extension ext = input.getExtensionByUrl("http://example.org/fhir/ext-ref");
		assertThat(ext).isNotNull();
		assertThat(((Reference) ext.getValue()).getReference()).isEqualTo("Practitioner/new");
	}

	@Test
	void testDelete_ExtensionWhereUrlSyntax() {
		// Control test: delete extension using .where(url='...') syntax (should already work)
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-delete")
			.setValue(new StringType("ToDelete"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.delete()
			.path("Patient.extension.where(url='http://example.org/fhir/ext-delete')")
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		assertThat(input.getExtensionByUrl("http://example.org/fhir/ext-delete")).isNull();
	}

	@Test
	void testReplace_ExtensionWhereUrlSyntax_StringValue() {
		// Replace extension value using .where(url='...') syntax
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName")
			.setValue(new StringType("One"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension.where(url='http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName').value")
			.value(new StringType("Two"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		Extension ext = input.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName");
		assertThat(ext).isNotNull();
		assertThat(((StringType) ext.getValue()).getValue()).isEqualTo("Two");
	}

	@Test
	void fail_Replace_ExtensionFilterSyntax_UrlNotFound() {
		// Negative test: extension URL that does not exist should give HAPI-2761
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-exists")
			.setValue(new StringType("Value"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://example.org/fhir/ext-nonexistent').value")
			.value(new StringType("NewValue"))
			.andThen()
			.build();

		assertThatThrownBy(() -> myPatch.apply(input, patch))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2761");
	}

	@Test
	void testReplace_ExtensionFilterSyntax_NestedExtension() {
		// Setup: Patient with nested extensions: extension('url1').extension('url2').value
		Patient input = (Patient) buildPatient(withGiven("Sunny"), withFamily("Day"));
		Extension outerExt = input.addExtension();
		outerExt.setUrl("http://example.org/fhir/ext-outer");
		outerExt.addExtension()
			.setUrl("http://example.org/fhir/ext-inner")
			.setValue(new StringType("InnerOld"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://example.org/fhir/ext-outer').extension('http://example.org/fhir/ext-inner').value")
			.value(new StringType("InnerNew"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		Extension outer = input.getExtensionByUrl("http://example.org/fhir/ext-outer");
		assertThat(outer).isNotNull();
		Extension inner = outer.getExtensionByUrl("http://example.org/fhir/ext-inner");
		assertThat(inner).isNotNull();
		assertThat(((StringType) inner.getValue()).getValue()).isEqualTo("InnerNew");
	}

	@Test
	void testReplace_ExtensionFilterSyntax_SingleExtension() {
		// Minimal case: resource with exactly one extension, replace its value
		Patient input = new Patient();
		input.addExtension()
			.setUrl("http://example.org/fhir/ext-only")
			.setValue(new StringType("Old"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.extension('http://example.org/fhir/ext-only').value")
			.value(new StringType("New"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		assertThat(input.getExtension()).hasSize(1);
		assertThat(((StringType) input.getExtension().get(0).getValue()).getValue()).isEqualTo("New");
	}

	@Test
	void testReplace_ModifierExtensionFilterSyntax_StringValue() {
		// Setup: Patient with a modifierExtension containing a string value
		Patient input = new Patient();
		input.addModifierExtension()
			.setUrl("http://example.org/fhir/mod-ext")
			.setValue(new StringType("Old"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.modifierExtension('http://example.org/fhir/mod-ext').value")
			.value(new StringType("New"))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		assertThat(input.getModifierExtension()).hasSize(1);
		Extension ext = input.getModifierExtension().get(0);
		assertThat(ext.getUrl()).isEqualTo("http://example.org/fhir/mod-ext");
		assertThat(((StringType) ext.getValue()).getValue()).isEqualTo("New");
	}

	@Test
	public void testReplace_ChoiceType() {
		// Setup: Patient with a choice type element (deceased[x]) initially set to a dateTime value
		Patient input = new Patient();
		input.setDeceased(new DateTimeType("2025-01-01"));

		IBaseParameters patch = new FhirPatchBuilder(myFhirContext)
			.replace()
			.path("Patient.deceased")
			.value(new BooleanType(true))
			.andThen()
			.build();

		// Test
		myPatch.apply(input, patch);

		// Verify
		assertThat(input.getDeceased()).isInstanceOf(BooleanType.class);
		assertThat(((BooleanType) input.getDeceased()).getValue()).isTrue();
	}

	/**
	 * Returns a list of parameters for the
	 * patchApply_withValidParams_works test.
	 * <p>
	 * All inputs should assume an Appointment with the following references:
	 * * Patient/p1
	 * * Location/l1
	 * * Patient/p3
	 */
	static List<Arguments> patchParams() {
		List<Arguments> params = new ArrayList<>();

		// 1 - Appointment.participant.actor.reference.where(startsWith('Patient/')).first()
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).first()"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p2", "Location/l1", "Patient/p3")));
		}

		// 2 - Appointment.participant.actor.where(reference.startsWith('Patient/')).first()
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.where(reference.startsWith('Patient/')).first()"));
			comp.addPart().setName("value").setValue(new Reference("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/P2", List.of("Patient/p2", "Location/l1", "Patient/p3")));
		}

		// 3 - Appointment.participant.actor.where(reference.startsWith('Patient/'))[0]
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			// should act like "first"
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.where(reference.startsWith('Patient/'))[0]"));
			comp.addPart().setName("value").setValue(new Reference("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p2", "Location/l1", "Patient/p3")));
		}

		// 4 - Appointment.participant.actor.where(reference.startsWith('Patient/'))[1]
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			// should act like "first"
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.where(reference.startsWith('Patient/'))[1]"));
			comp.addPart().setName("value").setValue(new Reference("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p1", "Location/l1", "Patient/p2")));
		}

		// 5 - Appointment.participant.actor.reference.where(startsWith('Patient/')).last()
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");
			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).last()"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p1", "Location/l1", "Patient/p2")));
		}

		// 6 - Appointment.participant.actor.reference.where(endsWith('Patient/p1')).single()
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");
			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(endsWith('Patient/p1')).single()"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p3", "Location/l1", "Patient/p2")));
		}

		// 7 - Appointment.participant.actor.reference.where(startsWith('Patient/')).tail()
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");
			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).tail()"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			// tail should be the same as replacing everything (that matches Patient/) except Patient/p1
			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p1", "Location/l1", "Patient/p2")));
		}

		// 8 - Appointment.participant.actor.reference.where(startsWith('Patient/')).skip(1)
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");
			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).skip(1)"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			// skip should skip the first value that matches "Patient" and replace it (ie, skip Patient/p1 and replace Patient/p3)
			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p1", "Location/l1", "Patient/p2")));
		}

		// 9 - Appointment.participant.actor.reference.where(startsWith('Patient/')).take(1)
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");
			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).take(1)"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));

			// take should grab the first value that matches "Patient" (ie, take should grab Patient/p1 and not Patient/p3)
			params.add(Arguments.of(parameters, "Patient/p2", List.of("Patient/p3", "Location/l1", "Patient/p2")));
		}

		return params;
	}


}
