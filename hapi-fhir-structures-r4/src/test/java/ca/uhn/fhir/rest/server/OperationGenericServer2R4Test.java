package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.junit.*;

import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OperationGenericServer2R4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationGenericServer2R4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;
	private static IdType ourLastId;
	private static Object ourLastParam1;
	private static Object ourLastParam2;
	private static Parameters ourLastResourceParam;
	private int myPort;
	private Server myServer;

	@Before
	public void before() {
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastId = null;
		ourLastResourceParam = null;
	}


	@Test
	public void testDeclarativeTypedParameters() throws Exception {

		@SuppressWarnings("unused")
		class PatientProvider implements IResourceProvider {

			@Override
			public Class<Patient> getResourceType() {
				return Patient.class;
			}

			@Operation(name = "$OP_INSTANCE")
			public Parameters opInstance(
				@ResourceParam() IBaseResource theResourceParam,
				@IdParam IdType theId,
				@OperationParam(name = "PARAM1", typeName = "code") IPrimitiveType<String> theParam1,
				@OperationParam(name = "PARAM2", typeName = "Coding") ICompositeType theParam2
			) {

				ourLastId = theId;
				ourLastParam1 = theParam1;
				ourLastParam2 = theParam2;
				ourLastResourceParam = (Parameters) theResourceParam;

				Parameters retVal = new Parameters();
				retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
				return retVal;
			}

		}

		PatientProvider provider = new PatientProvider();
		startServer(provider);

		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new CodeType("PARAM1val"));
		p.addParameter().setName("PARAM2").setValue(new Coding("sys", "val", "dis"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + myPort + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			status.getEntity().getContent().close();

			CodeType param1 = (CodeType) ourLastParam1;
			assertEquals("PARAM1val", param1.getValue());

			Coding param2 = (Coding) ourLastParam2;
			assertEquals("sys", param2.getSystem());
			assertEquals("val", param2.getCode());
			assertEquals("dis", param2.getDisplay());
		}

	}

	@Test
	public void testDeclarativeTypedParametersInvalid() throws Exception {

		@SuppressWarnings("unused")
		class PatientProvider implements IResourceProvider {

			@Override
			public Class<Patient> getResourceType() {
				return Patient.class;
			}

			@Operation(name = "$OP_INSTANCE")
			public Parameters opInstance(
				@OperationParam(name = "PARAM2", typeName = "code") ICompositeType theParam2
			) {
				return new Parameters();
			}

		}

		try {
			PatientProvider provider = new PatientProvider();
			startServer(provider);
			fail();
		} catch (ServletException e) {
			ConfigurationException ce = (ConfigurationException) e.getCause();
			assertEquals("Failure scanning class PatientProvider: Non assignable parameter typeName=\"code\" specified on method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test$2PatientProvider.opInstance(org.hl7.fhir.instance.model.api.ICompositeType)", ce.getMessage());
		}
	}


	@Test
	public void testTypeOperationWithTypeDeclaredByName() throws Exception {

		@SuppressWarnings("unused")
		class PlainProvider {

			@Operation(name = "$OP_INSTANCE", typeName = "Patient", idempotent = true)
			public Parameters opInstance(
				@ResourceParam() IBaseResource theResourceParam,
				@IdParam IdType theId
			) {

				ourLastId = theId;

				Parameters retVal = new Parameters();
				retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
				return retVal;
			}

		}

		PlainProvider provider = new PlainProvider();
		startServer(provider);

		HttpGet httpPost = new HttpGet("http://localhost:" + myPort + "/Patient/123/$OP_INSTANCE");
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			status.getEntity().getContent().close();

			assertEquals("123", ourLastId.getIdPart());
		}

	}

	@Test
	public void testTypeOperationWithInvalidType() throws Exception {

		@SuppressWarnings("unused")
		class PlainProvider {

			@Operation(name = "$OP_INSTANCE", typeName = "FOO", idempotent = true)
			public Parameters opInstance() {
				return null;
			}

		}

		PlainProvider provider = new PlainProvider();
		try {
			startServer(provider);
			fail();
		} catch (ServletException e) {
			Throwable cause = e.getRootCause();
			assertEquals("Failure scanning class PlainProvider: Failed to bind method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test$2PlainProvider.opInstance() - Unknown resource name \"FOO\" (this name is not known in FHIR version \"R4\")", cause.getMessage());
		}
	}


	private void startServer(Object theProvider) throws Exception {
		myServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

		servlet.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2));

		servlet.setFhirContext(ourCtx);
		servlet.registerProvider(theProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);
	}


	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forR4();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
