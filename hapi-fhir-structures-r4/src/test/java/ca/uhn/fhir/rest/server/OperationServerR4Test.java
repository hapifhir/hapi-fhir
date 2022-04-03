package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.MoneyQuantity;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperationServerR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerR4Test.class);
	private static final String TEXT_HTML = "text/html";
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;
	private static IdType ourLastId;
	private static String ourLastMethod;
	private static StringType ourLastParam1;
	private static Patient ourLastParam2;
	private static List<StringType> ourLastParam3;
	private static MoneyQuantity ourLastParamMoney1;
	private static UnsignedIntType ourLastParamUnsignedInt1;
	private static int ourPort;
	private static Server ourServer;
	private static IBaseResource ourNextResponse;
	private static RestOperationTypeEnum ourLastRestOperation;
	private IGenericClient myFhirClient;

	@BeforeEach
	public void before() {
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastParam3 = null;
		ourLastParamUnsignedInt1 = null;
		ourLastParamMoney1 = null;
		ourLastId = null;
		ourLastMethod = "";
		ourNextResponse = null;
		ourLastRestOperation = null;

		myFhirClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
	}

	@Test
	public void testConformance() {
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogResponseBody(true);
		myFhirClient.registerInterceptor(loggingInterceptor);

		CapabilityStatement p = myFhirClient.fetchConformance().ofType(CapabilityStatement.class).prettyPrint().execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));

		List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> ops = p.getRestFirstRep().getResource().stream().filter(t->t.getType().equals("Patient")).findFirst().orElseThrow(()->new IllegalArgumentException()).getOperation();
		assertThat(ops.size(), greaterThan(1));

		List<String> opNames = toOpNames(ops);
		assertThat(opNames.toString(), opNames, containsInRelativeOrder("OP_TYPE"));

		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId(ops.get(opNames.indexOf("OP_TYPE")).getDefinition()).execute();
		assertEquals("OP_TYPE", def.getCode());
	}

	/**
	 * See #380
	 */
	@Test
	public void testOperationDefinition() {
		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId("OperationDefinition/Patient-t-OP_TYPE").execute();

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(def));

