package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.resource.Observation;
import ca.uhn.fhir.model.dev.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.PortUtil;

import com.google.common.net.UrlEscapers;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class BundleTypeInResponseTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = new FhirContext();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleTypeInResponseTest.class);
	private static int ourPort;

	private static Server ourServer;

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());
		assertEquals(BundleTypeEnum.SEARCHSET, bundle.getType().getValueAsEnum());
	}


	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		servlet.setResourceProviders(patientProvider);
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

		@Search
		public List<Patient> findPatient() {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier().setSystem("system").setValue("identifier123");
			retVal.add(patient);
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
