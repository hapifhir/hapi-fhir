package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.PatientProfileDstu2;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchReturningProfiledResourceDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchReturningProfiledResourceDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testClientTypedRequest() throws Exception {
		ourCtx = FhirContext.forDstu2();
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		Bundle bundle = client.search().forResource(PatientProfileDstu2.class).returnBundle(Bundle.class).execute();

		assertEquals(PatientProfileDstu2.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testClientUntypedRequestWithHint() throws Exception {
		ourCtx = FhirContext.forDstu2();
		ourCtx.setDefaultTypeForProfile("http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient", PatientProfileDstu2.class);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		assertEquals(PatientProfileDstu2.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testClientUntypedRequestWithoutHint() throws Exception {
		ourCtx = FhirContext.forDstu2();
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		assertEquals(Patient.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testProfilesGetAdded() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent, not(containsString("html")));
		assertThat(responseContent, containsString("<profile value=\"http://foo\"/>"));
		assertThat(responseContent, containsString("<profile value=\"http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient\"/>"));

	}

	@Test
	public void testProfilesGetAddedHtml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=html");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent, containsString("html"));
		assertThat(responseContent, containsString("http://foo"));
		assertThat(responseContent, containsString("http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient"));

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.registerInterceptor(new ResponseHighlighterInterceptor());
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setResourceProviders(new DummyPatientResourceProvider());

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

		/**
		 * Basic search, available for every resource. Allows search per id (use of read is better, though), fulltext
		 * content
		 * and lastUpdated timestamp.
		 * 
		 * @param id
		 * @param content
		 * @param lastUpdated
		 * @return
		 */
		//@formatter:off
		@Search
		public List<Patient> search(
				@Description(shortDefinition = "The resource id") 
				@OptionalParam(name = "_id") StringParam id, 
				@Description(shortDefinition = "Search the contents of the resource's data using a fulltext search") 
				@OptionalParam(name = Constants.PARAM_CONTENT) StringParam content,
				@Description(shortDefinition = "Search for resources considering the time of their last update") 
				@OptionalParam(name = Constants.PARAM_LASTUPDATED) DateRangeParam lastUpdated) {
			//@formatter:on

			List<Patient> result = new ArrayList<Patient>();

			PatientProfileDstu2 pp = new PatientProfileDstu2();

			ResourceMetadataKeyEnum.PROFILES.put(pp, Collections.singletonList(new IdDt("http://foo")));

			pp.setId("123");
			pp.getOwningOrganization().setReference("Organization/456");
			result.add(pp);

			ourLog.info("Search: Everything ok. Going to return results!");
			return result;
		}

	}

}
