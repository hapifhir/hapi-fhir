package ca.uhn.fhir.jpa.dao.r4.suites;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.FhirPathFilterInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface IPatchTests {

	org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IPatchTests.class);

	FhirContext getFhirContext();

	DaoRegistry getDaoRegistry();

	JpaStorageSettings getStorageSettings();

	@ParameterizedTest
	@ValueSource(strings = {
		"Appointment.participant.actor.reference.where(startsWith('Patient/')).first()",
		"Appointment.participant.actor.where(reference.startsWith('Patient/')).first()"
	})
	default void patch_primitiveWithPath_shouldWork(String theFhirPath) {
		// setup
		SystemRequestDetails rd = new SystemRequestDetails();
		IParser parser = getFhirContext().newJsonParser();
		DaoMethodOutcome outcome;

		String replacementText = "FIND_ME";
		String patient1Id = "Patient/p1";
		String patient2Id = "Patient/p2";
		String appointmentId = "a1";

		Patient patient1;
		Patient patient2;
		Location location;
		Appointment appointment;
		Parameters patch;
		{
			@Language("JSON")
			String patient1Str = """
				{
				  "resourceType" : "Patient",
				  "id" : "p1",
				  "active" : true,
				  "name" : [{
				    "use" : "official",
				    "family" : "Simpson",
				    "given" : ["Homer", "Jay"]
				  }],
				  "gender" : "male",
				  "birthDate" : "1974-12-25",
				  "deceasedBoolean" : false
				}
				""";
			patient1 = parser.parseResource(Patient.class, patient1Str);

			@Language("JSON")
			String patient2Str = """
    			{
    				"resourceType": "Patient",
    				"id": "p2",
    				"active": true,
    				"name": [{
    					"use": "official",
    					"family": "Bouvier",
    					"given": [ "Marge" ]
    				}],
    				"gender": "female",
    				"birthDate": "1974-12-25",
    				"deceasedBoolean": false
    			}
				""";
			patient2 = parser.parseResource(Patient.class, patient2Str);

			@Language("JSON")
			String locationStr = """
				{
				  "resourceType": "Location",
				  "id": "l1",
				  "status": "active",
				  "name": "South Wing, second floor"
				}
				""";
			location = parser.parseResource(Location.class, locationStr);

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
			patch = parser.parseResource(Parameters.class, patchStr);
		}

		// create the resources
		IFhirResourceDao<Patient> patientDao = getResourceDao(Patient.class);
		IFhirResourceDao<Location> locationDao = getResourceDao(Location.class);
		IFhirResourceDao<Appointment> appointmentDao = getResourceDao(Appointment.class);

		outcome = patientDao.update(patient1, rd);
		ourLog.info("Patient 1 id {}", outcome.getId().getValueAsString());
		outcome = patientDao.update(patient2, rd);
		ourLog.info("Patient 2 id {}", outcome.getId().getValueAsString());
		outcome = locationDao.update(location, rd);
		ourLog.info("Location id {}", outcome.getId().getValueAsString());
		outcome = appointmentDao.update(appointment, rd);
		ourLog.info("Appointment id {}", outcome.getId().getValueAsString());

		// verify get works
		{
			ourLog.info("GET for fhirpath {}", theFhirPath);
			SystemRequestDetails details = new SystemRequestDetails();
			Appointment result = appointmentDao.read(new IdType(appointmentId),
				details);
			assertNotNull(result);

			SystemRequestDetails newSrd = new SystemRequestDetails();
			newSrd.addParameter(Constants.PARAM_FHIRPATH,
				new String[] { "Appointment.participant.actor.reference.where(startsWith('Patient')).first()" });
			newSrd.setFhirContext(getFhirContext());
			ResponseDetails responseDetails = new ResponseDetails();
			responseDetails.setResponseResource(result);
			FhirPathFilterInterceptor interceptor = new FhirPathFilterInterceptor();
			interceptor.preProcessOutgoingResponse(newSrd, responseDetails);
			assertNotNull(responseDetails.getResponseResource());

			ourLog.info(parser.encodeResourceToString(responseDetails.getResponseResource()));
			assertTrue(responseDetails.getResponseResource() instanceof Parameters);
			Parameters readParameters = (Parameters) responseDetails.getResponseResource();
			assertEquals(1, readParameters.getParameter().size());
			assertTrue(readParameters.getParameter().get(0)
				.getPart().stream().anyMatch(p -> {
					if (p.getName().equals("result")) {
						if (p.getValue().isPrimitive()) {
							return p.getValue().toString().equals(patient1Id);
						} else if (p.getValue() instanceof Reference r) {
							return r.getReference().equals(patient1Id);
						} else {
							ourLog.info("Fhirtype {}", p.getValue().fhirType());
						}
					}
					return false;
				}));
		}

		// test patch
		ourLog.info("Test PATCH");
		outcome = appointmentDao.patch(
			new IdType(appointmentId),
			null,
			PatchTypeEnum.FHIR_PATCH_JSON,
			null,
			patch,
			rd);

		// verify successful patch
		assertTrue(outcome.getOperationOutcome() instanceof OperationOutcome);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals(1, oo.getIssue().size());
		assertTrue(oo.getIssue().stream()
			.anyMatch(p -> p.getDiagnostics().contains("Successfully patched resource")));
		Appointment patchedAppointment = (Appointment) outcome.getResource();

		/*
		 * Verify Patient/p1 is replaced with Patient/p2
		 */
		assertTrue(patchedAppointment.getParticipant().stream()
			.anyMatch(p -> p.getActor().getReference() != null && p.getActor().getReference().equals(patient2Id)));
		assertFalse(patchedAppointment.getParticipant()
			.stream().anyMatch(p -> p.getActor().getReference() != null && p.getActor().getReference().equals(patient1Id)));
	}

	private <R extends IBaseResource> IFhirResourceDao<R> getResourceDao(Class<R> theClazz) {
		return getDaoRegistry().getResourceDao(theClazz);
	}
}
