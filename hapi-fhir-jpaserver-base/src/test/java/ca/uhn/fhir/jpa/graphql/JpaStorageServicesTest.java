package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@ContextConfiguration(classes = {TestR4Config.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class JpaStorageServicesTest extends BaseJpaR4Test {

	@Autowired
	private IGraphQLStorageServices mySvc;

	@AfterEach
	public void after() {
		myDaoConfig.setFilterParameterEnabled(new DaoConfig().isFilterParameterEnabled());
	}

	@BeforeEach
	public void before() {
		myDaoConfig.setFilterParameterEnabled(true);
	}

	private String createSomeAppointment() {
		CodeableConcept someCodeableConcept = new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY"));
		Appointment someAppointment = new Appointment();
		someAppointment.setAppointmentType(someCodeableConcept);
		return myAppointmentDao.create(someAppointment).getId().getIdPart();
	}

	@Test
	public void testListResourcesGraphqlArgumentConversion() {
		String appointmentId = createSomeAppointment();

		Argument argument = new Argument("appointment_type", new StringValue("TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals(appointmentId)));
	}

	@Test
	public void testListResourceGraphqlFilterArgument() {
		String appointmentId = createSomeAppointment();

		Argument argument = new Argument("_filter", new StringValue("appointment-type eq TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals(appointmentId)));
	}

	@Test
	public void testListResourceGraphqlInvalidException() {
		Argument argument = new Argument("test", new StringValue("some test value"));

		List<IBaseResource> result = new ArrayList<>();
		try {
			mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unknown GraphQL argument \"test\". Value GraphQL argument for this type are: [_id, _language, actor, appointment_type, based_on, date, identifier, location, part_status, patient, practitioner, reason_code, reason_reference, service_category, service_type, slot, specialty, status, supporting_info]", e.getMessage());
		}
	}

	private void createSomePatientWithId(String id) {
		Patient somePatient = new Patient();
		somePatient.setId(id);
		myPatientDao.update(somePatient);
	}

	@Test
	public void testListResourceGraphqlArrayOfArgument() {
		createSomePatientWithId("hapi-123");
		createSomePatientWithId("hapi-124");

		Argument argument = new Argument();
		argument.setName("_id");
		argument.addValue(new StringValue("hapi-123"));
		argument.addValue(new StringValue("hapi-124"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Patient", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());

		List<String> expectedId = Arrays.asList("hapi-123", "hapi-124");
		assertTrue(result.stream().allMatch((it) -> expectedId.contains(it.getIdElement().getIdPart())));
	}
}
