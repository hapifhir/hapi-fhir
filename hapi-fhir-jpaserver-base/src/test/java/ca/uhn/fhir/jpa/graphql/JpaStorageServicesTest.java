package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.hl7.fhir.utilities.graphql.StringValue;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ContextConfiguration(classes = {TestR4Config.class})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class JpaStorageServicesTest extends BaseJpaR4Test {

	@After
	public void after() {
		myDaoConfig.setFilterParameterEnabled(new DaoConfig().isFilterParameterEnabled());
	}

	@Before
	public void before() {
		myDaoConfig.setFilterParameterEnabled(true);
	}

	@Autowired
	private IGraphQLStorageServices mySvc;

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

		Assert.assertFalse(result.isEmpty());
		Assert.assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals(appointmentId)));
	}

	@Test
	public void testListResourceGraphqlFilterArgument() {
		String appointmentId = createSomeAppointment();

		Argument argument = new Argument("_filter", new StringValue("appointment-type eq TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		Assert.assertFalse(result.isEmpty());
		Assert.assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals(appointmentId)));
	}

	@Test(expected = InvalidRequestException.class)
	public void testListResourceGraphqlInvalidException() {
		Argument argument = new Argument("test", new StringValue("some test value"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);
	}
}
