package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Money;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OperationServerDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	private static IdType ourLastId;
	private static String ourLastMethod;
	private static StringType ourLastParam1;
	private static Patient ourLastParam2;
	private static List<StringType> ourLastParam3;
	private static Money ourLastParamMoney1;
	private static UnsignedIntType ourLastParamUnsignedInt1;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerDstu3Test.class);
	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .registerProvider(new PlainProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		 .setDefaultPrettyPrint(false);

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

		myFhirClient = ourServer.getFhirClient();
	}


	@Test
	void testConformance() throws Exception {
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogResponseBody(true);
		myFhirClient.registerInterceptor(loggingInterceptor);

		CapabilityStatement p = myFhirClient.fetchConformance().ofType(CapabilityStatement.class).prettyPrint().execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));

		List<CapabilityStatementRestOperationComponent> ops = p.getRest().get(0).getOperation();
		assertThat(ops.size()).isGreaterThan(1);

		List<String> opNames = toOpNames(ops);
		assertThat(opNames).contains("OP_TYPE");

//		OperationDefinition def = (OperationDefinition) ops.get(opNames.indexOf("OP_TYPE")).getDefinition().getResource();
		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId(ops.get(opNames.indexOf("OP_TYPE")).getDefinition().getReferenceElement()).execute();
		assertEquals("OP_TYPE", def.getCode());
	}

	/**
	 * See #380
	 */
	@Test
	void testOperationDefinition() {
		OperationDefinition def = myFhirClient.read().resource(OperationDefinition.class).withId("OperationDefinition/Patient-t-OP_TYPE").execute();

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

	private List<String> toOpNames(List<CapabilityStatementRestOperationComponent> theOps) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CapabilityStatementRestOperationComponent next : theOps) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	@Test
	void testInstanceEverythingGet() {

		// Try with a GET
		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$everything").get();

		status.assertStatus(200);

		assertEquals("instance $everything", ourLastMethod);
		assertThat(status.getBody()).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	void testInstanceEverythingHapiClient() throws Exception {
		ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdType("Patient/123")).named("$everything").withParameters(new Parameters()).execute();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());


	}

	@Test
	void testInstanceEverythingPost() {
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

		// Try with a POST
		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$everything").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("instance $everything", ourLastMethod);
		assertThat(status.getBody()).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	void testOperationCantUseGetIfItIsntIdempotent() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").get();

		status.assertStatus(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(status.getBody()).contains("HTTP Method GET is not allowed");
	}

	@Test
	void testOperationWrongParameterType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerType(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);
		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
		ourLog.info(status.getBody());
	}

	@Test
	void testOperationOnInstance() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());

		/*
		 * Against type should fail
		 */

		status = ourServer.fhirRequest("/Patient/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);

		ourLog.info(status.getBody());
		status.assertStatus(400);

	}

	@Test
	void testOperationOnInstanceAndType_Instance() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

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
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

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
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/$OP_SERVER").post(inParamsStr, Constants.CT_FHIR_XML);

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
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

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
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE_RET_BUNDLE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
		assertEquals("100", resp.getEntryFirstRep().getResponse().getStatus());
	}

	@Test
	void testOperationWithBundleProviderResponse() {
		HttpTestResponse status = ourServer.fhirRequest("/$OP_INSTANCE_BUNDLE_PROVIDER?_pretty=true").get();

		status.assertStatus(200);
		ourLog.info(status.getBody());

		ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
	}

	@Test
	void testOperationWithGetUsingParams() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val").get();

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertNull(ourLastParam2);
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationWithGetUsingParamsFailsWithNonPrimitive() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo").get();

		status.assertStatus(405);

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(status.getBody()).contains("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype");
	}


	@Test
	void testOperationWithListParam() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val1"));
		p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val2"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/$OP_SERVER_LIST_PARAM").post(inParamsStr, Constants.CT_FHIR_XML);

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
		p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	void testOperationWithProfileDatatypeParams2() {
		Parameters p = new Parameters();
		Money money = new Money();
		money.setCode("CODE");
		money.setSystem("SYSTEM");
		money.setValue(123L);
		p.addParameter().setName("PARAM1").setValue(money);
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT2").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT2", ourLastMethod);
		assertEquals("CODE", ourLastParamMoney1.getCode());
		assertEquals("SYSTEM", ourLastParamMoney1.getSystem());
		assertEquals("123", ourLastParamMoney1.getValue().toString());
	}

	@Test
	void testOperationWithProfileDatatypeUrl() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT?PARAM1=123").get();

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	void testOperationWrongParamType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(400);

		ourLog.info("HTTP {} {}", status.getStatusCode(), status.getReasonPhrase());
		ourLog.info(status.getBody());

		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
	}

	@Test
	void testReadWithOperations() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient/123").get();

		status.assertStatus(200);

		assertEquals("read", ourLastMethod);
	}


	@AfterAll
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@Operation(name="$OP_INSTANCE")
		public Parameters opInstance(
				@IdParam IdType theId,
				@OperationParam(name="PARAM1") StringType theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_INSTANCE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_INSTANCE_OR_TYPE")
		public Parameters opInstanceOrType(
				@IdParam(optional=true) IdType theId,
				@OperationParam(name="PARAM1") StringType theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_INSTANCE_OR_TYPE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_PROFILE_DT2", idempotent=true)
		public Bundle opProfileType(
				@OperationParam(name="PARAM1") Money theParam1
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
		public Bundle opProfileType(
				@OperationParam(name="PARAM1") UnsignedIntType theParam1
				) {
			//@formatter:on

			ourLastMethod = "$OP_PROFILE_DT";
			ourLastParamUnsignedInt1 = theParam1;

			Bundle retVal = new Bundle();
			retVal.addEntry().getResponse().setStatus("100");
			return retVal;
		}

		//@formatter:off
		@SuppressWarnings("unused")
		@Operation(name="$OP_TYPE", idempotent=true)
		public Parameters opType(
				@OperationParam(name="PARAM1") StringType theParam1,
				@OperationParam(name="PARAM2") Patient theParam2,
				@OperationParam(name="PARAM3", min=2, max=5) List<StringType> theParam3,
				@OperationParam(name="PARAM4", min=1) List<StringType> theParam4
				) {
			//@formatter:on

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_TYPE_ONLY_STRING", idempotent=true)
		public Parameters opTypeOnlyString(
				@OperationParam(name="PARAM1") StringType theParam1
				) {
			//@formatter:on

			ourLastMethod = "$OP_TYPE_ONLY_STRING";
			ourLastParam1 = theParam1;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_TYPE_RET_BUNDLE")
		public Bundle opTypeRetBundle(
				@OperationParam(name="PARAM1") StringType theParam1,
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

		@Operation(name = "$everything", idempotent=true)
		public Bundle patientEverything(@IdParam IdType thePatientId) {
			ourLastMethod = "instance $everything";
			ourLastId = thePatientId;
			return new Bundle();
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

		//@formatter:off
		@Operation(name="$OP_INSTANCE_BUNDLE_PROVIDER", idempotent=true)
		public IBundleProvider opInstanceReturnsBundleProvider() {
			ourLastMethod = "$OP_INSTANCE_BUNDLE_PROVIDER";

			List<IBaseResource> resources = new ArrayList<IBaseResource>();
			for (int i =0; i < 100;i++) {
				Patient p = new Patient();
				p.setId("Patient/" + i);
				p.addName().setFamily("Patient " + i);
				resources.add(p);
			}

			return new SimpleBundleProvider(resources);
		}

		//@formatter:off
		@Operation(name="$OP_SERVER")
		public Parameters opServer(
				@OperationParam(name="PARAM1") StringType theParam1,
				@OperationParam(name="PARAM2") Patient theParam2
				) {
			//@formatter:on

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		//@formatter:off
		@Operation(name="$OP_SERVER_LIST_PARAM")
		public Parameters opServerListParam(
				@OperationParam(name="PARAM2") Patient theParam2,
				@OperationParam(name="PARAM3") List<StringType> theParam3
				) {
			//@formatter:on

			ourLastMethod = "$OP_SERVER_LIST_PARAM";
			ourLastParam2 = theParam2;
			ourLastParam3 = theParam3;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

	}

}
