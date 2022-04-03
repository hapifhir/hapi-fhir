package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.MoneyDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestOperation;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.OperationParameterUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperationServerDstu2Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;

	private static IdDt ourLastId;
	private static String ourLastMethod;
	private static StringDt ourLastParam1;
	private static Patient ourLastParam2;
	private static List<StringDt> ourLastParam3;
	private static MoneyDt ourLastParamMoney1;
	private static UnsignedIntDt ourLastParamUnsignedInt1;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;
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
		
		myFhirClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
	}

	@Test
	public void testConformance() throws Exception {
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogResponseBody(true);
		myFhirClient.registerInterceptor(loggingInterceptor);

		Conformance p = myFhirClient.fetchConformance().ofType(Conformance.class).prettyPrint().execute();
		List<RestOperation> ops = p.getRest().get(0).getOperation();
		assertThat(ops.size(), greaterThan(1));
		assertNull(ops.get(0).getDefinition().getReference().getBaseUrl());
		assertThat(ops.get(0).getDefinition().getReference().getValue(), startsWith("OperationDefinition/"));

		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId(ops.get(0).getDefinition().getReference()).execute();
		assertThat(def.getCode(), not(blankOrNullString()));

		List<String> opNames = toOpNames(ops);
		assertThat(opNames, containsInRelativeOrder("OP_TYPE"));
		
		assertEquals("OperationDefinition/Patient-t-OP_TYPE", ops.get(opNames.indexOf("OP_TYPE")).getDefinition().getReference().getValue());
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
		assertEquals(OperationParameterUseEnum.IN.getCode(), def.getParameter().get(0).getUse());
		assertEquals(0, def.getParameter().get(0).getMin().intValue());
		assertEquals("1", def.getParameter().get(0).getMax());
		
		assertEquals("PARAM2", def.getParameter().get(1).getName());
		assertEquals(OperationParameterUseEnum.IN.getCode(), def.getParameter().get(1).getUse());
		assertEquals(0, def.getParameter().get(1).getMin().intValue());
		assertEquals("1", def.getParameter().get(1).getMax());
		
		assertEquals("PARAM3", def.getParameter().get(2).getName());
		assertEquals(OperationParameterUseEnum.IN.getCode(), def.getParameter().get(2).getUse());
		assertEquals(2, def.getParameter().get(2).getMin().intValue());
		assertEquals("5", def.getParameter().get(2).getMax());
		
		assertEquals("PARAM4", def.getParameter().get(3).getName());
		assertEquals(OperationParameterUseEnum.IN.getCode(), def.getParameter().get(3).getUse());
		assertEquals(1, def.getParameter().get(3).getMin().intValue());
		assertEquals("*", def.getParameter().get(3).getMax());
		
	}

	
	private List<String> toOpNames(List<RestOperation> theOps) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (RestOperation next : theOps) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	@Test
	public void testInstanceEverythingGet() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$everything");
		CloseableHttpResponse status = ourClient.execute(httpGet);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("instance $everything", ourLastMethod);
		assertThat(response, startsWith("<Bundle"));
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testInstanceEverythingHapiClient() throws Exception {
		Parameters p = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort).operation().onInstance(new IdDt("Patient/123")).named("$everything").withParameters(new Parameters()).execute();
		Bundle b = (Bundle) p.getParameterFirstRep().getResource();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testInstanceEverythingPost() throws Exception {
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

		// Try with a POST
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$everything");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("instance $everything", ourLastMethod);
		assertThat(response, startsWith("<Bundle"));
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	public void testOperationCantUseGetIfItIsntIdempotent() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("POST", status.getFirstHeader(Constants.HEADER_ALLOW).getValue());
		assertThat(response, containsString("HTTP Method GET is not allowed"));
	}

	@Test
	public void testOperationWrongParameterType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerDt(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String response = IOUtils.toString(status.getEntity().getContent());
			assertThat(response, containsString("Request has parameter PARAM1 of type IntegerDt but method expects type StringDt"));
			ourLog.info(response);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testOperationOnInstance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());

		/*
		 * Against type should fail
		 */

		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		status = ourClient.execute(httpPost);

		response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(response);
		assertEquals(400, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testOperationOnInstanceAndType_Instance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$OP_INSTANCE_OR_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());

	}

	@Test
	public void testOperationOnInstanceAndType_Type() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_INSTANCE_OR_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals(null, ourLastId);
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnServer() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/$OP_SERVER");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	public void testOperationOnTypeReturnBundle() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE_RET_BUNDLE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertEquals("100", resp.getEntryFirstRep().getResponse().getStatus());
	}

	@Test
	public void testOperationWithBundleProviderResponse() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/$OP_INSTANCE_BUNDLE_PROVIDER?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(response);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
	}

	@Test
	public void testOperationWithGetUsingParams() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$OP_TYPE?PARAM1=PARAM1val");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
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
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("POST", status.getFirstHeader(Constants.HEADER_ALLOW).getValue());
		assertThat(response, containsString("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype"));
	}

	@Test
	public void testOperationWithListParam() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3").setValue(new StringDt("PARAM3val1"));
		p.addParameter().setName("PARAM3").setValue(new StringDt("PARAM3val2"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/$OP_SERVER_LIST_PARAM");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("$OP_SERVER_LIST_PARAM", ourLastMethod);
		assertEquals(true, ourLastParam2.getActive().booleanValue());
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
		p.addParameter().setName("PARAM1").setValue(new IntegerDt("123"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_PROFILE_DT");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	public void testOperationWithProfileDatatypeParams2() throws Exception {
		Parameters p = new Parameters();
		MoneyDt money = new MoneyDt();
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
		p.addParameter().setName("PARAM1").setValue(new IntegerDt("123"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$OP_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(400, status.getStatusLine().getStatusCode());
		String response = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(status.getStatusLine().toString());
		ourLog.info(response);

		assertThat(response, containsString("Request has parameter PARAM1 of type IntegerDt but method expects type StringDt"));
	}

	@Test
	public void testReadWithOperations() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
		HttpResponse status = ourClient.execute(httpGet);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals("read", ourLastMethod);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forDstu2();
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2));

		servlet.setFhirContext(ourCtx);
		servlet.setResourceProviders(new PatientProvider());
		servlet.setPlainProviders(new PlainProvider());
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

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

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@Operation(name="$OP_INSTANCE")
		public Parameters opInstance(
				@IdParam IdDt theId,
				@OperationParam(name="PARAM1") StringDt theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_INSTANCE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_INSTANCE_OR_TYPE")
		public Parameters opInstanceOrType(
				@IdParam(optional=true) IdDt theId,
				@OperationParam(name="PARAM1") StringDt theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_INSTANCE_OR_TYPE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_PROFILE_DT2", idempotent=true)
		public Bundle opProfileDt(
				@OperationParam(name="PARAM1") MoneyDt theParam1
				) {
			//@formatter:on

			ourLastMethod = "$OP_PROFILE_DT2";
			ourLastParamMoney1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_PROFILE_DT", idempotent=true)
		public Bundle opProfileDt(
				@OperationParam(name="PARAM1") UnsignedIntDt theParam1
				) {
			//@formatter:on

			ourLastMethod = "$OP_PROFILE_DT";
			ourLastParamUnsignedInt1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_TYPE", idempotent=true)
		public Parameters opType(
				@OperationParam(name="PARAM1") StringDt theParam1,
				@OperationParam(name="PARAM2") Patient theParam2,
				@OperationParam(name="PARAM3", min=2, max=5) List<StringDt> theParam3,
				@OperationParam(name="PARAM4", min=1) List<StringDt> theParam4
				) {
			//@formatter:on

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_TYPE_ONLY_STRING", idempotent=true)
		public Parameters opTypeOnlyString(
				@OperationParam(name="PARAM1") StringDt theParam1
				) {
			//@formatter:on

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_TYPE_RET_BUNDLE")
		public Bundle opTypeRetBundle(
				@OperationParam(name="PARAM1") StringDt theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_TYPE_RET_BUNDLE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		@Operation(name = "$everything", idempotent = true)
		public Bundle patientEverything(@IdParam IdDt thePatientId) {
			ourLastMethod = "instance $everything";
			ourLastId = thePatientId;
			return new Bundle();
		}

		/**
		 * Just to make sure this method doesn't "steal" calls
		 */
		@Read
		public Patient read(@IdParam IdDt theId) {
			ourLastMethod = "read";
			Patient retVal = new Patient();
			retVal.setId(theId);
			return retVal;
		}

	}

	public static class PlainProvider {

		//@formatter:off
		@Operation(name="$OP_INSTANCE_BUNDLE_PROVIDER", idempotent=true)
		public IBundleProvider opInstanceReturnsBundleProvider() {
			ourLastMethod = "$OP_INSTANCE_BUNDLE_PROVIDER";

			List<IBaseResource> resources = new ArrayList<>();
			for (int i =0; i < 100;i++) {
				Patient p = new Patient();
				p.setId("Patient/" + i);
				p.addName().addFamily("Patient " + i);
				resources.add(p);
			}
			
			return new SimpleBundleProvider(resources);
		}

		//@formatter:off
		@Operation(name="$OP_SERVER")
		public Parameters opServer(
				@OperationParam(name="PARAM1") StringDt theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_SERVER_LIST_PARAM")
		public Parameters opServerListParam(
				@OperationParam(name="PARAM2") Patient theParam2,
				@OperationParam(name="PARAM3") List<StringDt> theParam3
				) {
			//@formatter:on

			ourLastMethod = "$OP_SERVER_LIST_PARAM";
			ourLastParam2 = theParam2;
			ourLastParam3 = theParam3;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringDt("RETVAL1"));
			return retVal;
		}

	}

}
