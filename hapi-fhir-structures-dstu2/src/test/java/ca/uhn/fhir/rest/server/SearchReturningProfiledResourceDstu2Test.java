package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.PatientProfileDstu2;

public class SearchReturningProfiledResourceDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static String ourLastMethod;
	private static StringParam ourLastRef;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchReturningProfiledResourceDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastRef = null;
	}

	@Test
	public void testProfilesGetAdded() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		
		assertThat(responseContent, containsString("<profile value=\"http://foo\"/>"));
		assertThat(responseContent, containsString("<profile value=\"http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient\"/>"));
		
	}


	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(new DummyPatientResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

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

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
