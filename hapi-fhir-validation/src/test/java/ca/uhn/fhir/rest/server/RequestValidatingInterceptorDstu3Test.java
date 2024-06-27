package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RequestValidatingInterceptorDstu3Test extends BaseValidationTestWithInlineMocks {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorDstu3Test.class);
	private RequestValidatingInterceptor myInterceptor;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastRequestWasSearch = false;
		ourServer.getInterceptorService().unregisterAllInterceptors();

		myInterceptor = new RequestValidatingInterceptor();
		//		myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		//		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		//		myInterceptor.setResponseHeaderName("X-RESP");
		//		myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServer.registerInterceptor(myInterceptor);
	}

	@Test
	public void testCreateJsonInvalidNoFailure() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
		assertThat(responseContent).doesNotContain("<severity value=\"error\"/>");
	}

	@Test
	public void testCreateJsonInvalidNoValidatorsSpecified() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
		assertThat(responseContent).contains("\"severity\":\"error\"");
	}

	@Test
	public void testCreateJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@Test
	public void testCreateJsonValidNoValidatorsSpecifiedDefaultMessage() throws Exception {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString().contains("X-FHIR-Request-Validation: NO ISSUES"));
	}

	@Test
	public void testCreateXmlInvalidInstanceValidator() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
	}

	@Test
	public void testCreateXmlInvalidNoValidatorsSpecified() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
	}

	@Test
	public void testCreateXmlInvalidNoValidatorsSpecifiedOutcomeHeader() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Request-Validation: {\"resourceType\":\"OperationOutcome");
	}


	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionNpeNoIgnore() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new NullPointerException("SOME MESSAGE")).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionNpeIgnore() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(NullPointerException.class).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionIseNoIgnore() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new InternalErrorException("FOO")).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"FOO\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionIseIgnore() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(InternalErrorException.class).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@Test
	public void testCreateXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	/**
	 * Test for #345
	 */
	@Test
	public void testDelete() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpDelete httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/123");

		CloseableHttpResponse status = ourClient.execute(httpDelete);
		try {
			ourLog.info("Response was:\n{}", status);

			assertEquals(204, status.getStatusLine().getStatusCode());
			assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testFetchMetadata() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata");

		// This header caused a crash
		httpGet.addHeader("Content-Type", "application/xml+fhir");

		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("CapabilityStatement");
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
		assertEquals(true, ourLastRequestWasSearch);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return new MethodOutcome(theId.withVersion("2"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name = "foo") StringParam theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<IResource>();
		}

	}

}
