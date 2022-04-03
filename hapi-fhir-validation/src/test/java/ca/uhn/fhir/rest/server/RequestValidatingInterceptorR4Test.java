package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.ResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class RequestValidatingInterceptorR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorR4Test.class);
	@RegisterExtension
	static HttpClientExtension ourClient = new HttpClientExtension();
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	@Order(0)
	static RestfulServerExtension ourServlet = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	@Order(1)
	static ResourceProviderExtension<PatientProvider> ourProvider = new ResourceProviderExtension<>(ourServlet, new PatientProvider());
	private static boolean ourLastRequestWasSearch;
	private static int ourPort;
	private RequestValidatingInterceptor myInterceptor;

	@BeforeEach
	public void before() {
		ourProvider.getProvider().ourLastGraphQlQueryGet = null;
		ourProvider.getProvider().ourLastGraphQlQueryPost = null;
		ourLastRequestWasSearch = false;
		ourServlet.unregisterAllInterceptors();

		myInterceptor = new RequestValidatingInterceptor();
		//		myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		//		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		//		myInterceptor.setResponseHeaderName("X-RESP");
		//		myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServlet.registerInterceptor(myInterceptor);
		ourPort = ourServlet.getPort();
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Request-Validation"));
		assertThat(responseContent, not(containsString("<severity value=\"error\"/>")));
	}

	@Test
	public void testGraphQlRequestResponse_GET() throws IOException {
		HttpGet request = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"));

		try (CloseableHttpResponse status = ourClient.getClient().execute(request)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);

			ourLog.info("Response was:\n{}", status);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("{\"name\":{\"family\": \"foo\"}}", responseContent);
			assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryGet);
		}

	}

	@Test
	public void testGraphQlRequestResponse_POST() throws IOException {
		HttpPost request = new HttpPost("http://localhost:" + ourPort + "/Patient/123/$graphql");
		request.setEntity(new StringEntity("{\"query\": \"{name}\"}", ContentType.APPLICATION_JSON));

		try (CloseableHttpResponse status = ourClient.getClient().execute(request)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);

			ourLog.info("Response was:\n{}", status);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("{\"name\":{\"family\": \"foo\"}}", responseContent);
			assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryPost);
		}

	}

	@Test
	public void testCreateJsonInvalidNoValidatorsSpecified() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Request-Validation"));
		assertThat(responseContent, containsString("\"severity\": \"error\""));
	}

	@Test
	public void testCreateJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
	}

	@Test
	public void testCreateJsonValidNoValidatorsSpecifiedDefaultMessage() throws Exception {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-FHIR-Request-Validation: NO ISSUES")));
	}

	@Test
	public void testValidateXmlPayloadWithXxeDirective_InstanceValidator() throws IOException {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);

		String encoded =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
				"<!DOCTYPE foo [  \n" +
				"<!ELEMENT foo ANY >\n" +
				"<!ENTITY xxe SYSTEM \"file:///etc/passwd\" >]>" +
				"<Patient xmlns=\"http://hl7.org/fhir\">" +
				"<text>" +
				"<status value=\"generated\"/>" +
				"<div xmlns=\"http://www.w3.org/1999/xhtml\">TEXT &xxe; TEXT</div>\n" +
				"</text>" +
				"<address>" +
				"<line value=\"FOO\"/>" +
				"</address>" +
				"</Patient>";

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		try (CloseableHttpResponse status = ourClient.getClient().execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);

			ourLog.info("Response was:\n{}", status);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(422, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("DOCTYPE"));
		}

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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		try (CloseableHttpResponse status = ourClient.getClient().execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);

			ourLog.info("Response was:\n{}", status);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(422, status.getStatusLine().getStatusCode());
			assertThat(status.toString(), containsString("X-FHIR-Request-Validation"));
		}
	}

	@Test
	public void testCreateXmlInvalidNoValidatorsSpecified() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Request-Validation"));
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Request-Validation: {\"resourceType\":\"OperationOutcome"));
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>"));
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<diagnostics value=\"FOO\"/>"));
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
	}

	@Test
	public void testCreateXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
	}

	/**
	 * Test for #345
	 */
	@Test
	public void testDelete() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpDelete httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/123");

		CloseableHttpResponse status = ourClient.getClient().execute(httpDelete);
		try {
			ourLog.info("Response was:\n{}", status);

			assertEquals(204, status.getStatusLine().getStatusCode());
			assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testFetchMetadata() throws Exception {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");

		// This header caused a crash
		httpGet.addHeader("Content-Type", "application/xml+fhir");

		HttpResponse status = ourClient.getClient().execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("CapabilityStatement"));
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.getClient().execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Request-Validation")));
		assertEquals(true, ourLastRequestWasSearch);
	}

	public static class PatientProvider implements IResourceProvider {

		public String ourLastGraphQlQueryGet;
		public String ourLastGraphQlQueryPost;

		private IBaseResource myReturnResource;

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return new MethodOutcome(theId.withVersion("2"));
		}

		@GraphQL(type = RequestTypeEnum.GET)
		public String graphQLGet(@IdParam IIdType theId, @GraphQLQueryUrl String theQueryUrl) {
			ourLastGraphQlQueryGet = theQueryUrl;
			return "{\"name\":{\"family\": \"foo\"}}";
		}

		@GraphQL(type = RequestTypeEnum.POST)
		public String graphQLPost(@IdParam IIdType theId, @GraphQLQueryBody String theQueryUrl) {
			ourLastGraphQlQueryPost = theQueryUrl;
			return "{\"name\":{\"family\": \"foo\"}}";
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		public void setReturnResource(IBaseResource theReturnResource) {
			myReturnResource = theReturnResource;
		}

		@Search
		public ArrayList<IBaseResource> search(@OptionalParam(name = "foo") StringParam theString) {
			ourLastRequestWasSearch = true;
			ArrayList<IBaseResource> retVal = new ArrayList<>();
			if (myReturnResource != null) {
				myReturnResource.setId("1");
				retVal.add(myReturnResource);
				myReturnResource = null;
			}
			return retVal;
		}


	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