//		@OperationParam(name="PARAM1") StringType theParam1,
//		@OperationParam(name="PARAM2") Patient theParam2,
//		@OperationParam(name="PARAM3", min=2, max=5) List<StringType> theParam3,
//		@OperationParam(name="PARAM4", min=1) List<StringType> theParam4,

		assertEquals(4, def.getParameter().size());
		assertEquals("PARAM1", def.getParameter().get(0).getName());
		assertEquals(OperationParameterUse.IN, def.getParameter().get(0).getUse());
		assertEquals(0, def.getParameter().get(0).getMin());
		assertEquals("1", def.getParameter().get(0).getMax());

		assertEquals("PARAM2", def.getParameter().get(1).getName());
		assertEquals(OperationParameterUse.IN, def.getParameter().get(1).getUse());
		assertEquals(0, def.getParameter().get(1).getMin());
		assertEquals("1", def.getParameter().get(1).getMax());

		assertEquals("PARAM3", def.getParameter().get(2).getName());
		assertEquals(OperationParameterUse.IN, def.getParameter().get(2).getUse());
		assertEquals(2, def.getParameter().get(2).getMin());
		assertEquals("5", def.getParameter().get(2).getMax());

		assertEquals("PARAM4", def.getParameter().get(3).getName());
		assertEquals(OperationParameterUse.IN, def.getParameter().get(3).getUse());
		assertEquals(1, def.getParameter().get(3).getMin());
		assertEquals("*", def.getParameter().get(3).getMax());

	}

	private List<String> toOpNames(List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> theOps) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CapabilityStatement.CapabilityStatementRestResourceOperationComponent next : theOps) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	@Test
	public void testElementsFilterOnOperationResponse() throws Exception {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.COLLECTION);
		ourNextResponse = bundle;

		Patient patient = new Patient();
		patient.addName().setFamily("FAMILY").addGiven("GIVEN");
		patient.addIdentifier().setSystem("SYSTEM").setValue("VALUE");
		bundle.addEntry().setResource(patient);

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/$OP_TYPE_RETURNING_BUNDLE"
			+ "?_pretty=true&_elements=identifier");
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response);
			Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
			Patient pt = (Patient) resp.getEntry().get(0).getResource();
			assertEquals(0, pt.getName().size());
			assertEquals(1, pt.getIdentifier().size());
		}

	}

	@Test
	public void testManualResponseWithPrimitiveParam() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$manualResponseWithPrimitiveParam?path=THIS_IS_A_PATH");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		}

		assertEquals("$manualResponseWithPrimitiveParam", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());
		assertEquals("THIS_IS_A_PATH", ourLastParam1.getValue());

	}

	@Test
	public void testInstanceEverythingGet() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$everything");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response, startsWith("<Bundle"));
		}

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testInstanceOnPlainProvider() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$OP_PLAIN_PROVIDER_ON_INSTANCE");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response, startsWith("<Bundle"));
		}

		assertEquals("$OP_PLAIN_PROVIDER_ON_INSTANCE", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());
		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE, ourLastRestOperation);
	}

	@Test
	public void testInstanceEverythingHapiClient() {
		ourCtx.newRestfulGenericClient("http://localhost:" + ourPort).operation().onInstance(new IdType("Patient/123")).named("$everything").withParameters(new Parameters()).execute();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());


	}

	@Test
	public void testInstanceVersionEverythingHapiClient() {
		ourCtx
			.newRestfulGenericClient("http://localhost:" + ourPort)
			.operation()
			.onInstanceVersion(new IdType("Patient/123/_history/456"))
			.named("$everything")
			.withParameters(new Parameters())
			.execute();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123/_history/456", ourLastId.toUnqualified().getValue());


	}

	@Test
	public void testInstanceEverythingPost() throws Exception {
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

		// Try with a POST
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$everything");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("instance $everything", ourLastMethod);
		assertThat(response, startsWith("<Bundle"));
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testManualInputAndOutput() throws Exception {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$manualInputAndOutput");
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String receivedContentType = status.getEntity().getContentType().getValue();
			byte[] receivedBytes = IOUtils.toByteArray(status.getEntity().getContent());
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(contentType.getMimeType(), receivedContentType);
			assertArrayEquals(bytes, receivedBytes);

		}

	}

	@Test
	public void testManualInputAndOutputWithUrlParam() throws Exception {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$manualInputAndOutputWithParam?param1=value");
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String receivedContentType = status.getEntity().getContentType().getValue();
			byte[] receivedBytes = IOUtils.toByteArray(status.getEntity().getContent());
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(contentType.getMimeType(), receivedContentType);
			assertArrayEquals(bytes, receivedBytes);
			assertEquals("value", ourLastParam1.getValue());

		}

	}

	@Test
	public void testOperationCantUseGetIfItIsntIdempotent() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("POST", status.getFirstHeader(Constants.HEADER_ALLOW).getValue());
		assertThat(response, containsString("HTTP Method GET is not allowed"));
	}

	@Test
	public void testOperationWrongParameterType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerType(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response, containsString("Request has parameter PARAM1 of type IntegerType but method expects type StringType"));
			ourLog.info(response);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testOperationOnInstance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals("RET1", resp.getParameter().get(0).getName());
			assertNull(status.getFirstHeader(Constants.HEADER_ETAG));
		}

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE", ourLastMethod);

		/*
		 * Against type should fail
		 */

		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(400, status.getStatusLine().getStatusCode());
			assertNull(status.getFirstHeader(Constants.HEADER_ETAG));

		}
	}

	@Test
	public void testOperationOnInstanceAndType_Instance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE_OR_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());

	}

	@Test
	public void testOperationOnInstanceAndType_Type() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_INSTANCE_OR_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals(null, ourLastId);
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnServer() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/$OP_SERVER");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnServerWithRawString() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/$OP_SERVER_WITH_RAW_STRING");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnTypeReturnBundle() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE_RET_BUNDLE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertEquals("100", resp.getEntryFirstRep().getResponse().getStatus());
	}

	@Test
	public void testOperationWithBundleProviderResponse() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/$OP_SERVER_BUNDLE_PROVIDER?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(response);

		ourCtx.newXmlParser().parseResource(Bundle.class, response);
	}

	@Test
	public void testOperationWithGetUsingParams() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$OP_TYPE?PARAM1=PARAM1val");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertNull(ourLastParam2);
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationWithGetUsingParamsFailsWithNonPrimitive() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(405, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("POST", status.getFirstHeader(Constants.HEADER_ALLOW).getValue());
		assertThat(response, containsString("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype"));
	}

	@Test
	public void testOperationWithListParam() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val1"));
		p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val2"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/$OP_SERVER_LIST_PARAM");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("$OP_SERVER_LIST_PARAM", ourLastMethod);
		assertEquals(true, ourLastParam2.getActive());
		assertEquals(null, ourLastParam1);
		assertEquals(2, ourLastParam3.size());
		assertEquals("PARAM3val1", ourLastParam3.get(0).getValue());
		assertEquals("PARAM3val2", ourLastParam3.get(1).getValue());

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationWithProfileDatatypeParams() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_PROFILE_DT");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		status.getEntity().getContent().close();

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	public void testOperationWithProfileDatatypeParams2() throws Exception {
		Parameters p = new Parameters();
		MoneyQuantity money = new MoneyQuantity();
		money.setCode("CODE");
		money.setSystem("SYSTEM");
		money.setValue(123L);
		p.addParameter().setName("PARAM1").setValue(money);
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_PROFILE_DT2");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("$OP_PROFILE_DT2", ourLastMethod);
		assertEquals("CODE", ourLastParamMoney1.getCode());
		assertEquals("SYSTEM", ourLastParamMoney1.getSystem());
		assertEquals("123", ourLastParamMoney1.getValue().toString());
	}

	@Test
	public void testOperationWithProfileDatatypeUrl() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/$OP_PROFILE_DT?PARAM1=123");
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	public void testOperationWrongParamType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(400, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(status.getStatusLine().toString());
		ourLog.info(response);

		assertThat(response, containsString("Request has parameter PARAM1 of type IntegerType but method expects type StringType"));
	}

	@Test
	public void testReadWithOperations() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("read", ourLastMethod);
	}

	@Test
	public void testReturnBinaryWithAcceptFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$binaryop");
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("$binaryop", ourLastMethod);

			assertEquals("application/fhir+xml;charset=utf-8", status.getEntity().getContentType().getValue());
			assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"text/html\"/><data value=\"PGh0bWw+VEFHUzwvaHRtbD4=\"/></Binary>", IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8));
		}
	}

	@Test
	public void testReturnBinaryWithAcceptHtml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$binaryop");
		httpGet.addHeader(Constants.HEADER_ACCEPT, TEXT_HTML);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("$binaryop", ourLastMethod);

			assertEquals("text/html", status.getEntity().getContentType().getValue());
			assertEquals("<html>TAGS</html>", IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8));
		}
	}

	public static class PatientProvider implements IResourceProvider {


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "$OP_INSTANCE")
		public Parameters opInstance(
			@IdParam IdType theId,
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_INSTANCE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.setId("Parameters/123/_history/1");
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_INSTANCE_OR_TYPE")
		public Parameters opInstanceOrType(
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_INSTANCE_OR_TYPE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.setId("Parameters/123/_history/1");
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_TYPE_RETURNING_BUNDLE", idempotent = true)
		public IBaseResource opTypeReturningBundle(
		) {
			ourLastMethod = "$OP_TYPE_RETURNING_BUNDLE";
			return ourNextResponse;
		}


		@Operation(name = "$OP_PROFILE_DT2", idempotent = true)
		public Bundle opProfileType(
			@OperationParam(name = "PARAM1") MoneyQuantity theParam1
		) {

			ourLastMethod = "$OP_PROFILE_DT2";
			ourLastParamMoney1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		@Operation(name = "$OP_PROFILE_DT", idempotent = true)
		public Bundle opProfileType(
			@OperationParam(name = "PARAM1") UnsignedIntType theParam1
		) {

			ourLastMethod = "$OP_PROFILE_DT";
			ourLastParamUnsignedInt1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		@SuppressWarnings("unused")
		@Operation(name = "$OP_TYPE", idempotent = true)
		public Parameters opType(
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2,
			@OperationParam(name = "PARAM3", min = 2, max = 5) List<StringType> theParam3,
			@OperationParam(name = "PARAM4", min = 1) List<StringType> theParam4
		) {

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_TYPE_ONLY_STRING", idempotent = true)
		public Parameters opTypeOnlyString(
			@OperationParam(name = "PARAM1") StringType theParam1
		) {

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_TYPE_RET_BUNDLE")
		public Bundle opTypeRetBundle(
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_TYPE_RET_BUNDLE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		@Operation(name = "$everything", idempotent = true)
		public Bundle patientEverything(@IdParam IdType thePatientId) {
			ourLastMethod = "instance $everything";
			ourLastId = thePatientId;
			return new Bundle();
		}

		@Operation(name = "$manualInputAndOutput", manualResponse = true, manualRequest = true)
		public void manualInputAndOutput(HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws IOException {
			String contentType = theServletRequest.getContentType();
			byte[] bytes = IOUtils.toByteArray(theServletRequest.getInputStream());

			ourLog.info("Received call with content type {} and {} bytes", contentType, bytes.length);

			theServletResponse.setContentType(contentType);
			theServletResponse.getOutputStream().write(bytes);
			theServletResponse.getOutputStream().close();
		}

		@Operation(name = "$manualInputAndOutputWithParam", manualResponse = true, manualRequest = true)
		public void manualInputAndOutputWithParam(
			@OperationParam(name = "param1") StringType theParam1,
			HttpServletRequest theServletRequest,
			HttpServletResponse theServletResponse
		) throws IOException {

			ourLastParam1 = theParam1;
			String contentType = theServletRequest.getContentType();
			byte[] bytes = IOUtils.toByteArray(theServletRequest.getInputStream());

			ourLog.info("Received call with content type {} and {} bytes", contentType, bytes.length);

			theServletResponse.setContentType(contentType);
			theServletResponse.getOutputStream().write(bytes);
			theServletResponse.getOutputStream().close();
		}

		/**
		 * Just to make sure this method doesn't "steal" calls
		 */
		@Read
		public Patient read(@IdParam IdType theId) {
			ourLastMethod = "read";
			Patient retVal = new Patient();
			retVal.setId(theId);
			return retVal;
		}

	}

	public static class PlainProvider {


		@Operation(name = "$OP_PLAIN_PROVIDER_ON_INSTANCE", idempotent = true, global = true)
		public IBundleProvider opPlainProviderOnInstance(@IdParam IdType theId, RequestDetails theRequestDetails) {
			ourLastMethod = "$OP_PLAIN_PROVIDER_ON_INSTANCE";
			ourLastId = theId;
			ourLastRestOperation = theRequestDetails.getRestOperationType();

			List<IBaseResource> resources = new ArrayList<IBaseResource>();
			for (int i = 0; i < 100; i++) {
				Patient p = new Patient();
				p.setId("Patient/" + i);
				p.addName().setFamily("Patient " + i);
				resources.add(p);
			}

			return new SimpleBundleProvider(resources);
		}


		@Operation(name = "$OP_SERVER_BUNDLE_PROVIDER", idempotent = true)
		public IBundleProvider opInstanceReturnsBundleProvider() {
			ourLastMethod = "$OP_SERVER_BUNDLE_PROVIDER";

			List<IBaseResource> resources = new ArrayList<IBaseResource>();
			for (int i = 0; i < 100; i++) {
				Patient p = new Patient();
				p.setId("Patient/" + i);
				p.addName().setFamily("Patient " + i);
				resources.add(p);
			}

			return new SimpleBundleProvider(resources);
		}

		@Operation(name = "$manualResponseWithPrimitiveParam", idempotent = true, global = true, manualResponse = true)
		public void manualResponseWithPrimitiveParam(
			@IdParam IIdType theResourceId,
			@OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath,
			ServletRequestDetails theRequestDetails,
			HttpServletRequest theServletRequest,
			HttpServletResponse theServletResponse) {

			ourLastMethod = "$manualResponseWithPrimitiveParam";
			ourLastId = (IdType) theResourceId;
			ourLastParam1 = (StringType) thePath;

			theServletResponse.setStatus(200);
		}

		@Operation(name = "$OP_SERVER")
		public Parameters opServer(
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_SERVER_WITH_RAW_STRING")
		public Parameters opServer(
			@OperationParam(name = "PARAM1") String theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = new StringType(theParam1);
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$OP_SERVER_LIST_PARAM")
		public Parameters opServerListParam(
			@OperationParam(name = "PARAM2") Patient theParam2,
			@OperationParam(name = "PARAM3") List<StringType> theParam3
		) {

			ourLastMethod = "$OP_SERVER_LIST_PARAM";
			ourLastParam2 = theParam2;
			ourLastParam3 = theParam3;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Operation(name = "$binaryop", idempotent = true)
		public Binary binaryOp(
			@OperationParam(name = "PARAM3", min = 0, max = 1) List<StringType> theParam3
		) {

			ourLastMethod = "$binaryop";
			ourLastParam3 = theParam3;

			Binary retVal = new Binary();
			retVal.setContentType(TEXT_HTML);
			retVal.setContent("<html>TAGS</html>".getBytes(Charsets.UTF_8));
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
		ourCtx = FhirContext.forR4();
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2));

		servlet.setFhirContext(ourCtx);
		PlainProvider plainProvider = new PlainProvider();
		PatientProvider patientProvider = new PatientProvider();
		servlet.registerProviders(patientProvider, plainProvider);
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
