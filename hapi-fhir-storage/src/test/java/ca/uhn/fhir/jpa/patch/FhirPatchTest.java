package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Parameters;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

	@ParameterizedTest
	@ValueSource(strings = {
		"Appointment.participant.actor.reference.where(startsWith('Patient/')).first()",
		"Appointment.participant.actor.where(reference.startsWith('Patient/')).first()"
	})
	public void patchApply_withPrimitiveTarget_shouldWork(String theFhirPath) {
		// setup
		IParser parser = myFhirContext.newJsonParser();
		String originalPatientId = "Patient/p1";
		String replacedPatientId = "Patient/p2";
		String replacementText = "FIND_ME";

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
			appointment = parser.parseResource(Appointment.class, appointmentStr);

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
						  "valueString": "FIND_ME"
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
			patchStr = patchStr.replace(replacementText, theFhirPath);
			parameters = parser.parseResource(Parameters.class, patchStr);
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
