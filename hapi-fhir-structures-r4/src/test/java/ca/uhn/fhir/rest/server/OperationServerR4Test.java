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
import ca.uhn.fhir.test.utilities.FhirHttpResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OperationServerR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerR4Test.class);
	private static final String TEXT_HTML = "text/html";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		.withDefaultResponseEncoding(EncodingEnum.XML);
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
	void before() {
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
	void testConformance() {
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogResponseBody(true);
		myFhirClient.registerInterceptor(loggingInterceptor);

		CapabilityStatement p = myFhirClient.fetchConformance().ofType(CapabilityStatement.class).prettyPrint().execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));

		List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> ops = p.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient" )).findFirst().orElseThrow(() -> new IllegalArgumentException()).getOperation();
		assertThat(ops.size()).isGreaterThan(1);

		List<String> opNames = toOpNames(ops);
		assertThat(opNames).as(opNames.toString()).contains("OP_TYPE" );

		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId(ops.get(opNames.indexOf("OP_TYPE" )).getDefinition()).execute();
		assertEquals("OP_TYPE", def.getCode());
	}

	/**
	 * See #380
	 */
	@Test
	void testOperationDefinition() {
		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId("OperationDefinition/Patient-t-OP_TYPE" ).execute();

		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(def));

