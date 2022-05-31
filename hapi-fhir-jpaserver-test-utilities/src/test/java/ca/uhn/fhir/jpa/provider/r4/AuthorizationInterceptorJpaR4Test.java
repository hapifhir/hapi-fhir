package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRuleTester;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class AuthorizationInterceptorJpaR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptorJpaR4Test.class);

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
		ourRestServer.registerInterceptor(new BulkDataExportProvider());
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof AuthorizationInterceptor);
	}

	@Test
	public void testBulkExport_AuthorizeGroupId() {

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().bulkExport().groupExportOnGroup(new IdType("Group/123")).andThen()
					.allow().bulkExport().groupExportOnGroup(new IdType("Group/789")).andThen()
					.build();
			}
		};
		myInterceptorRegistry.registerInterceptor(authInterceptor);

		/*
		 * Matching group ID
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/789"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}

		/*
		 * Second matching group ID
		 */
		{
		 BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		 bulkDataExportOptions.setGroupId(new IdType("Group/789"));
		 bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

		 ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
		 IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
		 assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());

		}

		/*
		 * Non matching group ID
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}

		/*
		 * Non group export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}
	}


	@Test
	public void testBulkExport_AuthorizePatientId() {

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().bulkExport().patientExportOnGroup(new IdType("Group/123")).andThen()
					.build();
			}
		};
		myInterceptorRegistry.registerInterceptor(authInterceptor);

		/*
		 * Matching group ID
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/123"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}

		/*
		 * Non matching group ID
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}

		/*
		 * Non group export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}


	}


	@Test
	public void testBulkExport_AuthorizeSystem() {

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().bulkExport().systemExport().andThen()
					.build();
			}
		};
		myInterceptorRegistry.registerInterceptor(authInterceptor);

		/*
		 * System export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}

		/*
		 * Patient export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}


	}


	@Test
	public void testBulkExport_AuthorizeAny() {

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().bulkExport().any().andThen()
					.build();
			}
		};
		myInterceptorRegistry.registerInterceptor(authInterceptor);

		/*
		 * System export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}

		/*
		 * Patient export
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setGroupId(new IdType("Group/456"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}


	}

	@Test
	public void testBulkExport_SpecificResourceTypesEnforced() {

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().bulkExport().systemExport().withResourceTypes(Lists.newArrayList("Patient", "Encounter")).andThen()
					.build();
			}
		};
		myInterceptorRegistry.registerInterceptor(authInterceptor);

		/*
		 * Appropriate Resources
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Patient", "Encounter"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, jobDetails.getStatus());
		}

		/*
		 * Inappropriate Resources
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Patient", "Encounter", "Observation"));
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}

		/*
		 * No Resources
		 */
		{
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

			try {
				ServletRequestDetails requestDetails = new ServletRequestDetails().setServletRequest(new MockHttpServletRequest());
				myBulkDataExportSvc.submitJob(bulkDataExportOptions, true, requestDetails);
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}
		}


	}

	/**
	 * See #667
	 */
	@Test
	public void testBlockUpdatingPatientUserDoesnNotHaveAccessTo() {
		Patient pt1 = new Patient();
		pt1.setActive(true);
		final IIdType pid1 = myClient.create().resource(pt1).execute().getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		final IIdType pid2 = myClient.create().resource(pt2).execute().getId().toUnqualifiedVersionless();

		AuthorizationInterceptor authInterceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().write().allResources().inCompartment("Patient", pid1).andThen()
					.build();
			}
		};
		ourRestServer.getInterceptorService().registerInterceptor(authInterceptor);

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.setSubject(new Reference(pid1));
		IIdType oid = myClient.create().resource(obs).execute().getId().toUnqualified();

		ourRestServer.getInterceptorService().unregisterInterceptor(authInterceptor);
		ourRestServer.getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().write().allResources().inCompartment("Patient", pid2).andThen()
					.build();
			}
		});

		/*
		 * Try to update to a new patient. The user has access to write to things in
		 * pid2's compartment, so this would normally be ok, but in this case they are overwriting
		 * a resource that is already in pid1's compartment, which shouldn't be allowed.
		 */
		obs = new Observation();
		obs.setId(oid);
		obs.setStatus(ObservationStatus.FINAL);
		obs.setSubject(new Reference(pid2));

		try {
			myClient.update().resource(obs).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

	}

	@Test
	public void testUpdateConditional() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final MethodOutcome output1 = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		ourRestServer.getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/" + output1.getId().getIdPart())).andThen()
					.allow().updateConditional().allResources()
					.build();
			}
		});

		patient = new Patient();
		patient.setId(output1.getId().toUnqualifiedVersionless());
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		MethodOutcome output2 = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		assertEquals(output1.getId().getIdPart(), output2.getId().getIdPart());

		patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		try {
			myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|101").execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: " + Msg.code(334) + "Access denied by default policy (no applicable rules)", e.getMessage());
		}

		patient = new Patient();
		patient.setId("999");
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		try {
			myClient.update().resource(patient).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: " + Msg.code(334) + "Access denied by default policy (no applicable rules)", e.getMessage());
		}

	}

	@Test
	public void testCreateConditionalViaTransaction() {
		ourRestServer.getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().create().resourcesOfType("Patient").withAnyId().withTester(new IAuthRuleTester() {
						@Override
						public boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
							if (theInputResource instanceof Patient) {
								Patient patient = (Patient) theInputResource;
								return patient
									.getIdentifier()
									.stream()
									.filter(t -> "http://uhn.ca/mrns".equals(t.getSystem()))
									.anyMatch(t -> "100".equals(t.getValue()));
							}
							return false;
						}
					}).andThen()
					.allow().createConditional().resourcesOfType("Patient").andThen()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.build();
			}
		});

		// Create a patient (allowed)
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
			patient.addName().setFamily("Tester").addGiven("Raghad");

			Bundle request = new Bundle();
			request.setType(Bundle.BundleType.TRANSACTION);
			request.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setIfNoneExist("Patient?identifier=http://uhn.ca/mrns|100");
			Bundle response = myClient.transaction().withBundle(request).execute();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

			// Subsequent calls also shouldn't fail
			myClient.transaction().withBundle(request).execute();
			myClient.transaction().withBundle(request).execute();
		}

		// Create a patient with wrong identifier (blocked)
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("101");
			patient.addName().setFamily("Tester").addGiven("Fozzie");

			Bundle request = new Bundle();
			request.setType(Bundle.BundleType.TRANSACTION);
			request.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setIfNoneExist("Patient?identifier=http://uhn.ca/mrns|101");

			try {
				myClient.transaction().withBundle(request).execute();
				fail();
			} catch (ForbiddenOperationException e) {
				assertEquals("HTTP 403 Forbidden: " + Msg.code(334) + "Access denied by default policy (no applicable rules)", e.getMessage());
			}
		}

		// Create an organization (blocked)
		{
			Organization patient = new Organization();
			patient.setName("FOO");

			Bundle request = new Bundle();
			request.setType(Bundle.BundleType.TRANSACTION);
			request.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setIfNoneExist("Organization?name=FOO");

			try {
				myClient.transaction().withBundle(request).execute();
				fail();
			} catch (ForbiddenOperationException e) {
				assertEquals("HTTP 403 Forbidden: " + Msg.code(334) + "Access denied by default policy (no applicable rules)", e.getMessage());
			}
		}

	}


	@Test
	public void testReadInTransaction() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		IIdType id = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				String authHeader = theRequestDetails.getHeader("Authorization");
				if (!"Bearer AAA".equals(authHeader)) {
					throw new AuthenticationException("Invalid auth header: " + authHeader);
				}
				return new RuleBuilder()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow().read().resourcesOfType(Patient.class).withAnyId()
					.build();
			}
		});

		SimpleRequestHeaderInterceptor interceptor = new SimpleRequestHeaderInterceptor("Authorization", "Bearer AAA");
		try {
			myClient.registerInterceptor(interceptor);

			Bundle bundle;
			Bundle responseBundle;

			// Read
			bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl(id.getValue());
			responseBundle = myClient.transaction().withBundle(bundle).execute();
			patient = (Patient) responseBundle.getEntry().get(0).getResource();
			assertEquals("Tester", patient.getNameFirstRep().getFamily());

			// Search
			bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Patient?");
			responseBundle = myClient.transaction().withBundle(bundle).execute();
			responseBundle = (Bundle) responseBundle.getEntry().get(0).getResource();
			patient = (Patient) responseBundle.getEntry().get(0).getResource();
			assertEquals("Tester", patient.getNameFirstRep().getFamily());

		} finally {
			myClient.unregisterInterceptor(interceptor);
		}

	}

	@Test
	public void testReadWithSubjectMasked() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		IIdType patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.setSubject(new Reference(patientId));
		IIdType observationId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType observationId2 = myClient.create().resource(obs2).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Observation.class).inCompartment("Patient", patientId)
					.build();
			}
		});

		Bundle bundle;
		Observation response;

		// Read (no masking)
		response = myClient.read().resource(Observation.class).withId(observationId).execute();
		assertEquals(ObservationStatus.FINAL, response.getStatus());
		assertEquals(patientId.getValue(), response.getSubject().getReference());

		// Read (with _elements masking)
		response = myClient
			.read()
			.resource(Observation.class)
			.withId(observationId)
			.elementsSubset("status")
			.execute();
		assertEquals(ObservationStatus.FINAL, response.getStatus());
		assertEquals(null, response.getSubject().getReference());

		// Read a non-allowed observation
		try {
			myClient.read().resource(Observation.class).withId(observationId2).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

	}

	@Test
	public void testSearchCodeIn() {
		createLocalCsAndVs();

		createObservation(withId("allowed"), withObservationCode(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM, "A"));
		createObservation(withId("disallowed"), withObservationCode(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM, "foo"));

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", FhirResourceDaoR4TerminologyTest.URL_MY_VALUE_SET).andThen()
					.build();
			}
		}.setValidationSupport(myValidationSupport));

		// Should be ok
		myClient.read().resource(Observation.class).withId("Observation/allowed").execute();

	}

	@Test
	public void testReadCodeIn_AllowedInCompartment() throws IOException {
		myValueSetDao.update(loadResourceFromClasspath(ValueSet.class, "r4/adi-vs2.json"));
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		logAllValueSetConcepts();

		mySystemDao.transaction(mySrd, loadResourceFromClasspath(Bundle.class, "r4/adi-ptbundle.json"));

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny().read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://payer-to-payer-exchange/fhir/ValueSet/mental-health/ndc-canonical-valueset").andThen()
					.allow().read().allResources().inCompartment("Patient", new IdType("Patient/P")).andThen()
					.build();
			}
		}.setValidationSupport(myValidationSupport));

		// Should be ok
		myClient.read().resource(Patient.class).withId("Patient/P").execute();
		myClient.read().resource(Observation.class).withId("Observation/O").execute();
	}

	/**
	 * See #751
	 */
	@Test
	public void testDeleteInCompartmentIsBlocked() {

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		IIdType id = myClient.update().resource(patient).execute().getId();

		Observation obs = new Observation();
		obs.setId("Observation/B");
		obs.getSubject().setReference("Patient/A");
		myClient.update().resource(obs).execute();

		obs = new Observation();
		obs.setId("Observation/C");
		obs.setStatus(ObservationStatus.FINAL);
		myClient.update().resource(obs).execute();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().allResources().inCompartment("Patient", new IdType("Patient/A")).andThen()
					.allow().read().allResources().withAnyId().andThen()
					.denyAll()
					.build();
			}
		});

		myClient.delete().resourceById(new IdType("Observation/B")).execute();

		try {
			myClient.read().resource(Observation.class).withId("Observation/B").execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myClient.delete().resourceById(new IdType("Observation/C")).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	/**
	 * See #503
	 */
	@Test
	public void testDeleteIsAllowedForCompartment() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final IIdType id = myClient.create().resource(patient).execute().getId();

		Observation obsInCompartment = new Observation();
		obsInCompartment.setStatus(ObservationStatus.FINAL);
		obsInCompartment.getSubject().setReferenceElement(id.toUnqualifiedVersionless());
		IIdType obsInCompartmentId = myClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		Observation obsNotInCompartment = new Observation();
		obsNotInCompartment.setStatus(ObservationStatus.FINAL);
		IIdType obsNotInCompartmentId = myClient.create().resource(obsNotInCompartment).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().resourcesOfType(Observation.class).inCompartment("Patient", id).andThen()
					.deny().delete().allResources().withAnyId().andThen()
					.allowAll()
					.build();
			}
		});

		myClient.delete().resourceById(obsInCompartmentId.toUnqualifiedVersionless()).execute();

		try {
			myClient.delete().resourceById(obsNotInCompartmentId.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testDeleteIsAllowedForCompartmentUsingTransaction() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final IIdType id = myClient.create().resource(patient).execute().getId();

		Observation obsInCompartment = new Observation();
		obsInCompartment.setStatus(ObservationStatus.FINAL);
		obsInCompartment.getSubject().setReferenceElement(id.toUnqualifiedVersionless());
		IIdType obsInCompartmentId = myClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		Observation obsNotInCompartment = new Observation();
		obsNotInCompartment.setStatus(ObservationStatus.FINAL);
		IIdType obsNotInCompartmentId = myClient.create().resource(obsNotInCompartment).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().resourcesOfType(Observation.class).inCompartment("Patient", id).andThen()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.denyAll()
					.build();
			}
		});

		Bundle bundle;

		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obsInCompartmentId.toUnqualifiedVersionless().getValue());
		myClient.transaction().withBundle(bundle).execute();

		try {
			bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl(obsNotInCompartmentId.toUnqualifiedVersionless().getValue());
			myClient.transaction().withBundle(bundle).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	/**
	 * See #503
	 */
	@Test
	public void testDeleteIsBlocked() {

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny().delete().allResources().withAnyId().andThen()
					.allowAll()
					.build();
			}
		});

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		IIdType id = myClient.create().resource(patient).execute().getId();

		try {
			myClient.delete().resourceById(id.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

		patient = myClient.read().resource(Patient.class).withId(id.toUnqualifiedVersionless()).execute();
		assertEquals(id.getValue(), patient.getId());
	}

	@Test
	public void testDeleteCascadeBlocked() {
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry);
		ourRestServer.getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
		try {

			// Create Patient, and Observation that refers to it
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
			patient.addName().setFamily("Tester").addGiven("Raghad");
			final IIdType patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReferenceElement(patientId);
			myClient.create().resource(obs).execute();

			// Allow any deletes, but don't allow cascade
			ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return new RuleBuilder()
						.allow().delete().allResources().withAnyId().andThen()
						.build();
				}
			});

			try {
				myClient
					.delete()
					.resourceById(patientId)
					.withAdditionalHeader(Constants.HEADER_CASCADE, Constants.CASCADE_DELETE)
					.execute();
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}


	@Test
	public void testDeleteCascadeAllowed() {
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry);
		ourRestServer.getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
		try {

			// Create Patient, and Observation that refers to it
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
			patient.addName().setFamily("Tester").addGiven("Raghad");
			final IIdType patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReferenceElement(patientId);
			myClient.create().resource(obs).execute();

			// Allow any deletes and allow cascade
			ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return new RuleBuilder()
						.allow().delete().allResources().withAnyId().andThen()
						.allow().delete().onCascade().allResources().withAnyId().andThen()
						.build();
				}
			});

			myClient
				.delete()
				.resourceById(patientId)
				.withAdditionalHeader(Constants.HEADER_CASCADE, Constants.CASCADE_DELETE)
				.execute();

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}

	@Test
	public void testDeleteCascadeAllowed_ButNotOnTargetType() {
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry);
		ourRestServer.getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
		try {

			// Create Patient, and Observation that refers to it
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
			patient.addName().setFamily("Tester").addGiven("Raghad");
			final IIdType patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReferenceElement(patientId);
			myClient.create().resource(obs).execute();

			// Allow any deletes, but don't allow cascade
			ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return new RuleBuilder()
						.allow().delete().resourcesOfType(Patient.class).withAnyId().andThen()
						.allow().delete().resourcesOfType(Observation.class).withAnyId().andThen()
						.allow().delete().onCascade().resourcesOfType(Patient.class).withAnyId().andThen()
						.build();
				}
			});

			try {
				myClient
					.delete()
					.resourceById(patientId)
					.withAdditionalHeader(Constants.HEADER_CASCADE, Constants.CASCADE_DELETE)
					.execute();
				fail();
			} catch (ForbiddenOperationException e) {
				// good
			}

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}

	@Test
	public void testDeleteResourceConditional() throws IOException {
		String methodName = "testDeleteResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		final IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		pt = new Patient();
		pt.addName().setFamily("FOOFOOFOO");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(post);
		final IdType id2;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
			id2 = new IdType(newIdString);
		} finally {
			response.close();
		}

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 2").delete().allResources().inCompartment("Patient", new IdDt("Patient/" + id.getIdPart())).andThen()
					.build();
				//@formatter:on
			}
		});

		HttpDelete delete = new HttpDelete(ourServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

		delete = new HttpDelete(ourServerBase + "/Patient?name=FOOFOOFOO");
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(403, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

	}


	@Test
	public void testDiffOperation_AllowedByType_Instance() {
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("A"), withActiveFalse());
		createObservation(withId("B"), withStatus("final"));

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().operation().named(ProviderConstants.DIFF_OPERATION_NAME).onAnyInstance().andAllowAllResponses().andThen()
					.allow().operation().named(ProviderConstants.DIFF_OPERATION_NAME).onServer().andAllowAllResponses().andThen()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll()
					.build();
			}
		});

		Parameters diff;

		diff = myClient.operation().onInstance("Patient/A").named(ProviderConstants.DIFF_OPERATION_NAME).withNoParameters(Parameters.class).execute();
		assertEquals(1, diff.getParameter().size());

		diff = myClient.operation().onInstanceVersion(new IdType("Patient/A/_history/2")).named(ProviderConstants.DIFF_OPERATION_NAME).withNoParameters(Parameters.class).execute();
		assertEquals(1, diff.getParameter().size());

		try {
			myClient.operation().onInstance("Observation/B").named(ProviderConstants.DIFF_OPERATION_NAME).withNoParameters(Parameters.class).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

	}

	@Test
	public void testDiffOperation_AllowedByType_Server() {
		createPatient(withId("A"), withActiveTrue());
		createPatient(withId("B"), withActiveFalse());
		createObservation(withId("C"), withStatus("final"));
		createObservation(withId("D"), withStatus("amended"));

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().operation().named(ProviderConstants.DIFF_OPERATION_NAME).onAnyInstance().andAllowAllResponses().andThen()
					.allow().operation().named(ProviderConstants.DIFF_OPERATION_NAME).onServer().andAllowAllResponses().andThen()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll()
					.build();
			}
		});

		Parameters diff;

		diff = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_PARAMETER, new StringType("Patient/A"))
			.andParameter(ProviderConstants.DIFF_TO_PARAMETER, new StringType("Patient/B"))
			.execute();
		assertEquals(2, diff.getParameter().size());

		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.DIFF_OPERATION_NAME)
				.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_PARAMETER, new StringType("Observation/C"))
				.andParameter(ProviderConstants.DIFF_TO_PARAMETER, new StringType("Observation/D"))
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

	}


	@Test
	public void testGraphQL_AllowedByType_Instance() throws IOException {
		createPatient(withId("A"), withFamily("MY_FAMILY"));
		createPatient(withId("B"), withFamily("MY_FAMILY"));

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().graphQL().any().andThen()
					.allow().read().instance("Patient/A").andThen()
					.denyAll()
					.build();
			}
		});

		HttpGet httpGet;
		String query = "{name{family,given}}";

		httpGet = new HttpGet(ourServerBase + "/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("MY_FAMILY"));
		}

		httpGet = new HttpGet(ourServerBase + "/Patient/B/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(403, response.getStatusLine().getStatusCode());
		}

	}


	/**
	 * See #762
	 */
	@Test
	public void testInstanceRuleOkForResourceWithNoId() {
		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny().write().instance("Patient/123").andThen()
					.allowAll()
					.build();
			}
		});

		/*
		 * Create a transaction using linked IDs
		 */

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);

		Patient p = new Patient();
		p.setActive(true);
		p.setId("123");
		request.addEntry().setResource(p).getRequest().setMethod(Bundle.HTTPVerb.POST)
			.setUrl(
				"Patient/" +
					p.getId());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setResource(p);
		request.addEntry().setResource(o).getRequest().setMethod(Bundle.HTTPVerb.POST);

		Bundle resp = myClient.transaction().withBundle(request).execute();
		assertEquals(2, resp.getEntry().size());


	}


	@Test
	public void testTransactionResponses() {

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					// Allow write but not read
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("write patient").write().resourcesOfType(Encounter.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		// Create a bundle that will be used as a transaction
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		Encounter encounter = new Encounter();
		encounter.addIdentifier(new Identifier().setSystem("http://foo").setValue("123"));
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);
		bundle.addEntry()
			.setFullUrl("Encounter")
			.setResource(encounter)
			.getRequest()
			.setUrl("Encounter")
			.setMethod(Bundle.HTTPVerb.POST);

		// return=minimal - should succeed
		Bundle resp = myClient
			.transaction()
			.withBundle(bundle)
			.withAdditionalHeader(Constants.HEADER_PREFER, "return=" + Constants.HEADER_PREFER_RETURN_MINIMAL)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertNull(resp.getEntry().get(0).getResource());

		// return=OperationOutcome - should succeed
		resp = myClient
			.transaction()
			.withBundle(bundle)
			.withAdditionalHeader(Constants.HEADER_PREFER, "return=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertNull(resp.getEntry().get(0).getResource());

		// return=Representation - should fail
		try {
			myClient
				.transaction()
				.withBundle(bundle)
				.withAdditionalHeader(Constants.HEADER_PREFER, "return=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}


	/**
	 * See #762
	 */
	@Test
	public void testInstanceRuleOkForResourceWithNoId2() {

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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


		// Create a bundle that will be used as a transaction
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);


		String encounterId = "123-123";
		String encounterSystem = "http://our.internal.code.system/encounter";
		Encounter encounter = new Encounter();

		encounter.addIdentifier(new Identifier().setValue(encounterId)
			.setSystem(encounterSystem));

		encounter.setStatus(Encounter.EncounterStatus.FINISHED);

		Patient p = new Patient()
			.addIdentifier(new Identifier().setValue("321-321").setSystem("http://our.internal.code.system/patient"));
		p.setId(IdDt.newRandomUuid());

		// add patient to bundle so its created
		bundle.addEntry()
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

		// add condition to bundle so its created
		bundle.addEntry()
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

		// add encounter to bundle so its created
		bundle.addEntry()
			.setResource(encounter)
			.getRequest()
			.setUrl("Encounter")
			.setIfNoneExist("identifier=" + encounterSystem + "|" + encounterId)
			.setMethod(Bundle.HTTPVerb.POST);


		Bundle resp = myClient
			.transaction()
			.withBundle(bundle)
			.withAdditionalHeader(Constants.HEADER_PREFER, "return=" + Constants.HEADER_PREFER_RETURN_MINIMAL)
			.execute();
		assertEquals(3, resp.getEntry().size());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
	}


	@Test
	public void testOperationEverything_SomeIncludedResourcesNotAuthorized() {
		Patient pt1 = new Patient();
		pt1.setActive(true);
		final IIdType pid1 = myClient.create().resource(pt1).execute().getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setSubject(new Reference(pid1));
		myClient.create().resource(obs1).execute();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().operation().named(JpaConstants.OPERATION_EVERYTHING).onInstance(pid1).andRequireExplicitResponseAuthorization().andThen()
					.allow().read().resourcesOfType(Patient.class).inCompartment("Patient", pid1).andThen()
					.allow().read().resourcesOfType(Observation.class).inCompartment("Patient", pid1).andThen()
					.allow().create().resourcesOfType(Encounter.class).withAnyId().andThen()
					.build();
			}
		});

		Bundle outcome = myClient
			.operation()
			.onInstance(pid1)
			.named(JpaConstants.OPERATION_EVERYTHING)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		assertEquals(2, outcome.getEntry().size());

		// Add an Encounter, which will be returned by $everything but that hasn't been
		// explicitly authorized

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(pid1));
		myClient.create().resource(enc).execute();

		try {
			outcome = myClient
				.operation()
				.onInstance(pid1)
				.named(JpaConstants.OPERATION_EVERYTHING)
				.withNoParameters(Parameters.class)
				.returnResourceType(Bundle.class)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), containsString("Access denied by default policy"));
		}
	}


	@Test
	public void testPatchWithinCompartment() {
		Patient pt1 = new Patient();
		pt1.setActive(true);
		final IIdType pid1 = myClient.create().resource(pt1).execute().getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setSubject(new Reference(pid1));
		IIdType oid1 = myClient.create().resource(obs1).execute().getId().toUnqualified();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		final IIdType pid2 = myClient.create().resource(pt2).execute().getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setSubject(new Reference(pid2));
		IIdType oid2 = myClient.create().resource(obs2).execute().getId().toUnqualified();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().patch().allRequests().andThen()
					.allow().write().allResources().inCompartment("Patient", pid1).andThen()
					.allow().read().allResources().withAnyId().andThen()
					.build();
			}
		});

		String patchBody = "[\n" +
			"     { \"op\": \"replace\", \"path\": \"/status\", \"value\": \"amended\" }\n" +
			"     ]";

		// Allowed
		myClient.patch().withBody(patchBody).withId(oid1).execute();
		obs1 = myClient.read().resource(Observation.class).withId(oid1.toUnqualifiedVersionless()).execute();
		assertEquals(ObservationStatus.AMENDED, obs1.getStatus());

		// Denied
		try {
			myClient.patch().withBody(patchBody).withId(oid2).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
		obs2 = myClient.read().resource(Observation.class).withId(oid2.toUnqualifiedVersionless()).execute();
		assertEquals(ObservationStatus.FINAL, obs2.getStatus());
	}

	/**
	 * See #778
	 */
	@Test
	public void testReadingObservationAccessRight() {
		Practitioner practitioner1 = new Practitioner();
		final IIdType practitionerId1 = myClient.create().resource(practitioner1).execute().getId().toUnqualifiedVersionless();

		Practitioner practitioner2 = new Practitioner();
		final IIdType practitionerId2 = myClient.create().resource(practitioner2).execute().getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setActive(true);
		final IIdType patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				// allow write all Observation resource
				// allow read only Observation resource in which it has a practitioner1 or practitioner2 compartment
				return new RuleBuilder().allow()
					.write()
					.resourcesOfType(Observation.class)
					.withAnyId()
					.andThen()
					.allow()
					.read()
					.resourcesOfType(Observation.class)
					.inCompartment("Practitioner", Arrays.asList(practitionerId1, practitionerId2))
					.andThen()
					.denyAll()
					.build();
			}
		});

		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		obs1.setPerformer(
			Arrays.asList(new Reference(practitionerId1), new Reference(practitionerId2)));
		IIdType oid1 = myClient.create().resource(obs1).execute().getId().toUnqualified();

		// Observation with practitioner1 and practitioner1 as the Performer -> should have the read access
		myClient.read().resource(Observation.class).withId(oid1).execute();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setSubject(new Reference(patientId));
		IIdType oid2 = myClient.create().resource(obs2).execute().getId().toUnqualified();

		// Observation with patient as the subject -> read access should be blocked
		try {
			myClient.read().resource(Observation.class).withId(oid2).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testReadCompartmentTwoPatientIds() {
		Patient patient1 = new Patient();
		patient1.setActive(true);
		IIdType p1id = myPatientDao.create(patient1).getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.setActive(true);
		IIdType p2id = myPatientDao.create(patient2).getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow()
					.read()
					.allResources()
					.inCompartment("Patient", p1id)
					.andThen()
					.allow()
					.read()
					.allResources()
					.inCompartment("Patient", p2id)
					.andThen()
					.denyAll()
					.build();
			}
		});

		{
			String url = "/Patient?_id=" + p1id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(1, result.getTotal());
		}
		{
			String url = "/Patient?_id=" + p2id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(1, result.getTotal());
		}
		{
			String url = "/Patient?_id=" + p1id.getIdPart() + "," + p2id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(2, result.getTotal());
		}
	}


	@Test
	public void testReadCompartmentTwoPatientIdsTwoEOBs() {
		Patient patient1 = new Patient();
		patient1.setActive(true);
		IIdType p1id = myPatientDao.create(patient1).getId().toUnqualifiedVersionless();

		ExplanationOfBenefit eob1 = new ExplanationOfBenefit();
		eob1.setPatient(new Reference(p1id));
		myExplanationOfBenefitDao.create(eob1);

		Patient patient2 = new Patient();
		patient2.setActive(true);
		IIdType p2id = myPatientDao.create(patient2).getId().toUnqualifiedVersionless();

		ExplanationOfBenefit eob2 = new ExplanationOfBenefit();
		eob2.setPatient(new Reference(p2id));
		myExplanationOfBenefitDao.create(eob2);

		Patient patient3 = new Patient();
		patient3.setActive(true);
		IIdType p3id = myPatientDao.create(patient3).getId().toUnqualifiedVersionless();

		ExplanationOfBenefit eob3 = new ExplanationOfBenefit();
		eob3.setPatient(new Reference(p3id));
		myExplanationOfBenefitDao.create(eob3);

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow()
					.read()
					.allResources()
					.inCompartment("Patient", p1id)
					.andThen()
					.allow()
					.read()
					.allResources()
					.inCompartment("Patient", p2id)
					.andThen()
					.denyAll()
					.build();
			}
		});

		{
			String url = "/ExplanationOfBenefit?patient=" + p1id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(1, result.getTotal());
		}
		{
			String url = "/ExplanationOfBenefit?patient=" + p2id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(1, result.getTotal());
		}
		{
			String url = "/ExplanationOfBenefit?patient=" + p1id.getIdPart() + "," + p2id.getIdPart();
			Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
			assertEquals(2, result.getTotal());
		}
		{
			String url = "/ExplanationOfBenefit?patient=" + p1id.getIdPart() + "," + p3id.getIdPart();
			try {
				Bundle result = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
				fail();
			} catch (ForbiddenOperationException e) {
				assertThat(e.getMessage(), startsWith("HTTP 403 Forbidden: " + Msg.code(333) + "Access denied by rule"));
			}
		}

	}


	@Test
	public void testDeleteExpungeBlocked() {
		// Create Patient, and Observation that refers to it
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Siobhan");
		myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		// Allow any deletes, but don't allow expunge
		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().allResources().withAnyId().andThen()
					.build();
			}
		});

		try {
			myClient
				.delete()
				.resourceConditionalByUrl("Patient?name=Siobhan&_expunge=true")
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testDeleteExpungeAllowed() {

		// Create Patient, and Observation that refers to it
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		// Allow deletes and allow expunge
		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().delete().allResources().withAnyId().andThen()
					.allow().delete().onExpunge().allResources().withAnyId().andThen()
					.build();
			}
		});

		myClient
			.delete()
			.resourceConditionalByUrl("Patient?name=Siobhan&_expunge=true")
			.execute();
	}
}
