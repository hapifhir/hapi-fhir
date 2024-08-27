package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AuthorizationInterceptorR4Test extends BaseValidationTestWithInlineMocks {

	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"" + Msg.code(334) + "Access denied by default policy (no applicable rules)\"}]}";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AuthorizationInterceptorR4Test.class);
	private static String ourConditionalCreateId;
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static boolean ourHitMethod;
	private static List<Resource> ourReturn;
	private static List<IBaseResource> ourDeleted;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyObservationResourceProvider())
		.registerProvider(new DummyOrganizationResourceProvider())
		.registerProvider(new DummyEncounterResourceProvider())
		.registerProvider(new DummyCarePlanResourceProvider())
		.registerProvider(new DummyDiagnosticReportResourceProvider())
		.registerProvider(new DummyDeviceResourceProvider())
		.registerProvider(new DummyServiceRequestResourceProvider())
		.registerProvider(new DummyConsentResourceProvider())
		.registerProvider(new PlainProvider())
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.withPagingProvider(new FifoMemoryPagingProvider(10))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourServer.getRestfulServer().setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourDeleted = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
	}

	@AfterEach
	public void after() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
	}

	private Resource createCarePlan(Integer theId, String theSubjectId) {
		CarePlan retVal = new CarePlan();
		if (theId != null) {
			retVal.setId(new IdType("CarePlan", (long) theId));
		}
		retVal.setSubject(new Reference("Patient/" + theSubjectId));
		return retVal;
	}

	private Resource createDiagnosticReport(Integer theId, String theSubjectId) {
		DiagnosticReport retVal = new DiagnosticReport();
		if (theId != null) {
			retVal.setId(new IdType("DiagnosticReport", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));
		return retVal;
	}

	private HttpEntity createFhirResourceEntity(IBaseResource theResource) {
		String out = ourCtx.newJsonParser().encodeResourceToString(theResource);
		return new StringEntity(out, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8"));
	}

	private Observation createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdType("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));

		if (theSubjectId != null && theSubjectId.startsWith("#")) {
			Patient p = new Patient();
			p.setId(theSubjectId);
			p.setActive(true);
			retVal.addContained(p);
		}

		return retVal;
	}

	private Organization createOrganization(int theIndex) {
		Organization retVal = new Organization();
		retVal.setId("" + theIndex);
		retVal.setName("Org " + theIndex);
		return retVal;
	}

	private Patient createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}

	private Patient createPatient(Integer theId, int theVersion) {
		Patient retVal = createPatient(theId);
		retVal.setId(retVal.getIdElement().withVersion(Integer.toString(theVersion)));
		return retVal;
	}

	private Bundle createTransactionWithPlaceholdersRequestBundle() {
		// Create a input that will be used as a transaction
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		String encounterId = "123-123";
		String encounterSystem = "http://our.internal.code.system/encounter";
		Encounter encounter = new Encounter();

		encounter.addIdentifier(new Identifier().setValue(encounterId)
			.setSystem(encounterSystem));

		encounter.setStatus(Encounter.EncounterStatus.FINISHED);

		Patient p = new Patient()
			.addIdentifier(new Identifier().setValue("321-321").setSystem("http://our.internal.code.system/patient"));
		p.setId(IdDt.newRandomUuid());

		// add patient to input so its created
		input.addEntry()
			.setFullUrl(p.getId())
			.setResource(p)
			.getRequest()
			.setUrl("Patient")
			.setMethod(Bundle.HTTPVerb.POST);

		Reference patientRef = new Reference(p.getId());

		encounter.setSubject(patientRef);
		Condition condition = new Condition()
			.setCode(new CodeableConcept().addCoding(
				new Coding("http://hl7.org/fhir/icd-10", "S53.40", "FOREARM SPRAIN / STRAIN")))
			.setSubject(patientRef);

		condition.setId(IdDt.newRandomUuid());

		// add condition to input so its created
		input.addEntry()
			.setFullUrl(condition.getId())
			.setResource(condition)
			.getRequest()
			.setUrl("Condition")
			.setMethod(Bundle.HTTPVerb.POST);

		Encounter.DiagnosisComponent dc = new Encounter.DiagnosisComponent();

		dc.setCondition(new Reference(condition.getId()));
		encounter.addDiagnosis(dc);
		CodeableConcept reason = new CodeableConcept();
		reason.setText("SLIPPED ON FLOOR,PAIN L) ELBOW");
		encounter.addReasonCode(reason);

		// add encounter to input so its created
		input.addEntry()
			.setResource(encounter)
			.getRequest()
			.setUrl("Encounter")
			.setIfNoneExist("identifier=" + encounterSystem + "|" + encounterId)
			.setMethod(Bundle.HTTPVerb.POST);
		return input;
	}

	private Bundle createTransactionWithPlaceholdersResponseBundle() {
		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry()
			.setResource(new Patient().setActive(true)) // don't give this an ID
			.getResponse().setLocation("/Patient/1");
		return output;
	}

	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		return responseContent;
	}

	@Test
	public void testAllowAll() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allowAll("Default Rule")
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}


	/**
	 * A GET to the base URL isn't valid, but the interceptor should allow it
	 */
	@Test
	public void testGetRoot() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allowAll()
					.build();
			}
		});

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(400, status.getStatusLine().getStatusCode());

	}


	@Test
	public void testAllowAllForTenant() throws Exception {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().forTenantIds("TENANTA").andThen()
					.allowAll("Default Rule")
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testCustomCompartmentSpsOnMultipleInstances() throws Exception {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				AdditionalCompartmentSearchParameters additionalCompartmentSearchParameters = new AdditionalCompartmentSearchParameters();
				additionalCompartmentSearchParameters.addSearchParameters("Device:patient");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				relatedIds.add(new IdType("Patient/456"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inCompartmentWithAdditionalSearchParams("Patient", relatedIds, additionalCompartmentSearchParameters)
					.andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		//When
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Device/124456");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);

		//Then
		assertTrue(ourHitMethod);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testCustomSearchParamsDontOverPermit() throws Exception {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				AdditionalCompartmentSearchParameters additionalCompartmentSearchParameters = new AdditionalCompartmentSearchParameters();
				additionalCompartmentSearchParameters.addSearchParameters("Encounter:patient");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inCompartmentWithAdditionalSearchParams("Patient", relatedIds, additionalCompartmentSearchParameters)
					.andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		//When
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Device/124456");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);

		//then
		assertFalse(ourHitMethod);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}


	@Test
	public void testNonsenseParametersThrowAtRuntime() throws Exception {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				AdditionalCompartmentSearchParameters additionalCompartmentSearchParameters = new AdditionalCompartmentSearchParameters();
				additionalCompartmentSearchParameters.addSearchParameters("device:garbage");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inCompartmentWithAdditionalSearchParams("Patient", relatedIds, additionalCompartmentSearchParameters)
					.andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		//When
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Device/124456");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);

		//then
		assertFalse(ourHitMethod);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testRuleBuilderAdditionalSearchParamsInvalidValues() {
		//Too many colons
		try {
			AdditionalCompartmentSearchParameters additionalCompartmentSearchParameters = new AdditionalCompartmentSearchParameters();
			additionalCompartmentSearchParameters.addSearchParameters("too:many:colons");
			new RuleBuilder()
				.allow().read().allResources()
				.inCompartmentWithAdditionalSearchParams("Patient", new IdType("Patient/123"), additionalCompartmentSearchParameters)
				.andThen().denyAll()
				.build();
			fail();		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(342) + "too:many:colons is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'", e.getMessage());
		}


		//No colons
		try {
			AdditionalCompartmentSearchParameters additionalCompartmentSearchParameters = new AdditionalCompartmentSearchParameters();
			additionalCompartmentSearchParameters.addSearchParameters("no-colons");
			new RuleBuilder()
				.allow().read().allResources()
				.inCompartmentWithAdditionalSearchParams("Patient", new IdType("Patient/123"), additionalCompartmentSearchParameters)
				.andThen().denyAll()
				.build();
			fail();		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(341) + "no-colons is not a valid search parameter. Search parameters must be in the form resourcetype:parametercode, e.g. 'Device:patient'", e.getMessage());
		}
	}

	@Test
	public void testAllowByCompartmentUsingUnqualifiedIds() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow().read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/123")).andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		Patient patient;
		CarePlan carePlan;

		// Unqualified
		patient = new Patient();
		patient.setId("123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Qualified
		patient = new Patient();
		patient.setId("Patient/123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong one
		patient = new Patient();
		patient.setId("456");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);


		patient = new Patient();
		patient.setId("Patient/123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);
	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithAnyType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a"))
					.andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testAllowByCompartmentWithAnyTypeWithTenantId() throws Exception {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).forTenantIds("TENANTA")
					.andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testBatchWhenOnlyTransactionAllowed() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionReadDenied() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(2));

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testCodeIn_Search_BanList() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Banned code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Both Unacceptable and Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}


	@Test
	public void testCodeIn_Search_AllowList() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Both Unacceptable and Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Allowed code present - Search
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// No acceptable code present - Search
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}


	@Test
	public void testCodeNotIn_AllowSearch() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Both Unacceptable and Acceptable code present - Should not pass since one of the codes is in the VS
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testCodeNotIn_DenySearch() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Both Unacceptable and Acceptable code present - Should not pass since one of the codes is in the VS
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// No acceptable codesystem present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://blah")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * Even if everything is allow, let's be safe and deny if the ValueSet can't be validated at all
	 */
	@Test
	public void testCodeNotIn_DenySearch_UnableToValidateValueSet() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://foo").andThen()
					.allowAll()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}


	@Test
	public void testCodeIn_TransactionCreate() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.deny("Rule 1").write().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		HttpPost httpPost;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input
			.addEntry()
			.setResource(observation)
			.getRequest()
			.setUrl("/Observation")
			.setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(1));

		// Transaction with resource containing banned code
		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Rule 1");
		assertEquals(403, status.getStatusLine().getStatusCode());

		// Transaction with resource containing acceptable code
		observation.getCode().getCoding().clear();
		observation.getCode().addCoding().setSystem("http://foo").setCode("bar");
		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testCodeIn_InvalidSearchParam() throws IOException {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("blah", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		HttpGet httpGet;
		String response;
		Observation observation;
		CloseableHttpResponse status;

		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(response).contains("HAPI-2025: Unknown SearchParameter for resource Observation: blah");
		assertTrue(ourHitMethod);
	}


	@Test
	public void testBatchWhenTransactionWrongBundleType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.COLLECTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(1));

		HttpPost httpPost;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(422, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testDeleteByCompartment() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDeleteByCompartmentUsingTransaction() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.build();
			}
		});

		HttpPost httpPost;
		HttpResponse status;
		String responseString;

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		ourHitMethod = false;
		bundle.getEntry().clear();
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(2));
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/2");
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(403);
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(1));
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(200);
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/2");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/2"));
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(403);
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/1"));
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(200);
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDeleteByType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).withAnyId().andThen()
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;
		String responseString;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpDelete);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(204);
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Observation/1");
		status = ourClient.execute(httpDelete);
		responseString = extractResponseAndClose(status);
		assertThat(status.getStatusLine().getStatusCode()).as(responseString).isEqualTo(403);
		assertFalse(ourHitMethod);


	}

	/**
	 * #528
	 */
	@Test
	public void testDenyActionsNotOnTenant() throws Exception {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().denyAll().notForTenantIds("TENANTA", "TENANTB").build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTC/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: (unnamed rule)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testDenyAll() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("Default Rule")
					.build();
			}

			@Override
			protected void handleDeny(Verdict decision) {
				// Make sure the toString() method on Verdict never fails
				ourLog.info("Denying with decision: {}", decision);
				super.handleDeny(decision);
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Default Rule");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Default Rule");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by rule: Default Rule");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testDenyAllByDefault() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.build();
			}

			@Override
			protected void handleDeny(Verdict decision) {
				// Make sure the toString() method on Verdict never fails
				ourLog.info("Denying with decision: {}", decision);
				super.handleDeny(decision);
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithAnyType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll().build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testHistoryWithReadAll() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().withAnyId()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2, 1));

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testInvalidInstanceIds() {
		try {
			new RuleBuilder().allow("Rule 1").write().instance((String) null);
			fail();		} catch (NullPointerException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("");
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("Observation/");
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType());
			fail();		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType(""));
			fail();		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType("Observation", (String) null));
			fail();		} catch (NullPointerException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
	}

	@Test
	public void testMetadataAllow() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").metadata()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testMetadataDeny() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").metadata()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOperationAnyName() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().withAnyName().onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationAppliesAtAnyLevel() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").atAnyLevel().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/_history/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationAppliesAtAnyLevelWrongOpName() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opNameBadOp").atAnyLevel().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/_history/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeAllowed() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Bundle");
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("OperationOutcome");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithInvalidReturnValue() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization().andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// With a return value we don't allow
		ourReturn = Collections.singletonList(createPatient(222));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("OperationOutcome");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		// With a return value we do
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Bundle");
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithReturnValue() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Bundle");
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("OperationOutcome");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	@Test
	public void testOperationInstanceLevel() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onInstance(new IdType("http://example.com/Patient/1/_history/2")).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationInstanceLevelAnyInstance() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onAnyInstance().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationNotAllowedWithWritePermissiom() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").write().allResources().withAnyId().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// System
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationServerLevel() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevel() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWildcard() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onAnyType().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWithOperationMethodHavingOptionalIdParam() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Organization.class).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Organization/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Organization/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Organization/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWithTenant() throws Exception {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andRequireExplicitResponseAuthorization().forTenantIds("TENANTA").andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Right Tenant
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong Tenant
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTC/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}


	@Test
	public void testOperationTypeLevelDifferentBodyType() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("process-message").onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		HttpPost httpPost;
		HttpResponse status;
		String response;

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.MESSAGE);
		String inputString = ourCtx.newJsonParser().encodeResourceToString(input);

		// With body
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/$process-message");
		httpPost.setEntity(new StringEntity(inputString, ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// With body
		ourHitMethod = false;
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/$process-message");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testOperationWithTester() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization().withTester(null /* null should be ignored */).withTester(new IAuthRuleTester() {
						@Override
						public boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
							return theInputResourceId.getIdPart().equals("1");
						}
					})
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Bundle");
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("OperationOutcome");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	// This test is of dubious value since it does NOT exercise DAO code.  It simply exercises the AuthorizationInterceptor.
	// In functional testing or with a more realistic integration test, this scenario, namely having ONLY a FHIR_PATCH
	// role, will result in a failure to update the resource.
	@Test
	public void testPatchAllowed() throws IOException {
		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/999"));

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().patch().allRequests().andThen()
					.build();
			}
		});

		String patchBody = "[\n" +
			"     { \"op\": \"replace\", \"path\": \"Observation/status\", \"value\": \"amended\" }\n" +
			"     ]";
		HttpPatch patch = new HttpPatch(ourServer.getBaseUrl() + "/Observation/123");
		patch.setEntity(new StringEntity(patchBody, ContentType.create(Constants.CT_JSON_PATCH, Charsets.UTF_8)));
		CloseableHttpResponse status = ourClient.execute(patch);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testPatchNotAllowed() throws IOException {
		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/999"));

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().metadata().andThen()
					.build();
			}
		});

		String patchBody = "[\n" +
			"     { \"op\": \"replace\", \"path\": \"Observation/status\", \"value\": \"amended\" }\n" +
			"     ]";
		HttpPatch patch = new HttpPatch(ourServer.getBaseUrl() + "/Observation/123");
		patch.setEntity(new StringEntity(patchBody, ContentType.create(Constants.CT_JSON_PATCH, Charsets.UTF_8)));
		CloseableHttpResponse status = ourClient.execute(patch);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testGraphQLAllowed() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").graphQL().any().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"));
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testGraphQLDenied() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"));
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByAnyId() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByAnyIdWithTenantId() throws Exception {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId().forTenantIds("TENANTA")
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTB/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANTA/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByAnyIdWithTester() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId().withTester(new IAuthRuleTester() {
						@Override
						public boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
							return theInputResourceId != null && theInputResourceId.getIdPart().equals("1");
						}
					})
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}


	@Test
	public void testReadByTypeWithAnyId() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(new Consent().setDateTime(new Date()).setId("Consent/123"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Consent");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(new ServiceRequest().setAuthoredOn(new Date()).setId("ServiceRequest/123"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/ServiceRequest");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertTrue(ourHitMethod);
		assertEquals(200, status.getStatusLine().getStatusCode());

	}


	@Test
	public void testReadByCompartmentReadByIdParam() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByCompartmentReadByPatientParam() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?patient=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?patient=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?patient=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?subject=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?subject=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/DiagnosticReport?subject=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentRight() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongAllTypesProactiveBlockEnabledNoResponse() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		}.setFlags());

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.emptyList();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/999/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabled() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createCarePlan(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response).contains("Access denied by default policy (no applicable rules)");
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabledNoResponse() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.emptyList();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockEnabledNoResponse() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags());

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.emptyList();
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// CarePlan could potentially be in the Patient/1 compartment but we don't
		// have any rules explicitly allowing CarePlan so it's blocked
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/999/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByCompartmentDoesntAllowContained() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags());

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Read with allowed subject
		ourReturn = Lists.newArrayList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Read with contained
		ourReturn = Lists.newArrayList(createObservation(10, "#1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Read with contained
		Observation obs = (Observation) createObservation(10, null);
		obs.setSubject(new Reference(new Patient().setActive(true)));
		ourReturn = Lists.newArrayList(obs);
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().instance("Observation/900").andThen()
					.allow("Rule 1").read().instance("901").andThen()
					.build();
			}
		});

		HttpResponse status;
		String response;
		HttpGet httpGet;

		ourReturn = Collections.singletonList(createObservation(900, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation/900");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(901));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/901");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=json");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByInstanceAllowsTargetedSearch() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				RuleBuilder ruleBuilder = new RuleBuilder();
				ruleBuilder.allow().read().instance("Patient/900").andThen();
				ruleBuilder.allow().read().instance("Patient/700").andThen();
				return ruleBuilder.build();
			}
		});

		HttpResponse status;
		String response;
		HttpGet httpGet;
		ourReturn = Collections.singletonList(createPatient(900));

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=900");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=Patient/900");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=901");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=Patient/901");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		// technically this is invalid, but just in case..
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation?_id=Patient/901");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation?_id=901");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=Patient/900,Patient/700");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=900,777");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadPageRight() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String respString;
		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			ourReturn.add(createObservation(i, "Patient/1"));
		}

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation?_count=5&_format=json&subject=Patient/1");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		httpGet = new HttpGet(respBundle.getLink("next").getUrl());
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/5", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNull(respBundle.getLink("next"));

	}

	@Test
	public void testReadPageWrong() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String respString;
		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ourReturn.add(createObservation(i, "Patient/1"));
		}
		for (int i = 5; i < 10; i++) {
			ourReturn.add(createObservation(i, "Patient/2"));
		}

		ourHitMethod = false;
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Observation?_count=5&_format=json&subject=Patient/1");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		String nextUrl = respBundle.getLink("next").getUrl();
		httpGet = new HttpGet(nextUrl);
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testTransactionWithSearch() throws IOException {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		// Request is a transaction with 1 search
		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		String patientId = "10000003857";
		Bundle.BundleEntryComponent bundleEntryComponent = requestBundle.addEntry();
		Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
		bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.GET);
		bundleEntryRequestComponent.setUrl(ResourceType.Patient + "?identifier=" + patientId);
		bundleEntryComponent.setRequest(bundleEntryRequestComponent);

		/*
		 * Response is a transaction response containing the search results
		 */
		Bundle searchResponseBundle = new Bundle();
		Patient patent = new Patient();
		patent.setActive(true);
		patent.setId("Patient/123");
		searchResponseBundle.addEntry().setResource(patent);

		Bundle responseBundle = new Bundle();
		responseBundle
			.addEntry()
			.setResource(searchResponseBundle);
		ourReturn = Collections.singletonList(responseBundle);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(requestBundle));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testTransactionWithNoBundleType() throws IOException {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		// Request is a transaction with 1 search
		Bundle requestBundle = new Bundle();
		String patientId = "10000003857";
		Bundle.BundleEntryComponent bundleEntryComponent = requestBundle.addEntry();
		Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
		bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.GET);
		bundleEntryRequestComponent.setUrl(ResourceType.Patient + "?identifier=" + patientId);
		bundleEntryComponent.setRequest(bundleEntryRequestComponent);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(requestBundle));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(resp).contains("Invalid request Bundle.type value for transaction: \\\"\\\"");

	}

	/**
	 * See #762
	 */
	@Test
	public void testTransactionWithPlaceholderIdsResponseAuthorized() throws IOException {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write patient").write().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write encounter").write().resourcesOfType(Encounter.class).withAnyId().andThen()
					.allow("write condition").write().resourcesOfType(Condition.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		Bundle input = createTransactionWithPlaceholdersRequestBundle();
		Bundle output = createTransactionWithPlaceholdersResponseBundle();

		ourReturn = Collections.singletonList(output);
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(resp);

	}

	/**
	 * See #762
	 */
	@Test
	public void testTransactionWithPlaceholderIdsResponseUnauthorized() throws IOException {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("write patient").write().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write encounter").write().resourcesOfType(Encounter.class).withAnyId().andThen()
					.allow("write condition").write().resourcesOfType(Condition.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		Bundle input = createTransactionWithPlaceholdersRequestBundle();
		Bundle output = createTransactionWithPlaceholdersResponseBundle();

		ourReturn = Collections.singletonList(output);
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());

		ourLog.info(resp);

	}

	@Test
	public void testTransactionWriteGood() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.PUT);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testWriteByCompartmentCreate() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 1b").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1123")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader("If-None-Exist", "Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentCreateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").createConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalWithoutDirectMatch() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class).andThen()
					.allow().delete().instance(new IdType("Patient/2")).andThen()
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;
		String response;

		// Wrong resource
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Right resource
		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDoesntAllowDelete() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdate() throws Exception {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		String response;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient/2");
		httpPost.setEntity(createFhirResourceEntity(createPatient(2)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient/1");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(99)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToInvalid() throws Exception {
		ourConditionalCreateId = "1123";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValidAllTypes() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().allResources()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertTrue(ourHitMethod);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);

	}

	@Test
	public void testWriteByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().instance("Observation/900").andThen()
					.allow("Rule 1").write().instance("901").andThen()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation/900");
		httpPost.setEntity(createFhirResourceEntity(createObservation(900, "Patient/12")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut(ourServer.getBaseUrl() + "/Observation/901");
		httpPost.setEntity(createFhirResourceEntity(createObservation(901, "Patient/12")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/900")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testWritePatchByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").patch().allRequests().andThen()
					.allow("Rule 1").write().instance("Patient/900").andThen()
					.build();
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		String input = "[ { \"op\": \"replace\", \"path\": \"/gender\", \"value\": \"male\" }  ]";

		ourHitMethod = false;
		httpPost = new HttpPatch(ourServer.getBaseUrl() + "/Patient/900");
		httpPost.setEntity(new StringEntity(input, ContentType.parse("application/json-patch+json")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingDocumentBundles_onlyRecursesOneLevelDeep() {
		Patient patient = createPatient(1);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(new Composition());
		bundle.addEntry().setResource(patient);
		Bundle searchSet = new Bundle();
		searchSet.setType(Bundle.BundleType.SEARCHSET);
		searchSet.addEntry().setResource(bundle);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Bundle");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(searchSet, ourCtx);
		assertEquals(1, resources.size());
		assertTrue(resources.contains(bundle));
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingPatients_returnsPatients() {
		Patient patient1 = createPatient(1);
		Patient patient2 = createPatient(2);
		Bundle searchSet = new Bundle();
		searchSet.setType(Bundle.BundleType.SEARCHSET);
		searchSet.addEntry().setResource(patient1);
		searchSet.addEntry().setResource(patient2);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Patient");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(searchSet, ourCtx);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(patient1));
		assertTrue(resources.contains(patient2));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "MESSAGE"})
	public void testShouldExamineBundleResources_withBundleRequestAndStandAloneBundleType_returnsFalse(Bundle.BundleType theBundleType) {
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);
		assertFalse(AuthorizationInterceptor.shouldExamineChildResources(bundle, ourCtx));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "MESSAGE"}, mode= EnumSource.Mode.EXCLUDE)
	public void testShouldExamineBundleResources_withBundleRequestAndNonStandAloneBundleType_returnsTrue(Bundle.BundleType theBundleType) {
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);
		assertTrue(AuthorizationInterceptor.shouldExamineChildResources(bundle, ourCtx));
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyCarePlanResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CarePlan.class;
		}

		@Read(version = true)
		public CarePlan read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (CarePlan) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			markHitMethod();
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyEncounterResourceProvider implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			markHitMethod();
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}
	}

	public static class DummyOrganizationResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Organization.class;
		}

		/**
		 * This should come before operation1
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam(optional = true) IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

	}

	public static class DummyDiagnosticReportResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return DiagnosticReport.class;
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "subject") ReferenceParam theSubject,
			@OptionalParam(name = "patient") ReferenceParam thePatient
		) {
			markHitMethod();
			return ourReturn;
		}
	}

	public static class DummyDeviceResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Device.class;
		}

		@Read(version = true)
		public Device read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Device) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "patient") ReferenceParam thePatient
		) {
			markHitMethod();
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl) {
			markHitMethod();
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdType theId) {
			markHitMethod();
			return new MethodOutcome();
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		/**
		 * This should come before operation1
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * This should come after operation0
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Patch
		public MethodOutcome patch(@IdParam IdType theId, PatchTypeEnum thePatchType, @ResourceParam String theBody) {
			markHitMethod();
			return new MethodOutcome().setId(theId.withVersion("2"));
		}

		@Read(version = true)
		public Observation read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIds,
			@OptionalParam(name = "subject") ReferenceParam theSubject) {
			markHitMethod();
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Observation", ourConditionalCreateId);
				theResource.setId(actual);
			} else {
				theResource.setId(theId.withVersion("2"));
			}

			{
				HookParams params = new HookParams();
				params.add(IBaseResource.class, theResource);
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(TransactionDetails.class, new TransactionDetails());
				params.add(RequestPartitionId.class, RequestPartitionId.defaultPartition());
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, params);
			}

			{
				HookParams params = new HookParams();
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

	}

	public static class DummyServiceRequestResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ServiceRequest.class;
		}

		@Search
		public List<Resource> search() {
			assert ourReturn != null;
			markHitMethod();
			return ourReturn;
		}

	}

	public static class DummyConsentResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Consent.class;
		}

		@Search
		public List<Resource> search() {
			assert ourReturn != null;
			markHitMethod();
			return ourReturn;
		}

	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {

			markHitMethod();
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);

			HookParams params = new HookParams();
			params.add(RequestDetails.class, theRequestDetails);
			params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
			ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);

			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IInterceptorBroadcaster theRequestOperationCallback, @IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();
			for (IBaseResource next : ourReturn) {
				HookParams params = new HookParams()
					.add(IBaseResource.class, next)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(TransactionDetails.class, new TransactionDetails());
				theRequestOperationCallback.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
			}
			return new MethodOutcome();
		}

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			markHitMethod();
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History()
		public List<Resource> history() {
			markHitMethod();
			return (ourReturn);
		}

		@History()
		public List<Resource> history(@IdParam IdType theId) {
			markHitMethod();
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * More generic method second to make sure that the
		 * other method takes precedence
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Patch()
		public MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theResource, PatchTypeEnum thePatchType) {
			markHitMethod();

			return new MethodOutcome();
		}

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(@OptionalParam(name = "_id") TokenAndListParam theIdParam) {
			markHitMethod();
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				theResource.setId(actual);
			} else {
				theResource.setId(theId.withVersion("2"));
			}

			{
				HookParams params = new HookParams();
				params.add(IBaseResource.class, theResource);
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(TransactionDetails.class, new TransactionDetails());
				params.add(RequestPartitionId.class, RequestPartitionId.defaultPartition());
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, params);
			}
			{
				HookParams params = new HookParams();
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding,
												@Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			markHitMethod();
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
												@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			markHitMethod();
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

	}

	private static void markHitMethod() {
		ourHitMethod = true;
	}

	public static class PlainProvider {

		@History()
		public List<Resource> history() {
			markHitMethod();
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "process-message", idempotent = true)
		public Parameters processMessage(@OperationParam(name = "content") Bundle theInput) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@GraphQL
		public String processGraphQlRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQuery) {
			markHitMethod();
			return "{'foo':'bar'}";
		}

		@Transaction()
		public Bundle search(ServletRequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, @TransactionParam Bundle theInput) {
			markHitMethod();
			if (ourDeleted != null) {
				for (IBaseResource next : ourDeleted) {
					HookParams params = new HookParams()
						.add(IBaseResource.class, next)
						.add(RequestDetails.class, theRequestDetails)
						.add(ServletRequestDetails.class, theRequestDetails)
						.add(TransactionDetails.class, new TransactionDetails());
					theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
				}
			}
			return (Bundle) ourReturn.get(0);
		}

	}


}
