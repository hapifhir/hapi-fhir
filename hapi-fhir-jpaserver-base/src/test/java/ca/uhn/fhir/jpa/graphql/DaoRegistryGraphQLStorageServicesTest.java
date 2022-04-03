package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestR4Config.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class DaoRegistryGraphQLStorageServicesTest extends BaseJpaR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

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
	public void testListResourceGraphqlTokenArgumentWithSystem() {
		String appointmentId = createSomeAppointment();

		Argument argument = new Argument("appointment_type", new StringValue("TEST_SYSTEM|TEST_CODE"));

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
			assertEquals(Msg.code(1275) + "Unknown GraphQL argument \"test\". Value GraphQL argument for this type are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, actor, appointment_type, based_on, date, identifier, location, part_status, patient, practitioner, reason_code, reason_reference, service_category, service_type, slot, specialty, status, supporting_info]", e.getMessage());
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

	@Test
	public void testListResourceGraphqlWithPageSizeSmallerThanResultSize() {
		for (int i = 0; i < 10; i++) {
			createSomePatientWithId("hapi-" + i);
		}

		Argument argument = new Argument();
		argument.setName("_id");
		for (int i = 0; i < 10; i++) {
			argument.addValue(new StringValue("hapi-" + i));
		}

		//fisrt page
		List<IBaseResource> result = new ArrayList<>();
		when(mySrd.getServer().getDefaultPageSize()).thenReturn(5);
		mySvc.listResources(mySrd, "Patient", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertEquals(5, result.size());

		List<String> expectedId = Arrays.asList("hapi-1", "hapi-2",  "hapi-0", "hapi-3", "hapi-4");
		assertTrue(result.stream().allMatch((it) -> expectedId.contains(it.getIdElement().getIdPart())));

		//_offset=5
		List<IBaseResource> result2 = new ArrayList<>();
		Map<String, String[]> parametersMap = new HashMap<>();
		parametersMap.put("_offset", new String[]{"5"});
		when(mySrd.getParameters()).thenReturn(parametersMap);
		mySvc.listResources(mySrd, "Patient", Collections.singletonList(argument), result2);

		assertFalse(result2.isEmpty());
		assertEquals(5, result2.size());

		List<String> expectedId2 = Arrays.asList("hapi-5", "hapi-6",  "hapi-7", "hapi-8", "hapi-9");
		assertTrue(result2.stream().allMatch((it) -> expectedId2.contains(it.getIdElement().getIdPart())));
	}
}
