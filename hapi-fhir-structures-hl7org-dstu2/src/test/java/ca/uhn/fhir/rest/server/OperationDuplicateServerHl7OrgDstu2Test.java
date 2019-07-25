package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
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
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.OperationDefinition;
import org.hl7.fhir.instance.model.Organization;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.util.PortUtil;

public class OperationDuplicateServerHl7OrgDstu2Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationDuplicateServerHl7OrgDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testOperationsAreCollapsed() throws Exception {
		// Metadata
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
			HttpResponse status = ourClient.execute(httpGet);

			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(response);

			Conformance resp = ourCtx.newXmlParser().parseResource(Conformance.class, response);
			assertEquals(1, resp.getRest().get(0).getOperation().size());
			assertEquals("$myoperation", resp.getRest().get(0).getOperation().get(0).getName());
			assertEquals("OperationDefinition/myoperation", resp.getRest().get(0).getOperation().get(0).getDefinition().getReference());
		}

		// OperationDefinition
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/OperationDefinition/myoperation?_pretty=true");
			HttpResponse status = ourClient.execute(httpGet);

			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(response);

			OperationDefinition resp = ourCtx.newXmlParser().parseResource(OperationDefinition.class, response);
			assertEquals(true, resp.getSystemElement().getValue().booleanValue());
			assertEquals("$myoperation", resp.getCode());
			assertEquals(true, resp.getIdempotent());
			assertEquals(2, resp.getType().size());
			assertThat(Arrays.asList(resp.getType().get(0).getValue(), resp.getType().get(1).getValue()), containsInAnyOrder("Organization", "Patient"));
			assertEquals(1, resp.getParameter().size());
		}
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forDstu2Hl7Org();
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2));

		servlet.setFhirContext(ourCtx);
		servlet.setResourceProviders(new PatientProvider(), new OrganizationProvider());
		servlet.setPlainProviders(new PlainProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class BaseProvider {

		@Operation(name = "$myoperation", idempotent = true)
		public Parameters opInstanceReturnsBundleProvider(@OperationParam(name = "myparam") StringType theString) {
			return null;
		}

	}

	public static class OrganizationProvider extends BaseProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Organization.class;
		}

	}

	public static class PatientProvider extends BaseProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

	public static class PlainProvider {

		@Operation(name = "$myoperation", idempotent = true)
		public Parameters opInstanceReturnsBundleProvider(@OperationParam(name = "myparam") StringType theString) {
			return null;
		}

	}

}
