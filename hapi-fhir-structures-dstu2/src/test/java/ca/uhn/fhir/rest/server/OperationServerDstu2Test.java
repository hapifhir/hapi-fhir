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
import ca.uhn.fhir.test.utilities.FhirHttpResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OperationServerDstu2Test {
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();

	private static IdDt ourLastId;
	private static String ourLastMethod;
	private static StringDt ourLastParam1;
	private static Patient ourLastParam2;
	private static List<StringDt> ourLastParam3;
	private static MoneyDt ourLastParamMoney1;
	private static UnsignedIntDt ourLastParamUnsignedInt1;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerDstu2Test.class);

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new PatientProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		.setDefaultPrettyPrint(false);

	@BeforeEach
	void before() {
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastParam3 = null;
		ourLastParamUnsignedInt1 = null;
		ourLastParamMoney1 = null;
		ourLastId = null;
		ourLastMethod = "";
	}

	@Test
	void testConformance() throws Exception {
		Conformance p = ourServer.getFhirClient().fetchConformance().ofType(Conformance.class).prettyPrint().execute();
		List<RestOperation> ops = p.getRest().get(0).getOperation();
		assertThat(ops.size()).isGreaterThan(1);
		assertNull(ops.get(0).getDefinition().getReference().getBaseUrl());
		assertThat(ops.get(0).getDefinition().getReference().getValue()).startsWith("OperationDefinition/");

		OperationDefinition def = ourServer.getFhirClient().read().resource(OperationDefinition.class).withId(ops.get(0).getDefinition().getReference()).execute();
		assertThat(def.getCode()).isNotBlank();

		List<String> opNames = toOpNames(ops);
		assertThat(opNames).containsSubsequence("OP_TYPE");

		assertEquals("OperationDefinition/Patient-t-OP_TYPE", ops.get(opNames.indexOf("OP_TYPE")).getDefinition().getReference().getValue());
	}

	/**
	 * See #380
	 */
	@Test
	void testOperationDefinition() {
		OperationDefinition def = ourServer.getFhirClient().read().resource(OperationDefinition.class).withId("OperationDefinition/Patient-t-OP_TYPE").execute();

		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(def));

//		@OperationParam(name="PARAM1") StringType theParam1,
//		@OperationParam(name="PARAM2") Patient theParam2,
//		@OperationParam(name="PARAM3", min=2, max=5) List<StringType> theParam3,
//		@OperationParam(name="PARAM4", min=1) List<StringType> theParam4,

		assertThat(def.getParameter()).hasSize(4);
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
	void testInstanceEverythingGet() {

		// Try with a GET
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$everything").get();

		status.assertStatus(200);

		assertEquals("instance $everything", ourLastMethod);
		assertThat(status.getBody()).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

	}

	@Test
	void testInstanceEverythingHapiClient() throws Exception {
		Parameters p = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdDt("Patient/123")).named("$everything").withParameters(new Parameters()).execute();
		Bundle b = (Bundle) p.getParameterFirstRep().getResource();

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

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
	void testOperationCantUseGetIfItIsntIdempotent() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").get();

		status.assertStatus(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(status.getBody()).contains("HTTP Method GET is not allowed");
	}

	@Test
	void testOperationWrongParameterType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new IntegerDt(123));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);
		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerDt but method expects type StringDt");
		ourLog.info(status.getBody());
	}

	@Test
	void testOperationOnInstance() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
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
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());

	}

	@Test
	void testOperationOnInstanceAndType_Type() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_INSTANCE_OR_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertNull(ourLastId);
		assertEquals("$OP_INSTANCE_OR_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnServer() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_SERVER", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnType() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_TYPE", ourLastMethod);

		Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, status.getBody());
		assertEquals("RET1", resp.getParameter().get(0).getName());
	}

	@Test
	void testOperationOnTypeReturnBundle() {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringDt("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE_RET_BUNDLE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive().booleanValue());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
		assertEquals("100", resp.getEntryFirstRep().getResponse().getStatus());
	}

	@Test
	void testOperationWithBundleProviderResponse() {
		FhirHttpResponse status = ourServer.fhirRequest("/$OP_INSTANCE_BUNDLE_PROVIDER?_pretty=true").get();

		status.assertStatus(200);
		ourLog.info(status.getBody());

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, status.getBody());
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
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		p.addParameter().setName("PARAM3").setValue(new StringDt("PARAM3val1"));
		p.addParameter().setName("PARAM3").setValue(new StringDt("PARAM3val2"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/$OP_SERVER_LIST_PARAM").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_SERVER_LIST_PARAM", ourLastMethod);
		assertEquals(true, ourLastParam2.getActive().booleanValue());
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
		p.addParameter().setName("PARAM1").setValue(new IntegerDt("123"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_PROFILE_DT").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(200);

		assertEquals("$OP_PROFILE_DT", ourLastMethod);
		assertEquals("123", ourLastParamUnsignedInt1.getValueAsString());
	}

	@Test
	void testOperationWithProfileDatatypeParams2() {
		Parameters p = new Parameters();
		MoneyDt money = new MoneyDt();
		money.setCode("CODE");
		money.setSystem("SYSTEM");
		money.setValue(123L);
		p.addParameter().setName("PARAM1").setValue(money);
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
		p.addParameter().setName("PARAM1").setValue(new IntegerDt("123"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML);

		status.assertStatus(400);

		ourLog.info("HTTP {} {}", status.getStatusCode(), status.getReasonPhrase());
		ourLog.info(status.getBody());

		assertThat(status.getBody()).contains("Request has parameter PARAM1 of type IntegerDt but method expects type StringDt");
	}

	@Test
	void testReadWithOperations() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/123").get();

		status.assertStatus(200);

		assertEquals("read", ourLastMethod);
	}

	@AfterAll
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
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
