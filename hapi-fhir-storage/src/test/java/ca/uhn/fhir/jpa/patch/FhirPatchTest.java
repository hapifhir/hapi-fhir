package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
	 * This means:
	 * * matches for Patient/p1 and replaces with Patient/p2
	 * * only matches first patient
	 */
	static List<Parameters> patchParams() {
		List<Parameters> params = new ArrayList<>();

		// 1
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.reference.where(startsWith('Patient/')).first()"));
			comp.addPart().setName("value").setValue(new StringType("Patient/p2"));
			params.add(parameters);
		}

		// 2
		{
			Parameters parameters = new Parameters();
			Parameters.ParametersParameterComponent comp = parameters.addParameter()
				.setName("operation");

			comp.addPart().setName("type").setValue(new CodeType("replace"));
			comp.addPart().setName("path").setValue(new StringType("Appointment.participant.actor.where(reference.startsWith('Patient/')).first()"));
			comp.addPart().setName("value").setValue(new Reference("Patient/p2"));
			params.add(parameters);
		}

		return params;
	}

	@ParameterizedTest
	@MethodSource("patchParams")
	public void patchApply_withValidParams_works(Parameters thePatch) {
		// setup
		String originalPatientId = "Patient/p1";
		String replacedPatientId = "Patient/p2";

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
			return p.getActor() != null && p.getActor().getReference().equals(replacedPatientId);
		};

		// precheck
		// should be no replacement, but should have the original
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(originalPatientPred));
		assertFalse(appointment.getParticipant()
			.stream().anyMatch(newPatientPred));

		// test
		myPatch.apply(appointment, thePatch);

		// verify
		ourLog.trace(myParser.encodeResourceToString(appointment));
		// patch should replace original patient id with the replacement patient id
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(newPatientPred));
		assertFalse(appointment.getParticipant()
			.stream().anyMatch(originalPatientPred));

		// but not the location
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(p -> {
				return p.getActor() != null && p.getActor().getReference().equals("Location/l1");
			}));
		// or subsequent patient resources (because these use .first())
		assertTrue(appointment.getParticipant()
			.stream().anyMatch(p -> {
				return p.getActor() != null && p.getActor().getReference().equals("Patient/p3");
			}));
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
}