//		@OperationParam(name="PARAM1") StringType theParam1,
//		@OperationParam(name="PARAM2") Patient theParam2,
//		@OperationParam(name="PARAM3", min=2, max=5) List<StringType> theParam3,
//		@OperationParam(name="PARAM4", min=1) List<StringType> theParam4,

		assertThat(def.getParameter()).hasSize(4);
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
	void testElementsFilterOnOperationResponse() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.COLLECTION);
		ourNextResponse = bundle;

		Patient patient = new Patient();
		patient.addName().setFamily("FAMILY" ).addGiven("GIVEN" );
		patient.addIdentifier().setSystem("SYSTEM" ).setValue("VALUE" );
		bundle.addEntry().setResource(patient);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE_RETURNING_BUNDLE?_pretty=true&_elements=identifier").get();

		status.assertStatus(200);
		ourLog.info("Response: {}", status.getBody());
		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
		Patient pt = (Patient) resp.getEntry().get(0).getResource();
		assertThat(pt.getName()).isEmpty();
		assertThat(pt.getIdentifier()).hasSize(1);

	}

	@Test
	void testManualResponseWithPrimitiveParam() {

		// Try with a GET
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$manualResponseWithPrimitiveParam?path=THIS_IS_A_PATH").get();
		status.assertStatus(200);

		assertEquals("$manualResponseWithPrimitiveParam", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());
		assertEquals("THIS_IS_A_PATH", ourLastParam1.getValue());

	}

	@Test
	void testInstanceEverythingGet() {

		// Try with a GET
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$everything").get();
		status.assertStatus(200);
		assertThat(status.getBody()).startsWith("<Bundle");

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	void testInstanceOnPlainProvider() {

		// Try with a GET
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_PLAIN_PROVIDER_ON_INSTANCE").get();
		status.assertStatus(200);
		assertThat(status.getBody()).startsWith("<Bundle");

		assertEquals("$OP_PLAIN_PROVIDER_ON_INSTANCE", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());
		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE, ourLastRestOperation);
	}

	@Test
	void testInstanceEverythingHapiClient() {
		ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdType("Patient/123" )).named("$everything" ).withParameters(new Parameters()).execute();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());


	}

	@Test
	void testInstanceVersionEverythingHapiClient() {
		ourCtx
			.newRestfulGenericClient(ourServer.getBaseUrl())
			.operation()
			.onInstanceVersion(new IdType("Patient/123/_history/456" ))
			.named("$everything" )
			.withParameters(new Parameters())
			.execute();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123/_history/456", ourLastId.toUnqualified().getValue());


	}

	@Test
	void testInstanceEverythingPost() {
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

		// Try with a POST
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$everything").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("instance $everything", ourLastMethod);
		assertThat(status.getBody()).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	void testManualInputAndOutput() {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$manualInputAndOutput" );
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$manualInputAndOutput").execute(httpPost);

		status.assertStatus(200);
		assertEquals(contentType.getMimeType(), status.getHeader("Content-Type"));
		assertThat(status.getBody().getBytes(StandardCharsets.UTF_8)).containsExactly(bytes);

	}

	@Test
	void testManualInputAndOutputWithUrlParam() {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
		ContentType contentType = ContentType.IMAGE_PNG;

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$manualInputAndOutputWithParam?param1=value" );
		httpPost.setEntity(new ByteArrayEntity(bytes, contentType));
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$manualInputAndOutputWithParam?param1=value").execute(httpPost);

		status.assertStatus(200);
		assertEquals(contentType.getMimeType(), status.getHeader("Content-Type"));
		assertThat(status.getBody().getBytes(StandardCharsets.UTF_8)).containsExactly(bytes);
		assertEquals("value", ourLastParam1.getValue());

	}

	@Test
	void testOperationCantUseGetIfItIsntIdempotent() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").get();

		status.assertStatus(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(status.getBody()).contains("HTTP Method GET is not allowed");
	}

	@Test
	void testOperationWrongParameterType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);
		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
		ourLog.info(status.getBody());
	}

	@Test
	void testOperationOnInstance() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);
		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
		assertNull(status.getHeader(Constants.HEADER_ETAG));

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE", ourLastMethod);

		/*
		 * Against type should fail
		 */

		status = ourServer.fhirRequest("/Patient/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);

		ourLog.info(status.getBody());
		status.assertStatus(400);
		assertNull(status.getHeader(Constants.HEADER_ETAG));
	}

	@Test
	void testOperationOnInstanceAndType_Instance() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());

	}

	@Test
	void testOperationOnInstanceAndType_Type() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertNull(ourLastId);
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnServer() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnServerWithRawString() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER_WITH_RAW_STRING").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnTypeReturnBundle() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new StringType("PARAM1val" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE_RET_BUNDLE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
		assertEquals("100", resp.getEntryFirstRep().getResponse().getStatus());
	}

	@Test
	void testOperationWithBundleProviderResponse() {
		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER_BUNDLE_PROVIDER?_pretty=true").get();

		status.assertStatus(200);
		ourLog.info(status.getBody());

		ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
	}

	@Test
	void testOperationWithGetUsingParams() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val").get();

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertNull(ourLastParam2);
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationWithGetUsingParamsFailsWithNonPrimitive() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo").get();

		status.assertStatus(405);

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(status.getBody()).contains("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype");
	}

	@Test
	void testOperationWithListParam() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3" ).setValue(new StringType("PARAM3val1" ));
		p.addParameter().setName("PARAM3" ).setValue(new StringType("PARAM3val2" ));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER_LIST_PARAM").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_SERVER_LIST_PARAM", ourLastMethod);
		assertEquals(true, ourLastParam2.getActive());
		assertNull(ourLastParam1);
		assertThat(ourLastParam3).hasSize(2);
		assertEquals("PARAM3val1", ourLastParam3.get(0).getValue());
		assertEquals("PARAM3val2", ourLastParam3.get(1).getValue());

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationWithProfileDatatypeParams() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType("123" ));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	void testOperationWithProfileDatatypeParams2() {
		Parameters p = new Parameters();
		MoneyQuantity money = new MoneyQuantity();
		money.setCode("CODE" );
		money.setSystem("SYSTEM" );
		money.setValue(123L);
		p.addParameter().setName("PARAM1" ).setValue(money);
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT2").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT2", ourLastMethod);
		assertEquals("CODE", ourLastParamMoney1.getCode());
		assertEquals("SYSTEM", ourLastParamMoney1.getSystem());
		assertEquals("123", ourLastParamMoney1.getValue().toString());
	}

	@Test
	void testOperationWithProfileDatatypeUrl() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT?PARAM1=123").get();

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	void testOperationWrongParamType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1" ).setValue(new IntegerType("123" ));
		p.addParameter().setName("PARAM2" ).setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(400);

		ourLog.info("HTTP {} {}", status.getStatusCode(), status.getReasonPhrase());
		ourLog.info(status.getBody());

		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
	}

	@Test
	void testReadWithOperations() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123").get();

		status.assertStatus(200);

		assertEquals("read", ourLastMethod);
	}

	@Test
	void testReturnBinaryWithAcceptFhir() {
		FhirHttpResponse status = ourServer.fhirRequest("/$binaryop?_pretty=false")
			.withHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY)
			.get();

		status.assertStatus(200);
		assertEquals("$binaryop", ourLastMethod);

		assertEquals("application/fhir+xml;charset=utf-8", status.getHeader("Content-Type"));
		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"text/html\"/><data value=\"PGh0bWw+VEFHUzwvaHRtbD4=\"/></Binary>", status.getBody());
	}

	@Test
	void testReturnBinaryWithAcceptHtml() {
		FhirHttpResponse status = ourServer.fhirRequest("/$binaryop")
			.withHeader(Constants.HEADER_ACCEPT, TEXT_HTML)
			.get();

		status.assertStatus(200);
		assertEquals("$binaryop", ourLastMethod);

		assertEquals("text/html", status.getHeader("Content-Type"));
		assertEquals("<html>TAGS</html>", status.getBody());
	}

	static class PatientProvider implements IResourceProvider {


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

	static class PlainProvider {


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
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
