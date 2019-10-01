package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.utilities.graphql.Argument;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JpaGraphQLR4ProviderTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaGraphQLR4ProviderTest.class);
	public static final String DATA_PREFIX = "{\"data\": ";
	public static final String DATA_SUFFIX = "}";
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;

	@Before
	public void before() {
		//nothing
	}

	@Test
	public void testGraphInstance() throws Exception {
		String query = "{name{family,given}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
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

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testGraphInstanceWithFhirpath() throws Exception {
		String query = "{name(fhirpath:\"family.exists()\"){text,given,family}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
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

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testGraphSystemInstance() throws Exception {
		String query = "{Patient(id:123){id,name{given,family}}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
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

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testGraphSystemList() throws Exception {
		String query = "{PatientList(name:\"pet\"){name{family,given}}}";
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
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
				"      \"given\":[\"GivenOnlyB1\",\"GivenOnlyB2\"]\n" +
				"    }]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(responseContent));
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.registerProvider(new DummyPatientResourceProvider());
		MyStorageServices storageServices = new MyStorageServices();
		servlet.registerProvider(new GraphQLProvider(storageServices));
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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
				retVal.add((Patient) patient);
			}
			return retVal;
		}

	}

	private static class MyStorageServices implements IGraphQLStorageServices {
		@Override
		public void listResources(Object theAppInfo, String theType, List<Argument> theSearchParams, List<IBaseResource> theMatches) throws FHIRException {
			ourLog.info("listResources of {} - {}", theType, theSearchParams);

			if (theSearchParams.size() == 1) {
				String name = theSearchParams.get(0).getName();
				if ("name".equals(name)) {
					Patient p = new Patient();
					p.addName()
						.setFamily(theSearchParams.get(0).getValues().get(0).toString())
						.addGiven("GIVEN1")
						.addGiven("GIVEN2");
					p.addName()
						.addGiven("GivenOnly1")
						.addGiven("GivenOnly2");
					theMatches.add(p);

					p = new Patient();
					p.addName()
						.addGiven("GivenOnlyB1")
						.addGiven("GivenOnlyB2");
					theMatches.add(p);

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
