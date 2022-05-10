package ca.uhn.fhir.jpa.graphql;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ca.uhn.fhir.jpa.graphql.DaoRegistryGraphQLStorageServices.SEARCH_ID_PARAM;
import static ca.uhn.fhir.jpa.graphql.DaoRegistryGraphQLStorageServices.SEARCH_OFFSET_PARAM;
import static org.junit.jupiter.api.Assertions.*;

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

	private void createSomeAppointmentWithType(String id, CodeableConcept type) {
		Appointment someAppointment = new Appointment();
		someAppointment.setId(id);
		someAppointment.setAppointmentType(type);
		myAppointmentDao.update(someAppointment);
	}

	@Test
	public void testListResourcesGraphqlArgumentConversion() {
		createSomeAppointmentWithType("hapi-1", new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY")));

		Argument argument = new Argument("appointment_type", new StringValue("TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals("hapi-1")));
	}

	@Test
	public void testListResourceGraphqlFilterArgument() {
		createSomeAppointmentWithType("hapi-1", new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY")));

		Argument argument = new Argument("_filter", new StringValue("appointment-type eq TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals("hapi-1")));
	}

	@Test
	public void testListResourceGraphqlTokenArgumentWithSystem() {
		createSomeAppointmentWithType("hapi-1", new CodeableConcept(new Coding("TEST_SYSTEM", "TEST_CODE", "TEST_DISPLAY")));;

		Argument argument = new Argument("appointment_type", new StringValue("TEST_SYSTEM|TEST_CODE"));

		List<IBaseResource> result = new ArrayList<>();
		mySvc.listResources(mySrd, "Appointment", Collections.singletonList(argument), result);

		assertFalse(result.isEmpty());
		assertTrue(result.stream().anyMatch((it) -> it.getIdElement().getIdPart().equals("hapi-1")));
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

		List<String> expectedId = Arrays.asList("hapi-1", "hapi-2", "hapi-0", "hapi-3", "hapi-4");
		assertTrue(result.stream().allMatch((it) -> expectedId.contains(it.getIdElement().getIdPart())));

		//_offset=5
		List<IBaseResource> result2 = new ArrayList<>();
		Map<String, String[]> parametersMap = new HashMap<>();
		parametersMap.put("_offset", new String[]{"5"});
		when(mySrd.getParameters()).thenReturn(parametersMap);
		mySvc.listResources(mySrd, "Patient", Collections.singletonList(argument), result2);

		assertFalse(result2.isEmpty());
		assertEquals(5, result2.size());

		List<String> expectedId2 = Arrays.asList("hapi-5", "hapi-6", "hapi-7", "hapi-8", "hapi-9");
		assertTrue(result2.stream().allMatch((it) -> expectedId2.contains(it.getIdElement().getIdPart())));
	}

	@Test
	public void testSearch() {
		createSomePatientWithId("hapi-1");

		List<Argument> arguments = Collections.emptyList();
		IBaseBundle bundle = mySvc.search(mySrd, "Patient", arguments);

		List<String> result = toUnqualifiedVersionlessIdValues(bundle);
		assertEquals(1, result.size());
		assertEquals("Patient/hapi-1", result.get(0));
	}

	@Test
	public void testSearchNextPage() throws URISyntaxException {
		createSomePatientWithId("hapi-1");
		createSomePatientWithId("hapi-2");
		createSomePatientWithId("hapi-3");

		List<Argument> arguments = Collections.singletonList(new Argument("_count", new StringValue("1")));
		IBaseBundle bundle = mySvc.search(mySrd, "Patient", arguments);

		Optional<String> nextUrl = Optional.ofNullable(BundleUtil.getLinkUrlOfType(myFhirContext, bundle, "next"));
		assertTrue(nextUrl.isPresent());

		List<NameValuePair> params = URLEncodedUtils.parse(new URI(nextUrl.get()), StandardCharsets.UTF_8);
		Optional<String> cursorId = params.stream()
			.filter(it -> SEARCH_ID_PARAM.equals(it.getName()))
			.map(NameValuePair::getValue)
			.findAny();
		Optional<String> cursorOffset = params.stream()
			.filter(it -> SEARCH_OFFSET_PARAM.equals(it.getName()))
			.map(NameValuePair::getValue)
			.findAny();

		assertTrue(cursorId.isPresent());
		assertTrue(cursorOffset.isPresent());

		List<Argument> nextArguments = Arrays.asList(
			new Argument(SEARCH_ID_PARAM, new StringValue(cursorId.get())),
			new Argument(SEARCH_OFFSET_PARAM, new StringValue(cursorOffset.get()))
		);

		Optional<IBaseBundle> nextBundle = Optional.ofNullable(mySvc.search(mySrd, "Patient", nextArguments));
		assertTrue(nextBundle.isPresent());
	}

	@Test
	public void testSearchInvalidCursor() {
		try {
			List<Argument> arguments = Arrays.asList(
				new Argument(SEARCH_ID_PARAM, new StringValue("invalid-search-id")),
				new Argument(SEARCH_OFFSET_PARAM, new StringValue("0"))
			);
			mySvc.search(mySrd, "Patient", arguments);
			fail("InvalidRequestException should be thrown.");
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("GraphQL Cursor \"invalid-search-id\" does not exist and may have expired"));
		}
	}
}
