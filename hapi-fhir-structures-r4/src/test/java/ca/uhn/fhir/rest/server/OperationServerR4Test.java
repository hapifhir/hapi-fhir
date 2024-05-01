package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertNull;
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
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;

public class OperationServerR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerR4Test.class);
	private static final String TEXT_HTML = "text/html";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		.withDefaultResponseEncoding(EncodingEnum.XML);
	@RegisterExtension
	private static final HttpClientExtension ourClient = new HttpClientExtension();
	private static IdType ourLastId;
	private static String ourLastMethod;
	private static StringType ourLastParam1;
	private static Patient ourLastParam2;
	private static List<StringType> ourLastParam3;
	private static MoneyQuantity ourLastParamMoney1;
	private static UnsignedIntType ourLastParamUnsignedInt1;
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

		myFhirClient = ourServer.getFhirClient();
	}

	@Test
	public void testConformance() {
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogResponseBody(true);
		myFhirClient.registerInterceptor(loggingInterceptor);

		CapabilityStatement p = myFhirClient.fetchConformance().ofType(CapabilityStatement.class).prettyPrint().execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));

		List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> ops = p.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient" )).findFirst().orElseThrow(() -> new IllegalArgumentException()).getOperation();
		assertThat(ops.size()).isGreaterThan(1);

		List<String> opNames = toOpNames(ops);
		assertThat(opNames.toString(), opNames, containsInRelativeOrder("OP_TYPE" ));

		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId(ops.get(opNames.indexOf("OP_TYPE" )).getDefinition()).execute();
		assertThat(def.getCode()).isEqualTo("OP_TYPE");
	}

	/**
	 * See #380
	 */
	@Test
	public void testOperationDefinition() {
		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId("OperationDefinition/Patient-t-OP_TYPE" ).execute();

		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(def));

