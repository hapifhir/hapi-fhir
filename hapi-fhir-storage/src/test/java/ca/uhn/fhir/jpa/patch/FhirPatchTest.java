package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirPatchTest {
	org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPatchTest.class);

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	private final IParser myParser = myFhirContext.newJsonParser();

	private FhirPatch myPatch;

	@BeforeEach
	public void before() {
		myPatch = new FhirPatch(myFhirContext);
	}

	/**
	 * Returns a list of parameters for the
	 * patchApply_withValidParams_works test.
	 *
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
		ourLog.trace(myParser.encodeResourceToString(appointment));

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
	public void patchApply_withPrimitiveTarget_shouldWork() {
		// setup
		String originalPatientId = "Patient/p1";
		String replacedPatientId = "Patient/p2";

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
						  "valueString": "Appointment.participant.actor.reference.where(startsWith('Patient/')).first()"
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

		Predicate<Appointment.AppointmentParticipantComponent> originalPatientPred = p -> {
			return p.getActor() != null && p.getActor().getReference().equals(originalPatientId);
		};
		Predicate<Appointment.AppointmentParticipantComponent> newPatientPred = p -> {
			return p.getActor() != null && p.getActor().getReference().equals(replacedPatientId);
		};

		// precheck
		// should be no replacement, but should have the original
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(originalPatientPred));
		assertFalse(appointment.getParticipant()
			.stream().anyMatch(newPatientPred));

		// test
		myPatch.apply(appointment, parameters);

		// verify
		ourLog.trace(myParser.encodeResourceToString(appointment));
		// patch should replace original patient id with the replacement patient id
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(newPatientPred));
		assertFalse(appointment.getParticipant()
			.stream().anyMatch(originalPatientPred));
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
}
