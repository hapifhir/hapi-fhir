package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchR5Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR5();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private static int ourPort;

	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}


	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
//			validate(ourCtx.newJsonParser().parseResource(responseContent));
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);

			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		}

	}


	private void validate(IBaseResource theResource) {
		FhirValidator validatorModule = ourCtx.newValidator();
		ValidationResult result = validatorModule.validateWithResult(theResource);
		if (!result.isSuccessful()) {
			fail(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		}
	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			@RequiredParam(name = "identifier") TokenAndListParam theIdentifiers) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.getIdElement().setValue("Patient/" + i + "/_history/222");
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE.getCode());
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.setActive(true);
				retVal.add(patient);
			}
			return retVal;
		}

	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.setResourceProviders(patientProvider);
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

}
