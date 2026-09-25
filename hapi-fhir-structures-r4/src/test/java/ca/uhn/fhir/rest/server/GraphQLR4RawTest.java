package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GraphQLR4RawTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GraphQLR4RawTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static String ourNextRetVal;
	private static IdType ourLastId;
	private static String ourLastQuery;
	private static String ourLastResourceType;

	@RegisterExtension
	private final RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx)
		.registerProvider(new MyPatientResourceProvider())
		.registerProvider(new MyGraphQLProvider());

	@BeforeEach
	public void before() {
		ourNextRetVal = null;
		ourLastId = null;
		ourLastQuery = null;
		ourLastResourceType = null;
	}

	@Test
	public void testGraphInstance_Get() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}")).get().assertStatus(200);
		String responseContent = status.getBody();
		ourLog.info(responseContent);

		assertEquals("{\"foo\"}", responseContent);
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith("application/json");
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals("{name{family,given}}", ourLastQuery);

	}

	@Test
	public void testGraphInstance_Get_UnsupportedResourceType() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Condition/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}")).get().assertStatus(404);
		String responseContent = status.getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("Unknown resource type");

	}

	@Test
	public void testGraphInstance_Post_ContentTypeJson() throws Exception {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql")
			.withHeader("Accept", "application/json")
			.post("{\"query\": \"{name{family,given}}\"}".getBytes(StandardCharsets.UTF_8), "application/json")
			.assertStatus(200);
		String responseContent = status.getBody();
		ourLog.info(responseContent);

		assertEquals("{\"foo\"}", responseContent);
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith("application/json");
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals("{name{family,given}}", ourLastQuery);

	}

	@Test
	public void testGraphInstance_Post_ContentTypeGraphql() throws Exception {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql")
			.withHeader("Accept", "application/json")
			.post("{name{family,given}}".getBytes(StandardCharsets.UTF_8), "application/graphql")
			.assertStatus(200);
		String responseContent = status.getBody();
		ourLog.info(responseContent);

		assertEquals("{\"foo\"}", responseContent);
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith("application/json");
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals("{name{family,given}}", ourLastQuery);
		assertEquals("Patient", ourLastResourceType);

	}

	@Test
	public void testGraphBase_Post_ListQuery() throws Exception {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/$graphql")
			.withHeader("Accept", "application/json")
			.post("{\"query\": \"{PatientList(date: \\\"2022\\\") {name{family,given}}}\"}".getBytes(StandardCharsets.UTF_8), "application/json")
			.assertStatus(200);
		String responseContent = status.getBody();
		ourLog.info(responseContent);

		assertEquals("{\"foo\"}", responseContent);
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith("application/json");
		assertNull(ourLastId);
		assertNull(ourLastResourceType);
		assertEquals("{PatientList(date: \"2022\") {name{family,given}}}", ourLastQuery);

	}


	@Test
	public void testGraphSystem() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}")).get().assertStatus(200);
		String responseContent = status.getBody();
		ourLog.info(responseContent);

		assertEquals("{\"foo\"}", responseContent);
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith("application/json");
		assertNull(ourLastId);
		assertEquals("{name{family,given}}", ourLastQuery);

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class MyGraphQLProvider {

		@GraphQL(type = RequestTypeEnum.GET)
		public String processGet(@IdParam IdType theId, @GraphQLQueryUrl String theQuery) {
			ourLastId = theId;
			ourLastQuery = theQuery;
			return ourNextRetVal;
		}

		@GraphQL(type = RequestTypeEnum.POST)
		public String processPost(RequestDetails theRequestDetails, @IdParam IdType theId, @GraphQLQueryBody String theQuery) {
			ourLastId = theId;
			ourLastResourceType = theRequestDetails.getResourceName();
			ourLastQuery = theQuery;
			return ourNextRetVal;
		}

	}

	public static class MyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			@OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.getIdElement().setValue("Patient/" + i);
				retVal.add(patient);
			}
			return retVal;
		}

	}


}
