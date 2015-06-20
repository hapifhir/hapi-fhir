package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ValidateDstu1Test {
	private static CloseableHttpClient ourClient;
	private static EncodingEnum ourLastEncoding;
	private static String ourLastResourceBody;
	private static int ourPort;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static Server ourServer;

	@Before()
	public void before() {
		ourLastResourceBody = null;
		ourLastEncoding = null;
	}

	@Test
	public void testValidate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());

		assertThat(ourLastResourceBody, stringContainsInOrder("<Patient ", "<identifier>", "<value value=\"001"));
		assertEquals(EncodingEnum.XML, ourLastEncoding);

	}

	@Test
	public void testValidateWithNoParsed() throws Exception {

		Organization org = new Organization();
		org.addIdentifier().setValue("001");
		org.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Organization/_validate");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(org), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		assertEquals(204, status.getStatusLine().getStatusCode());

		assertThat(ourLastResourceBody, stringContainsInOrder("\"resourceType\":\"Organization\"", "\"identifier\"", "\"value\":\"001"));
		assertEquals(EncodingEnum.JSON, ourLastEncoding);

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider, new OrganizationProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class PatientProvider implements IResourceProvider {

		@Validate()
		public MethodOutcome validatePatient(@ResourceParam Patient thePatient, @ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			IdDt id = new IdDt(thePatient.getIdentifier().get(0).getValue().getValue());
			if (thePatient.getId().isEmpty() == false) {
				id = thePatient.getId();
			}

			ourLastResourceBody = theResourceBody;
			ourLastEncoding = theEncoding;

			return new MethodOutcome(id.withVersion("002"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

	public static class OrganizationProvider implements IResourceProvider {

		@Validate()
		public MethodOutcome validate(@ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			ourLastResourceBody = theResourceBody;
			ourLastEncoding = theEncoding;

			return new MethodOutcome(new IdDt("001"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Organization.class;
		}

	}

}
