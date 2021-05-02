package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

	private void createSomeAppointmentWithType(String id, CodeableConcept type) {
		Appointment someAppointment = new Appointment();
		someAppointment.setId(id);
		someAppointment.setAppointmentType(type);
		myAppointmentDao.update(someAppointment);
	}

	@Test
	public void testListResourcesGraphqlArgumentConversion() {
		createSomeAppointmentWithType("hapi-0d0bfc88-ddc4-4b0f-9835-a8d32030135b", new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY")));

		Argument argument = new Argument("appointment_type", new StringValue("TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals("hapi-0d0bfc88-ddc4-4b0f-9835-a8d32030135b")));
	}

	@Test
	public void testListResourceGraphqlFilterArgument() {
		createSomeAppointmentWithType("hapi-6754ec7b-298b-4865-873c-879c55f8deeb", new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY")));

		Argument argument = new Argument("_filter", new StringValue("appointment-type eq TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals("hapi-6754ec7b-298b-4865-873c-879c55f8deeb")));
	}

	@Test
	public void testListResourceGraphqlInvalidException() {
		Argument argument = new Argument("test", new StringValue("some test value"));

		List<IBaseResource> result = new ArrayList<>();
		try {
			mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);
			fail("InvalidRequestException should be thrown.");
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("Unknown GraphQL argument \"test\"."));
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
	public void testSearch() {
		createSomePatientWithId("hapi-0f99592d-1a88-40ad-bf86-20911a53309d");

		List<Argument> arguments = Collections.emptyList();
		IBaseBundle bundle = mySvc.search(mySrd, "Patient", arguments);

		assertTrue(toUnqualifiedIdValues(bundle).stream().anyMatch("hapi-0f99592d-1a88-40ad-bf86-20911a53309d"::equals));
	}

	@Test
	public void testSearchNextPage() throws URISyntaxException {
		createSomePatientWithId("hapi-154df442-9020-487e-81b8-aae695857c4f");
		createSomePatientWithId("hapi-9fed8c68-5e53-4f3f-9839-8ea1352f7709");
		createSomePatientWithId("hapi-22d3037f-0e6f-4459-9377-c2c1bc2018bb");

		List<Argument> arguments = Collections.singletonList(new Argument("_count", new StringValue("1")));
		IBaseBundle bundle = mySvc.search(mySrd, "Patient", arguments);

		Optional<String> nextUrl = Optional.ofNullable(BundleUtil.getLinkUrlOfType(getContext(), bundle, "next"));
		assertTrue(nextUrl.isPresent());

		List<NameValuePair> params = URLEncodedUtils.parse(new URI(nextUrl.get()), StandardCharsets.UTF_8);
		Optional<String> cursorId = params.stream()
			.filter(it -> "search-id".equals(it.getName()))
			.map(NameValuePair::getValue)
			.findAny();
		Optional<String> cursorOffset = params.stream()
			.filter(it -> "search-offset".equals(it.getName()))
			.map(NameValuePair::getValue)
			.findAny();

		assertTrue(cursorId.isPresent());
		assertTrue(cursorOffset.isPresent());

		List<Argument> nextArguments = Arrays.asList(
			new Argument("search-id", new StringValue(cursorId.get())),
			new Argument("search-offset", new StringValue(cursorOffset.get()))
		);

		Optional<IBaseBundle> nextBundle = Optional.ofNullable(mySvc.search(mySrd, "Patient", nextArguments));
		assertTrue(nextBundle.isPresent());
	}

	@Test
	public void testSearchInvalidCursor() {
		try {
			List<Argument> arguments = Arrays.asList(
				new Argument("search-id", new StringValue("fc823e42-199c-4d7c-aa93-e27b75d47c45")),
				new Argument("search-offset", new StringValue("0"))
			);
			mySvc.search(mySrd, "Patient", arguments);
			fail("InvalidRequestException should be thrown.");
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("GraphQL Cursor \"fc823e42-199c-4d7c-aa93-e27b75d47c45\" does not exist and may have expired"));
		}
	}
}
