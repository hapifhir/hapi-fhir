package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphQLR4ProviderTest {

	public static final String DATA_PREFIX = "{\"data\": ";
	public static final String DATA_SUFFIX = "}";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GraphQLR4ProviderTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static CloseableHttpClient ourClient;
	private MyStorageServices myGraphQLStorageServices = new MyStorageServices();

	@RegisterExtension
	private RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new GraphQLProvider(myGraphQLStorageServices));

	@BeforeEach
	public void before() {
		//nothing
	}

	@Test
	public void testGraphInstance() throws Exception {
		String query = "{name{family,given}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + myRestfulServerExtension.getPort() + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + "{\n" +
				"  \"name\":[{\n" +
				"    \"family\":\"FAMILY\",\n" +
				"    \"given\":[\"GIVEN1\",\"GIVEN2\"]\n" +
				"  },{\n" +
				"    \"given\":[\"GivenOnly1\",\"GivenOnly2\"]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
		}

	}

	@Test
	public void testGraphInstanceWithFhirpath() throws Exception {
		String query = "{name(fhirpath:\"family.exists()\"){text,given,family}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + myRestfulServerExtension.getPort() + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + "{\n" +
				"  \"name\":[{\n" +
				"    \"given\":[\"GIVEN1\",\"GIVEN2\"],\n" +
				"    \"family\":\"FAMILY\"\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
		}

	}

	@Test
	public void testGraphSystemInstance() throws Exception {
		String query = "{Patient(id:123){id,name{given,family}}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + myRestfulServerExtension.getPort() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + "{\n" +
				"  \"Patient\":{\n" +
				"    \"name\":[{\n" +
				"      \"given\":[\"GIVEN1\",\"GIVEN2\"],\n" +
				"      \"family\":\"FAMILY\"\n" +
				"    },{\n" +
				"      \"given\":[\"GivenOnly1\",\"GivenOnly2\"]\n" +
				"    }]\n" +
				"  }\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
		}

	}

	@Test
	public void testGraphSystemList() throws Exception {
		String query = "{PatientList(name:\"pet\"){name{family,given}}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + myRestfulServerExtension.getPort() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + "{\n" +
				"  \"PatientList\":[{\n" +
				"    \"name\":[{\n" +
				"      \"family\":\"pet\",\n" +
				"      \"given\":[\"GIVEN1\",\"GIVEN2\"]\n" +
				"    },{\n" +
				"      \"given\":[\"GivenOnly1\",\"GivenOnly2\"]\n" +
				"    }]\n" +
				"  },{\n" +
				"    \"name\":[{\n" +
				"      \"given\":[\"pet\",\"GivenOnlyB1\",\"GivenOnlyB2\"]\n" +
				"    }]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));

		}

	}

	@Test
	public void testGraphSystemArrayArgumentList() throws Exception {
		String query = "{PatientList(id:[\"hapi-123\",\"hapi-124\"]){id,name{family}}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + myRestfulServerExtension.getPort() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + "{\n" +
				"  \"PatientList\":[{\n" +
				"    \"id\":\"Patient/hapi-123/_history/2\",\n" +
				"    \"name\":[{\n" +
				"      \"family\":\"FAMILY 123\"\n" +
				"    }]\n" +
				"  },{\n" +
				"    \"id\":\"Patient/hapi-124/_history/1\",\n" +
				"    \"name\":[{\n" +
				"      \"family\":\"FAMILY 124\"\n" +
				"    }]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		ourClient.close();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

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

	private static class MyStorageServices implements IGraphQLStorageServices {
		@Override
		public void listResources(Object theAppInfo, String theType, List<Argument> theSearchParams, List<IBaseResource> theMatches) throws FHIRException {
			ourLog.info("listResources of {} - {}", theType, theSearchParams);

			if (theSearchParams.size() == 1) {
				Argument argument = theSearchParams.get(0);

				String name = argument.getName();
				List<String> value = argument.getValues().stream()
					.map((it) -> it.getValue())
					.collect(Collectors.toList());

				if ("name".equals(name) && "pet".equals(value.get(0))) {
					Patient patient1 = new Patient();
					patient1.addName()
						.setFamily("pet")
						.addGiven("GIVEN1")
						.addGiven("GIVEN2");
					patient1.addName()
						.addGiven("GivenOnly1")
						.addGiven("GivenOnly2");

					Patient patient2 = new Patient();
					patient2.addName()
						.addGiven("pet")
						.addGiven("GivenOnlyB1")
						.addGiven("GivenOnlyB2");

					theMatches.add(patient1);
					theMatches.add(patient2);
				}

				if ("id".equals(name) && Arrays.asList("hapi-123", "hapi-124").containsAll(value)) {
					Patient patient1 = new Patient();
					patient1.setId("Patient/hapi-123/_history/2");
					patient1.addName()
						.setFamily("FAMILY 123");

					Patient patient2 = new Patient();
					patient2.setId("Patient/hapi-124/_history/1");
					patient2.addName()
						.setFamily("FAMILY 124");

					theMatches.add(patient1);
					theMatches.add(patient2);
				}
			}
		}

		@Override
		public Resource lookup(Object theAppInfo, String theType, String theId) throws FHIRException {
			ourLog.info("lookup {}/{}", theType, theId);

			if (theType.equals("Patient") && theId.equals("123")) {
				Patient p = new Patient();
				p.addName()
					.setFamily("FAMILY")
					.addGiven("GIVEN1")
					.addGiven("GIVEN2");
				p.addName()
					.addGiven("GivenOnly1")
					.addGiven("GivenOnly2");
				return p;
			}

			return null;
		}

		@Override
		public IGraphQLStorageServices.ReferenceResolution lookup(Object theAppInfo, IBaseResource theContext, IBaseReference theReference) throws FHIRException {
			ourLog.info("lookup from {} to {}", theContext.getIdElement().getValue(), theReference.getReferenceElement().getValue());
			return null;
		}

		@Override
		public Bundle search(Object theAppInfo, String theType, List<Argument> theSearchParams) throws FHIRException {
			ourLog.info("search on {} - {}", theType, theSearchParams);
			return null;
		}
	}
}
