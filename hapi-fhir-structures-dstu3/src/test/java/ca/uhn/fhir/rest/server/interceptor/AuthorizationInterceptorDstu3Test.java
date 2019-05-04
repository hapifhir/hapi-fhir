package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.*;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class AuthorizationInterceptorDstu3Test {

	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"Access denied by default policy (no applicable rules)\"}]}";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AuthorizationInterceptorDstu3Test.class);
	private static CloseableHttpClient ourClient;
	private static String ourConditionalCreateId;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static boolean ourHitMethod;
	private static int ourPort;
	private static List<Resource> ourReturn;
	private static List<IBaseResource> ourDeleted;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServlet.getInterceptorService().unregisterAllInterceptors();
		ourServlet.setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourDeleted = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
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

	private Resource createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdType("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));
		return retVal;
	}

	private Organization createOrganization(int theIndex) {
		Organization retVal = new Organization();
		retVal.setId("" + theIndex);
		retVal.setName("Org " + theIndex);
		return retVal;
	}

	private Resource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}

	private Resource createPatient(Integer theId, int theVersion) {
		Resource retVal = createPatient(theId);
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
		encounter.addReason(reason);

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
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Rule 1"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testAllowAllForTenant() throws Exception {
		ourServlet.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Rule 1"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testAllowByCompartmentUsingUnqualifiedIds() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithAnyType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testAllowByCompartmentWithAnyTypeWithTenantId() throws Exception {
		ourServlet.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/CarePlan/135154");
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
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testBatchWhenOnlyTransactionAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionReadDenied() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionWrongBundleType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(422, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testDeleteByCompartment() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDeleteByCompartmentUsingTransaction() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		ourHitMethod = false;
		Bundle bundle = new Bundle();
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(2));
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/2");
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertEquals(responseString, 403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(1));
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertEquals(responseString, 200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/2");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/2"));
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertEquals(responseString, 403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/1"));
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(bundle), ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		responseString = extractResponseAndClose(status);
		assertEquals(responseString, 200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyActionsNotOnTenant() throws Exception {
		ourServlet.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTC/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: (unnamed rule)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testDenyAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testDenyAllByDefault() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithAnyType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll().build();
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
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
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/135154");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testHistoryWithReadAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testInvalidInstanceIds() {
		try {
			new RuleBuilder().allow("Rule 1").write().instance((String) null);
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("Observation/");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType());
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType(""));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType("Observation", (String) null));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
	}

	@Test
	public void testMetadataAllow() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testMetadataDeny() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOperationAnyName() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationAppliesAtAnyLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationAppliesAtAnyLevelWrongOpName() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithInvalidReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		// With a return value we do
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

	}

	@Test
	public void testOperationByInstanceOfTypeWithReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	@Test
	public void testOperationInstanceLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationInstanceLevelAnyInstance() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationNotAllowedWithWritePermissiom() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// System
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationServerLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWildcard() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Another type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWithOperationMethodHavingOptionalIdParam() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevelWithTenant() throws Exception {
		ourServlet.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong Tenant
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTC/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}


	@Test
	public void testOperationTypeLevelDifferentBodyType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/$process-message");
		httpPost.setEntity(new StringEntity(inputString, ContentType.create(Constants.CT_FHIR_JSON_NEW, Charsets.UTF_8)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// With body
		ourHitMethod = false;
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$process-message");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testOperationWithTester() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization().withTester(new IAuthRuleTester() {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	@Test
	public void testPatchAllowed() throws IOException {
		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/999"));

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		HttpPatch patch = new HttpPatch("http://localhost:" + ourPort + "/Observation/123");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		HttpPatch patch = new HttpPatch("http://localhost:" + ourPort + "/Observation/123");
		patch.setEntity(new StringEntity(patchBody, ContentType.create(Constants.CT_JSON_PATCH, Charsets.UTF_8)));
		CloseableHttpResponse status = ourClient.execute(patch);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByAnyId() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByAnyIdWithTenantId() throws Exception {
		ourServlet.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTB/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/TENANTA/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByAnyIdWithTester() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentReadByIdParam() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByCompartmentReadByPatientParam() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?patient=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?patient=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?patient=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?subject=Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?subject=1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/DiagnosticReport?subject=Patient/2");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongAllTypesProactiveBlockEnabledNoResponse() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/999/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabled() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createCarePlan(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabledNoResponse() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.emptyList();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockEnabledNoResponse() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(404, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// CarePlan could potentially be in the Patient/1 compartment but we don't
		// have any rules explicitly allowing CarePlan so it's blocked
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/CarePlan/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/999/_history");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/900");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(901));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/901");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testReadByInstanceAllowsTargetedSearch() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

//		ourHitMethod = false;
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=900");
//		status = ourClient.execute(httpGet);
//		extractResponseAndClose(status);
//		assertEquals(200, status.getStatusLine().getStatusCode());
//		assertTrue(ourHitMethod);
//
//		ourHitMethod = false;
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=Patient/900");
//		status = ourClient.execute(httpGet);
//		extractResponseAndClose(status);
//		assertEquals(200, status.getStatusLine().getStatusCode());
//		assertTrue(ourHitMethod);
//
//		ourHitMethod = false;
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=901");
//		status = ourClient.execute(httpGet);
//		response = extractResponseAndClose(status);
//		assertEquals(403, status.getStatusLine().getStatusCode());
//		assertEquals(ERR403, response);
//		assertFalse(ourHitMethod);
//
//		ourHitMethod = false;
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=Patient/901");
//		status = ourClient.execute(httpGet);
//		response = extractResponseAndClose(status);
//		assertEquals(403, status.getStatusLine().getStatusCode());
//		assertEquals(ERR403, response);
//		assertFalse(ourHitMethod);
//
//		ourHitMethod = false;
//		// technically this is invalid, but just in case..
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?_id=Patient/901");
//		status = ourClient.execute(httpGet);
//		response = extractResponseAndClose(status);
//		assertEquals(403, status.getStatusLine().getStatusCode());
//		assertEquals(ERR403, response);
//		assertFalse(ourHitMethod);
//
//		ourHitMethod = false;
//		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?_id=901");
//		status = ourClient.execute(httpGet);
//		response = extractResponseAndClose(status);
//		assertEquals(403, status.getStatusLine().getStatusCode());
//		assertEquals(ERR403, response);
//		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=Patient/900,Patient/700");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=900,777");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadPageRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?_count=5&_format=json&subject=Patient/1");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertEquals(5, respBundle.getEntry().size());
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
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/5", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNull(respBundle.getLink("next"));

	}

	@Test
	public void testReadPageWrong() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?_count=5&_format=json&subject=Patient/1");
		status = ourClient.execute(httpGet);
		respString = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, respString);
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		httpGet = new HttpGet(respBundle.getLink("next").getUrl());
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testTransactionWithSearch() throws IOException {

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(requestBundle));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testTransactionWithNoBundleType() throws IOException {

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(requestBundle));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(resp, containsString("Invalid request Bundle.type value for transaction: \\\"\\\""));

	}

	/**
	 * See #762
	 */
	@Test
	public void testTransactionWithPlaceholderIdsResponseAuthorized() throws IOException {

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		String resp = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());

		ourLog.info(resp);

	}

	@Test
	public void testTransactionWriteGood() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testWriteByCompartmentCreate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader("If-None-Exist", "Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentCreateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalWithoutDirectMatch() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Collections.singletonList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDoesntAllowDelete() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
		httpPost.setEntity(createFhirResourceEntity(createPatient(2)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/1");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(99)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByInstance() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/900");
		httpPost.setEntity(createFhirResourceEntity(createObservation(900, "Patient/12")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/901");
		httpPost.setEntity(createFhirResourceEntity(createObservation(901, "Patient/12")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/900")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
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

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		httpPost = new HttpPatch("http://localhost:" + ourPort + "/Patient/900");
		httpPost.setEntity(new StringEntity(input, ContentType.parse("application/json-patch+json")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	public static class DummyCarePlanResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CarePlan.class;
		}

		@Read(version = true)
		public CarePlan read(@IdParam IdType theId) {
			ourHitMethod = true;
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (CarePlan) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			ourHitMethod = true;
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyEncounterResourceProvider implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			ourHitMethod = true;
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
			ourHitMethod = true;
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
			ourHitMethod = true;
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourHitMethod = true;
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdType theId) {
			ourHitMethod = true;
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
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * This should come after operation0
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Patch
		public MethodOutcome patch(@IdParam IdType theId, PatchTypeEnum thePatchType, @ResourceParam String theBody) {
			ourHitMethod = true;
			return new MethodOutcome().setId(theId.withVersion("2"));
		}

		@Read(version = true)
		public Observation read(@IdParam IdType theId) {
			ourHitMethod = true;
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIds,
			@OptionalParam(name = "subject") ReferenceParam theSubject) {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Observation", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			}

			ourHitMethod = true;
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IInterceptorBroadcaster theRequestOperationCallback, @IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			for (IBaseResource next : ourReturn) {
				HookParams params = new HookParams()
					.add(IBaseResource.class, next)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				theRequestOperationCallback.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
			}
			return new MethodOutcome();
		}

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			ourHitMethod = true;
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
			ourHitMethod = true;
			return (ourReturn);
		}

		@History()
		public List<Resource> history(@IdParam IdType theId) {
			ourHitMethod = true;
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * More generic method second to make sure that the
		 * other method takes precedence
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Patch()
		public MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theResource, PatchTypeEnum thePatchType) {
			ourHitMethod = true;

			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			ourHitMethod = true;
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(@OptionalParam(name = "_id") TokenAndListParam theIdParam) {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding,
												@Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
												@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

	}

	public static class PlainProvider {

		@History()
		public List<Resource> history() {
			ourHitMethod = true;
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "process-message", idempotent = true)
		public Parameters processMessage(@OperationParam(name = "content") Bundle theInput) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}


		@Transaction()
		public Bundle search(ServletRequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, @TransactionParam Bundle theInput) {
			ourHitMethod = true;
			if (ourDeleted != null) {
				for (IBaseResource next : ourDeleted) {
					HookParams params = new HookParams()
						.add(IBaseResource.class, next)
						.add(RequestDetails.class, theRequestDetails)
						.add(ServletRequestDetails.class, theRequestDetails);
					theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
				}
			}
			return (Bundle) ourReturn.get(0);
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();
		DummyObservationResourceProvider obsProv = new DummyObservationResourceProvider();
		DummyOrganizationResourceProvider orgProv = new DummyOrganizationResourceProvider();
		DummyEncounterResourceProvider encProv = new DummyEncounterResourceProvider();
		DummyCarePlanResourceProvider cpProv = new DummyCarePlanResourceProvider();
		DummyDiagnosticReportResourceProvider drProv = new DummyDiagnosticReportResourceProvider();
		PlainProvider plainProvider = new PlainProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patProvider, obsProv, encProv, cpProv, orgProv, drProv);
		ourServlet.setPlainProviders(plainProvider);
		ourServlet.setPagingProvider(new FifoMemoryPagingProvider(100));
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}


}
