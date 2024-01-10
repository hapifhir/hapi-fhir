package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OperationGenericServer2R4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationGenericServer2R4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String TEST_OPERATION_NAME = "operation-with-nested-resources";
	private static final String REQUESTED_PATIENT_ID = "requested-patient-id";
	private static final String REQUESTED_CONSENT_ID = "requested-consent-id";
	public static final String EXPORT_PARAM_NAME = "export";
	public static final String MATCH_PARAM_NAME = "match";
	public static final String SERVER_ID_PARAM_NAME = "serverId";
	public static final String PATIENT_SUB_PARAM_NAME = "patient";
	public static final String CONSENT_SUB_PARAM_NAME = "Consent";
	private static final String EXPORTED_PATIENT_ID = "exported-patient-id";
	private static final String EXPORTED_CONSENT_ID = "exported-consent-id";
	private static final String MATCH_SUB_PARAM_NAME = "match-sub-param-name";
	private static final String MATCH_SUB_PARAM_VALUE = "match-sub-param-value";
	private static final String SERVER_ID_PARAM_VALUE = "server-id-param-value";
	private static IdType ourLastId;
	private static Object ourLastParam1;
	private static Object ourLastParam2;
	private static Object ourLastParam3;
	private static Parameters ourLastResourceParam;
	private static Parameters ourExportParameters;
	private static Parameters ourMatchParameters;
	private static String ourServerId;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new HashMapResourceProvider<>(ourCtx, MolecularSequence.class))
		 .withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		 .setDefaultResponseEncoding(EncodingEnum.JSON)
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastParam3=null;
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
		ourServer.registerProvider(provider);

		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new CodeType("PARAM1val"));
		p.addParameter().setName("PARAM2").setValue(new Coding("sys", "val", "dis"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
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
	public void testResourceWithinParameters() throws Exception {

		@SuppressWarnings("unused")
		class NestedPatientProvider implements IResourceProvider {

			@Override
			public Class<Patient> getResourceType() {
				return Patient.class;
			}

			@Operation(name = TEST_OPERATION_NAME)
			public Parameters testOperation(
				 @OperationParam(name = EXPORT_PARAM_NAME, typeName = "Parameters") IAnyResource theExportParameters,
				 @OperationParam(name = MATCH_PARAM_NAME, typeName = "Parameters") IAnyResource theMatchParameters,
				 @OperationParam(name = SERVER_ID_PARAM_NAME, typeName = "string") IPrimitiveType<String> theServerId
			) {

				ourExportParameters = (Parameters) theExportParameters;
				ourMatchParameters = (Parameters) theMatchParameters;
				ourServerId = theServerId.getValueAsString();

				Patient requestExportPatient = (Patient)ourExportParameters.getParameter(PATIENT_SUB_PARAM_NAME).getResource();
				Consent requestMatchConsent = (Consent) ourExportParameters.getParameter(CONSENT_SUB_PARAM_NAME).getResource();

				Parameters retVal = new Parameters();
				retVal.addParameter().setName(REQUESTED_PATIENT_ID).setValue(new StringType(requestExportPatient.getId()));
				retVal.addParameter().setName(REQUESTED_CONSENT_ID).setValue(new StringType(requestMatchConsent.getId()));
				return retVal;
			}

		}

		NestedPatientProvider provider = new NestedPatientProvider();
		ourServer.registerProvider(provider);

		Parameters exportParameters = new Parameters();
		Patient exportedPatient = new Patient();
		exportedPatient.setId(EXPORTED_PATIENT_ID);
		Consent exportedConsent = new Consent();
		exportedConsent.setId(EXPORTED_CONSENT_ID);
		exportParameters.addParameter().setName(PATIENT_SUB_PARAM_NAME).setResource(exportedPatient);
		exportParameters.addParameter().setName(CONSENT_SUB_PARAM_NAME).setResource(exportedConsent);

		Parameters matchParameters = new Parameters();
		matchParameters.addParameter().setName(MATCH_SUB_PARAM_NAME).setValue(new CodeType(MATCH_SUB_PARAM_VALUE));

		Parameters topParameters = new Parameters();
		topParameters.addParameter().setName(EXPORT_PARAM_NAME).setResource(exportParameters);
		topParameters.addParameter().setName(MATCH_PARAM_NAME).setResource(matchParameters);
		topParameters.addParameter().setName(SERVER_ID_PARAM_NAME).setValue(new StringType(SERVER_ID_PARAM_VALUE));

		String requestString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(topParameters);
		ourLog.info("REQUEST: {}", requestString);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$" + TEST_OPERATION_NAME);
		httpPost.setEntity(new StringEntity(requestString, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("RESPONSE: {}", responseString);
			status.getEntity().getContent().close();

			// test response
			Parameters responseParameters = ourCtx.newJsonParser().parseResource(Parameters.class, responseString);

			Parameters.ParametersParameterComponent patientParameter = responseParameters.getParameter().get(0);
			assertEquals(REQUESTED_PATIENT_ID, patientParameter.getName());
			assertEquals("Patient/" + EXPORTED_PATIENT_ID, patientParameter.getValue().primitiveValue());
			Parameters.ParametersParameterComponent consentParameter = responseParameters.getParameter().get(1);
			assertEquals(REQUESTED_CONSENT_ID, consentParameter.getName());
			assertEquals("Consent/" + EXPORTED_CONSENT_ID, consentParameter.getValue().primitiveValue());

			// test captured statics

			Patient capturedPatientSubParam = (Patient) ourExportParameters.getParameter(PATIENT_SUB_PARAM_NAME).getResource();
			assertEquals("Patient/" + EXPORTED_PATIENT_ID, capturedPatientSubParam.getId());
			Consent capturedConsentSubParam = (Consent) ourExportParameters.getParameter(CONSENT_SUB_PARAM_NAME).getResource();
			assertEquals("Consent/" + EXPORTED_CONSENT_ID, capturedConsentSubParam.getId());
			assertEquals(MATCH_SUB_PARAM_VALUE, ourMatchParameters.getParameter(MATCH_SUB_PARAM_NAME).getValue().primitiveValue());
		}

	}

	@Test
	public void testDeclarativeTypedListParameters() throws Exception {

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
				@OperationParam(name = "PARAM1", typeName = "code", max = OperationParam.MAX_UNLIMITED) List<IPrimitiveType<String>> theParam1
			) {

				ourLastId = theId;
				ourLastParam1 = theParam1;
				ourLastResourceParam = (Parameters) theResourceParam;

				Parameters retVal = new Parameters();
				retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
				return retVal;
			}

		}

		PatientProvider provider = new PatientProvider();
		ourServer.registerProvider(provider);

		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new CodeType("PARAM1val"));
		p.addParameter().setName("PARAM1").setValue(new CodeType("PARAM1val2"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			status.getEntity().getContent().close();

			List<IPrimitiveType<String>> param1 = (List<IPrimitiveType<String>>) ourLastParam1;
			assertEquals(2, param1.size());
			assertEquals(CodeType.class, param1.get(0).getClass());
			assertEquals("PARAM1val", param1.get(0).getValue());
			assertEquals("PARAM1val2", param1.get(1).getValue());
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeclarativeStringTypedParameters() throws Exception {

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
				@OperationParam(name = "PARAM1", min = 1, typeName = "uri") IPrimitiveType<String> theParam1,
				@OperationParam(name = "PARAM2", min = 1, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theParam2,
				@OperationParam(name = "PARAM3", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "attachment") List<ICompositeType> theParam3
			) {

				ourLastId = theId;
				ourLastParam1 = theParam1;
				ourLastParam2 = theParam2;
				ourLastParam3 = theParam2;
				ourLastResourceParam = (Parameters) theResourceParam;

				Parameters retVal = new Parameters();
				retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
				return retVal;
			}

		}

		PatientProvider provider = new PatientProvider();
		ourServer.registerProvider(provider);

		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new UriType("PARAM1val"));
		p.addParameter().setName("PARAM2").setValue(new StringType("PARAM2val"));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			status.getEntity().getContent().close();

			UriType param1 = (UriType) ourLastParam1;
			assertEquals("PARAM1val", param1.getValue());

			List<StringType> param2 = (List<StringType>) ourLastParam2;
			assertEquals("PARAM2val", param2.get(0).getValue());
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
			ourServer.registerProvider(provider);
			fail();
		} catch (ConfigurationException e) {
			ConfigurationException ce = (ConfigurationException) e.getCause();
			assertThat(ce.getMessage(), containsString(Msg.code(405) + "Non assignable parameter typeName=\"code\" specified on method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test"));
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
		ourServer.registerProvider(provider);

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(200, status.getStatusLine().getStatusCode());
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
			ourServer.registerProvider(provider);
			fail();
		} catch (ConfigurationException e) {
			Throwable cause = e.getCause();
			assertEquals(Msg.code(423) +  "Failed to bind method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test$2PlainProvider.opInstance() - " + Msg.code(1684) + "Unknown resource name \"FOO\" (this name is not known in FHIR version \"R4\")", cause.getMessage());
		}
	}



	@AfterEach
	public void after() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