//		@OperationParam(name="PARAM1") StringType theParam1,
//		@OperationParam(name="PARAM2") Patient theParam2,
//		@OperationParam(name="PARAM3", min=2, max=5) List<StringType> theParam3,
//		@OperationParam(name="PARAM4", min=1) List<StringType> theParam4,

		assertThat(def.getParameter()).hasSize(4);
		assertThat(def.getParameter().get(0).getName()).isEqualTo("PARAM1");
		assertThat(def.getParameter().get(0).getUse()).isEqualTo(OperationParameterUse.IN);
		assertThat(def.getParameter().get(0).getMin()).isEqualTo(0);
		assertThat(def.getParameter().get(0).getMax()).isEqualTo("1");

		assertThat(def.getParameter().get(1).getName()).isEqualTo("PARAM2");
		assertThat(def.getParameter().get(1).getUse()).isEqualTo(OperationParameterUse.IN);
		assertThat(def.getParameter().get(1).getMin()).isEqualTo(0);
		assertThat(def.getParameter().get(1).getMax()).isEqualTo("1");

		assertThat(def.getParameter().get(2).getName()).isEqualTo("PARAM3");
		assertThat(def.getParameter().get(2).getUse()).isEqualTo(OperationParameterUse.IN);
		assertThat(def.getParameter().get(2).getMin()).isEqualTo(2);
		assertThat(def.getParameter().get(2).getMax()).isEqualTo("5");

		assertThat(def.getParameter().get(3).getName()).isEqualTo("PARAM4");
		assertThat(def.getParameter().get(3).getUse()).isEqualTo(OperationParameterUse.IN);
		assertThat(def.getParameter().get(3).getMin()).isEqualTo(1);
		assertThat(def.getParameter().get(3).getMax()).isEqualTo("*");

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
		patient.addName().setFamily("FAMILY" ).addGiven("GIVEN" );
		patient.addIdentifier().setSystem("SYSTEM" ).setValue("VALUE" );
		bundle.addEntry().setResource(patient);

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE_RETURNING_BUNDLE"
			+ "?_pretty=true&_elements=identifier" );
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", response);
			Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
			Patient pt = (Patient) resp.getEntry().get(0).getResource();
			assertThat(pt.getName()).isEmpty();
			assertThat(pt.getIdentifier()).hasSize(1);
		}

	}

	@Test
	public void testManualResponseWithPrimitiveParam() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$manualResponseWithPrimitiveParam?path=THIS_IS_A_PATH" );
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		}

		assertThat(ourLastMethod).isEqualTo("$manualResponseWithPrimitiveParam");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");
		assertThat(ourLastParam1.getValue()).isEqualTo("THIS_IS_A_PATH");

	}

	@Test
	public void testInstanceEverythingGet() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$everything" );
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response).startsWith("<Bundle");
		}

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");

	}

	@Test
	public void testInstanceOnPlainProvider() throws Exception {

		// Try with a GET
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$OP_PLAIN_PROVIDER_ON_INSTANCE" );
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response).startsWith("<Bundle");
		}

		assertThat(ourLastMethod).isEqualTo("$OP_PLAIN_PROVIDER_ON_INSTANCE");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");
		assertThat(ourLastRestOperation).isEqualTo(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
	}

	@Test
	public void testInstanceEverythingHapiClient() {
		ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdType("Patient/123" )).named("$everything" ).withParameters(new Parameters()).execute();

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");


	}

	@Test
	public void testInstanceVersionEverythingHapiClient() {
		ourCtx
			.newRestfulGenericClient(ourServer.getBaseUrl())
			.operation()
			.onInstanceVersion(new IdType("Patient/123/_history/456" ))
			.named("$everything" )
			.withParameters(new Parameters())
			.execute();

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(ourLastId.toUnqualified().getValue()).isEqualTo("Patient/123/_history/456");


	}

	@Test
	public void testInstanceEverythingPost() throws Exception {
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

		// Try with a POST
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$everything" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(response).startsWith("<Bundle");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");

	}

	@Test
	public void testManualInputAndOutput() throws Exception {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$manualInputAndOutput" );
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String receivedContentType = status.getEntity().getContentType().getValue();
			byte[] receivedBytes = IOUtils.toByteArray(status.getEntity().getContent());
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(receivedContentType).isEqualTo(contentType.getMimeType());
			assertThat(receivedBytes).containsExactly(bytes);

		}

	}

	@Test
	public void testManualInputAndOutputWithUrlParam() throws Exception {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$manualInputAndOutputWithParam?param1=value" );
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String receivedContentType = status.getEntity().getContentType().getValue();
			byte[] receivedBytes = IOUtils.toByteArray(status.getEntity().getContent());
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(receivedContentType).isEqualTo(contentType.getMimeType());
			assertThat(receivedBytes).containsExactly(bytes);
			assertThat(ourLastParam1.getValue()).isEqualTo("value");

		}

	}

	@Test
	public void testOperationCantUseGetIfItIsntIdempotent() throws Exception {
		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE" );
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(status.getFirstHeader(Constants.HEADER_ALLOW).getValue()).isEqualTo("POST");
		assertThat(response).contains("HTTP Method GET is not allowed");
	}

	@Test
	public void testOperationWrongParameterType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
			ourLog.info(response);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testOperationOnInstance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
			assertNull(status.getFirstHeader(Constants.HEADER_ETAG));
		}

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastId.getIdPart()).isEqualTo("123");
		assertThat(ourLastMethod).isEqualTo("$OP_INSTANCE");

		/*
		 * Against type should fail
		 */

		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_INSTANCE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
			assertNull(status.getFirstHeader(Constants.HEADER_ETAG));

		}
	}

	@Test
	public void testOperationOnInstanceAndType_Instance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE_OR_TYPE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastId.getIdPart()).isEqualTo("123");
		assertThat(ourLastMethod).isEqualTo("$OP_INSTANCE_OR_TYPE");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");

	}

	@Test
	public void testOperationOnInstanceAndType_Type() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_INSTANCE_OR_TYPE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		CloseableHttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertNull(ourLastId);
		assertThat(ourLastMethod).isEqualTo("$OP_INSTANCE_OR_TYPE");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationOnServer() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_SERVER");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationOnServerWithRawString() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER_WITH_RAW_STRING" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_SERVER");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationOnType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationOnTypeReturnBundle() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE_RET_BUNDLE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE_RET_BUNDLE");

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertThat(resp.getEntryFirstRep().getResponse().getStatus()).isEqualTo("100");
	}

	@Test
	public void testOperationWithBundleProviderResponse() throws Exception {
		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/$OP_SERVER_BUNDLE_PROVIDER?_pretty=true" );
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(response);

		ourCtx.newXmlParser().parseResource(Bundle.class, response);
	}

	@Test
	public void testOperationWithGetUsingParams() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE?PARAM1=PARAM1val" );
		HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertNull(ourLastParam2);
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationWithGetUsingParamsFailsWithNonPrimitive() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo" );
		HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(405);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(status.getFirstHeader(Constants.HEADER_ALLOW).getValue()).isEqualTo("POST");
		assertThat(response).contains("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype");
	}

	@Test
	public void testOperationWithListParam() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3" ).setValue(new StringType("PARAM3val1" ));
		p.addParameter().setName("PARAM3" ).setValue(new StringType("PARAM3val2" ));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER_LIST_PARAM" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("$OP_SERVER_LIST_PARAM");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertNull(ourLastParam1);
		assertThat(ourLastParam3).hasSize(2);
		assertThat(ourLastParam3.get(0).getValue()).isEqualTo("PARAM3val1");
		assertThat(ourLastParam3.get(1).getValue()).isEqualTo("PARAM3val2");

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
	}

	@Test
	public void testOperationWithProfileDatatypeParams() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType("123" ));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_PROFILE_DT" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		status.getEntity().getContent().close();

		assertThat(ourLastMethod).isEqualTo("$OP_PROFILE_DT");
		assertThat(ourLastParamUnsignedInt1.getValueAsString()).isEqualTo("123");
	}

	@Test
	public void testOperationWithProfileDatatypeParams2() throws Exception {
		Parameters p = new Parameters();
		MoneyQuantity money = new MoneyQuantity();
		money.setCode("CODE" );
		money.setSystem("SYSTEM" );
		money.setValue(123L);
		p.addParameter().setName("PARAM1" ).setValue(money);
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_PROFILE_DT2" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("$OP_PROFILE_DT2");
		assertThat(ourLastParamMoney1.getCode()).isEqualTo("CODE");
		assertThat(ourLastParamMoney1.getSystem()).isEqualTo("SYSTEM");
		assertThat(ourLastParamMoney1.getValue().toString()).isEqualTo("123");
	}

	@Test
	public void testOperationWithProfileDatatypeUrl() throws Exception {
		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_PROFILE_DT?PARAM1=123" );
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("$OP_PROFILE_DT");
		assertThat(ourLastParamUnsignedInt1.getValueAsString()).isEqualTo("123");
	}

	@Test
	public void testOperationWrongParamType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType("123" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE" );
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8" )));
		HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
		String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(status.getStatusLine().toString());
		ourLog.info(response);

		assertThat(response).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
	}

	@Test
	public void testReadWithOperations() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123" );
		HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("read");
	}

	@Test
	public void testReturnBinaryWithAcceptFhir() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/$binaryop?_pretty=false" );
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(ourLastMethod).isEqualTo("$binaryop");

			assertThat(status.getEntity().getContentType().getValue()).isEqualTo("application/fhir+xml;charset=utf-8");
			assertThat(IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8)).isEqualTo("<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"text/html\"/><data value=\"PGh0bWw+VEFHUzwvaHRtbD4=\"/></Binary>");
		}
	}

	@Test
	public void testReturnBinaryWithAcceptHtml() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/$binaryop" );
		httpGet.addHeader(Constants.HEADER_ACCEPT, TEXT_HTML);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(ourLastMethod).isEqualTo("$binaryop");

			assertThat(status.getEntity().getContentType().getValue()).isEqualTo("text/html");
			assertThat(IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8)).isEqualTo("<html>TAGS</html>");
		}
	}

	public static class PatientProvider implements IResourceProvider {


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "$OP_INSTANCE" )
		public Parameters opInstance(
			@IdParam IdType theId,
			@OperationParam(name = "PARAM1" ) StringType theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2
		) {

			ourLastMethod = "$OP_INSTANCE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.setId("Parameters/123/_history/1" );
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
			return retVal;
		}

		@Operation(name = "$OP_INSTANCE_OR_TYPE" )
		public Parameters opInstanceOrType(
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "PARAM1" ) StringType theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2
		) {

			ourLastMethod = "$OP_INSTANCE_OR_TYPE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.setId("Parameters/123/_history/1" );
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
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
			@OperationParam(name = "PARAM1" ) MoneyQuantity theParam1
		) {

			ourLastMethod = "$OP_PROFILE_DT2";
			ourLastParamMoney1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100" );
			return retVal;
		}

		@Operation(name = "$OP_PROFILE_DT", idempotent = true)
		public Bundle opProfileType(
			@OperationParam(name = "PARAM1" ) UnsignedIntType theParam1
		) {

			ourLastMethod = "$OP_PROFILE_DT";
			ourLastParamUnsignedInt1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100" );
			return retVal;
		}

		@SuppressWarnings("unused" )
		@Operation(name = "$OP_TYPE", idempotent = true)
		public Parameters opType(
			@OperationParam(name = "PARAM1" ) StringType theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2,
			@OperationParam(name = "PARAM3", min = 2, max = 5) List<StringType> theParam3,
			@OperationParam(name = "PARAM4", min = 1) List<StringType> theParam4
		) {

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
			return retVal;
		}

		@Operation(name = "$OP_TYPE_ONLY_STRING", idempotent = true)
		public Parameters opTypeOnlyString(
			@OperationParam(name = "PARAM1" ) StringType theParam1
		) {

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
			return retVal;
		}

		@Operation(name = "$OP_TYPE_RET_BUNDLE" )
		public Bundle opTypeRetBundle(
			@OperationParam(name = "PARAM1" ) StringType theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2
		) {

			ourLastMethod = "$OP_TYPE_RET_BUNDLE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100" );
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
			@OperationParam(name = "param1" ) StringType theParam1,
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

		@Operation(name = "$OP_SERVER" )
		public Parameters opServer(
			@OperationParam(name = "PARAM1" ) StringType theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2
		) {

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
			return retVal;
		}

		@Operation(name = "$OP_SERVER_WITH_RAW_STRING" )
		public Parameters opServer(
			@OperationParam(name = "PARAM1" ) String theParam1,
			@OperationParam(name = "PARAM2" ) Patient theParam2
		) {

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = new StringType(theParam1);
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
			return retVal;
		}

		@Operation(name = "$OP_SERVER_LIST_PARAM" )
		public Parameters opServerListParam(
			@OperationParam(name = "PARAM2" ) Patient theParam2,
			@OperationParam(name = "PARAM3" ) List<StringType> theParam3
		) {

			ourLastMethod = "$OP_SERVER_LIST_PARAM";
			ourLastParam2 = theParam2;
			ourLastParam3 = theParam3;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1" ).setValue(new StringType("RETVAL1" ));
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
		TestUtil.randomizeLocaleAndTimezone();
	}

}
