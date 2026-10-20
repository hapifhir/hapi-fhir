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

public class GraphQLR4RawTest {

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
	public void testGraphInstance_Get() {
		ourNextRetVal = "{\"foo\"}";


		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql")
				.withQueryParam("query", "{name{family,given}}")
				.get()
				.assertStatus(200)
				.assertBodyEquals("{\"foo\"}");
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith(Constants.CT_JSON);
		assertThat(ourLastId.getValue()).isEqualTo("Patient/123");
		assertThat(ourLastQuery).isEqualTo("{name{family,given}}");

	}

	@Test
	public void testGraphInstance_Get_UnsupportedResourceType() {
		ourNextRetVal = "{\"foo\"}";


		myRestfulServerExtension.fhirRequest("/Condition/123/$graphql")
				.withQueryParam("query", "{name{family,given}}")
				.get()
				.assertStatus(404)
				.assertBodyContains("Unknown resource type");

	}

	@Test
	public void testGraphInstance_Post_ContentTypeJson() {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON)
				.post("{\"query\": \"{name{family,given}}\"}".getBytes(StandardCharsets.UTF_8), Constants.CT_JSON)
				.assertStatus(200)
				.assertBodyEquals("{\"foo\"}");
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith(Constants.CT_JSON);
		assertThat(ourLastId.getValue()).isEqualTo("Patient/123");
		assertThat(ourLastQuery).isEqualTo("{name{family,given}}");

	}

	@Test
	public void testGraphInstance_Post_ContentTypeGraphql() {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/Patient/123/$graphql")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON)
				.post("{name{family,given}}".getBytes(StandardCharsets.UTF_8), "application/graphql")
				.assertStatus(200)
				.assertBodyEquals("{\"foo\"}");
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith(Constants.CT_JSON);
		assertThat(ourLastId.getValue()).isEqualTo("Patient/123");
		assertThat(ourLastQuery).isEqualTo("{name{family,given}}");
		assertThat(ourLastResourceType).isEqualTo("Patient");

	}

	@Test
	public void testGraphBase_Post_ListQuery() {
		ourNextRetVal = "{\"foo\"}";

		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/$graphql")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_JSON)
				.post("{\"query\": \"{PatientList(date: \\\"2022\\\") {name{family,given}}}\"}".getBytes(StandardCharsets.UTF_8), Constants.CT_JSON)
				.assertStatus(200)
				.assertBodyEquals("{\"foo\"}");
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith(Constants.CT_JSON);
		assertThat(ourLastId).isNull();
		assertThat(ourLastResourceType).isNull();
		assertThat(ourLastQuery).isEqualTo("{PatientList(date: \"2022\") {name{family,given}}}");

	}


	@Test
	public void testGraphSystem() {
		ourNextRetVal = "{\"foo\"}";


		HttpTestResponse status = myRestfulServerExtension.fhirRequest("/$graphql")
				.withQueryParam("query", "{name{family,given}}")
				.get()
				.assertStatus(200)
				.assertBodyEquals("{\"foo\"}");
		assertThat(status.getHeader(Constants.HEADER_CONTENT_TYPE)).startsWith(Constants.CT_JSON);
		assertThat(ourLastId).isNull();
		assertThat(ourLastQuery).isEqualTo("{name{family,given}}");

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
