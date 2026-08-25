package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.ResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestValidatingInterceptorR4Test extends BaseValidationTestWithInlineMocks {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	@Order(0)
	static RestfulServerExtension ourServlet = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	@Order(1)
	static ResourceProviderExtension<PatientProvider> ourProvider = new ResourceProviderExtension<>(ourServlet, new PatientProvider());
	private static boolean ourLastRequestWasSearch;
	private RequestValidatingInterceptor myInterceptor;

	@BeforeEach
	void before() {
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
	}

	@Test
	void testCreateResource_whenResourceHasReference_willValidateRefWithRequestDetails() throws IOException {
		ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

		FhirValidator fhirValidator = createFhirValidatorWithReferenceChecking(argumentCaptor);

		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myInterceptor.setValidator(fhirValidator);

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference("Organization/123"));
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		assertThat(argumentCaptor.getValue()).isInstanceOf(RequestDetails.class);
	}

	@Test
	void testCreateJsonInvalidNoFailure() {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
		assertThat(status.getBody()).doesNotContain("<severity value=\"error\"/>");
	}

	@Test
	void testGraphQlRequestResponse_GET() {
		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name}")).get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertEquals("{\"name\":{\"family\": \"foo\"}}", status.getBody());
		assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryGet);
	}

	@Test
	void testGraphQlRequestResponse_POST() {
		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123/$graphql").post("{\"query\": \"{name}\"}", Constants.CT_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertEquals("{\"name\":{\"family\": \"foo\"}}", status.getBody());
		assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryPost);
	}

	@Test
	void testCreateJsonInvalidNoValidatorsSpecified() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
		assertThat(status.getBody()).contains("\"severity\": \"error\"");
	}

	@Test
	void testCreateJsonValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@Test
	void testCreateJsonValidNoValidatorsSpecifiedDefaultMessage() {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString().contains("X-FHIR-Request-Validation: NO ISSUES"));
	}

	@Test
	void testValidateXmlPayloadWithXxeDirective_InstanceValidator() {
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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getBody()).contains("DOCTYPE");
	}

	@Test
	void testCreateXmlInvalidInstanceValidator() {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
	}

	@Test
	void testCreateXmlInvalidNoValidatorsSpecified() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.toString()).contains("X-FHIR-Request-Validation");
	}

	@Test
	void testCreateXmlInvalidNoValidatorsSpecifiedOutcomeHeader() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).contains("X-FHIR-Request-Validation: {\"resourceType\":\"OperationOutcome");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeNoIgnore() {
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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeIgnore() {
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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseNoIgnore() {
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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"FOO\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseIgnore() {
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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@Test
	void testCreateXmlValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	/**
	 * Test for #345
	 */
	@Test
	void testDelete() {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123").delete();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(204);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
	}

	@Test
	void testFetchMetadata() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		// This header caused a crash
		HttpTestResponse status = ourServlet.fhirRequest("/metadata")
			.withHeader("Content-Type", "application/xml+fhir")
			.get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getBody()).contains("CapabilityStatement");
	}

	@Test
	void testSearch() {
		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.toString()).doesNotContain("X-FHIR-Request-Validation");
		assertEquals(true, ourLastRequestWasSearch);
	}

	private FhirValidator createFhirValidatorWithReferenceChecking(ArgumentCaptor<Object> theArgumentCaptor) throws IOException {
		IValidatorResourceFetcher validatorResourceFetcher = mock(IValidatorResourceFetcher.class);
		IValidationPolicyAdvisor policyAdvisor = mock(IValidationPolicyAdvisor.class);

		FhirValidator fhirValidator = ourCtx.newValidator();
		FhirInstanceValidator instanceValidatorModule = new FhirInstanceValidator(ourCtx);
		instanceValidatorModule.setValidatorResourceFetcher(validatorResourceFetcher);
		instanceValidatorModule.setValidatorPolicyAdvisor(policyAdvisor);
		fhirValidator.registerValidatorModule(instanceValidatorModule);

		when(validatorResourceFetcher.fetch(any(), theArgumentCaptor.capture(), anyString())).thenReturn(new Element("Organization").setType("Organization"));
		when(policyAdvisor.policyForReference(any(), any(), any(), any(), any())).thenReturn(ReferenceValidationPolicy.CHECK_EXISTS);
		when(policyAdvisor.policyForElement(any(), any(), any(), any(), any())).thenReturn(EnumSet.allOf(IValidationPolicyAdvisor.ElementValidationAction.class));

		return fhirValidator;
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
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
