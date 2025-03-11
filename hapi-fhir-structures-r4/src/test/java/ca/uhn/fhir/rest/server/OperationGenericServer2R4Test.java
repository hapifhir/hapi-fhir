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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class OperationGenericServer2R4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationGenericServer2R4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static IdType ourLastId;
	private static Object ourLastParam1;
	private static Object ourLastParam2;
	private static Object ourLastParam3;
	private static Parameters ourLastResourceParam;

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
			assertThat(param1).hasSize(2);
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
			fail();		} catch (ConfigurationException e) {
			ConfigurationException ce = (ConfigurationException) e.getCause();
			assertThat(ce.getMessage()).contains(Msg.code(405) + "Non assignable parameter typeName=\"code\" specified on method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test");
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
			fail();		} catch (ConfigurationException e) {
			Throwable cause = e.getCause();
			assertEquals(Msg.code(423) + "Failed to bind method public org.hl7.fhir.r4.model.Parameters ca.uhn.fhir.rest.server.OperationGenericServer2R4Test$2PlainProvider.opInstance() - " + Msg.code(1684) + "Unknown resource name \"FOO\" (this name is not known in FHIR version \"R4\")", cause.getMessage());
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
