package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.delete.ThreadSafeResourceDeleterSvc;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.term.TermTestUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
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
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AuthorizationInterceptorJpaR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptorJpaR4Test.class);

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;
	@Autowired
	private ThreadSafeResourceDeleterSvc myThreadSafeResourceDeleterSvc;
	private AuthorizationInterceptor myReadAllBundleInterceptor;
	private AuthorizationInterceptor myReadAllPatientInterceptor;
	private AuthorizationInterceptor myWriteResourcesInTransactionAuthorizationInterceptor;

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setDeleteExpungeEnabled(true);
		myReadAllBundleInterceptor = new ReadAllAuthorizationInterceptor("Bundle");
		myReadAllPatientInterceptor = new ReadAllAuthorizationInterceptor("Patient");
		myWriteResourcesInTransactionAuthorizationInterceptor = new WriteResourcesInTransactionAuthorizationInterceptor();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof AuthorizationInterceptor);
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
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(authInterceptor);

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.setSubject(new Reference(pid1));
		IIdType oid = myClient.create().resource(obs).execute().getId().toUnqualified();

		myServer.getRestfulServer().getInterceptorService().unregisterInterceptor(authInterceptor);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
					.allow().read().resourcesOfType("Patient").withAnyId().andThen()
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
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		createObservation(withId("allowed"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "A"));
		createObservation(withId("disallowed"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "foo"));

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", TermTestUtil.URL_MY_VALUE_SET).andThen()
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry, myThreadSafeResourceDeleterSvc);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
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
			myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
			myServer.getRestfulServer().getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}


	@Test
	public void testDeleteCascadeAllowed() {
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry, myThreadSafeResourceDeleterSvc);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
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
			myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
			myServer.getRestfulServer().getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}

	@Test
	public void testDeleteCascadeAllowed_ButNotOnTargetType() {
		CascadingDeleteInterceptor cascadingDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry, myThreadSafeResourceDeleterSvc);
		myServer.getRestfulServer().getInterceptorService().registerInterceptor(cascadingDeleteInterceptor);
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
			myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
			myServer.getRestfulServer().getInterceptorService().unregisterInterceptor(cascadingDeleteInterceptor);
		}
	}

	@Test
	public void testDeleteResourceConditional() throws IOException {
		String methodName = "testDeleteResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		final IdType id;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(myServerBase + "/Patient/"));
			id = new IdType(newIdString);
		} finally {
			response.close();
		}

		pt = new Patient();
		pt.addName().setFamily("FOOFOOFOO");
		resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		post = new HttpPost(myServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		response = ourHttpClient.execute(post);
		final IdType id2;
		try {
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(myServerBase + "/Patient/"));
			id2 = new IdType(newIdString);
		} finally {
			response.close();
		}

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 2").delete().allResources().inCompartment("Patient", new IdDt("Patient/" + id.getIdPart())).andThen()
					.build();
				//@formatter:on
			}
		});

		HttpDelete delete = new HttpDelete(myServerBase + "/Patient?name=" + methodName);
		response = ourHttpClient.execute(delete);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

		delete = new HttpDelete(myServerBase + "/Patient?name=FOOFOOFOO");
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		httpGet = new HttpGet(myServerBase + "/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam(query));
		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("MY_FAMILY"));
		}

		httpGet = new HttpGet(myServerBase + "/Patient/B/$graphql?query=" + UrlUtil.escapeUrlParam(query));
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
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertNull(resp.getEntry().get(0).getResource());

		// return=OperationOutcome - should succeed
		resp = myClient
			.transaction()
			.withBundle(bundle)
			.withAdditionalHeader(Constants.HEADER_PREFER, "return=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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

	@Test
	public void testSmartFilterSearchAllowed() {
		createObservation(withId("allowed"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "A"));
		createObservation(withId("allowed2"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "foo"));

		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("filter rule").read().allResources().withAnyId()
					.withFilterTester("code=" + TermTestUtil.URL_MY_CODE_SYSTEM + "|")
					.andThen().build();
			}
		};
		interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
		myServer.getRestfulServer().registerInterceptor(interceptor);

		// search runs without 403.
		Bundle bundle = myClient.search().byUrl("/Observation?code=foo").returnBundle(Bundle.class).execute();
		assertThat(bundle.getEntry(), hasSize(1));
	}

	@Test
	public void testSmartFilterSearch_badQuery_abstain() {
		createObservation(withId("obs1"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "A"));
		createObservation(withId("obs2"), withObservationCode(TermTestUtil.URL_MY_CODE_SYSTEM, "foo"));

		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("filter rule").read().allResources().withFilter("unknown_code=foo").andThen()
					.build();
			}
		};
		interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
		myServer.getRestfulServer().registerInterceptor(interceptor);

		// search should fail since the allow rule can't be evaluated with an unknown SP
		try {
			myClient.search().byUrl("/Observation").returnBundle(Bundle.class).execute();
			fail("expect 403 error");
		} catch (ForbiddenOperationException e) {
			// expected
		}

	}

	@Test
	public void testSearchBundles_withPermissionToSearchAllBundles_doesNotReturn403ForbiddenForDocumentBundles(){
		myServer.getRestfulServer().registerInterceptor(myReadAllBundleInterceptor);

		Bundle bundle1 = createDocumentBundle(createPatient("John", "Smith"));
		Bundle bundle2 = createDocumentBundle(createPatient("Jane", "Doe"));
		assertSearchContainsResources("/Bundle", bundle1, bundle2);
	}

	@Test
	public void testSearchBundles_withPermissionToSearchAllBundles_doesNotReturn403ForbiddenForCollectionBundles(){
		myServer.getRestfulServer().registerInterceptor(myReadAllBundleInterceptor);

		Bundle bundle1 = createCollectionBundle(createPatient("John", "Smith"));
		Bundle bundle2 = createCollectionBundle(createPatient("Jane", "Doe"));
		assertSearchContainsResources("/Bundle", bundle1, bundle2);
	}

	@Test
	public void testSearchBundles_withPermissionToSearchAllBundles_doesNotReturn403ForbiddenForMessageBundles(){
		myServer.getRestfulServer().registerInterceptor(myReadAllBundleInterceptor);

		Bundle bundle1 = createMessageHeaderBundle(createPatient("John", "Smith"));
		Bundle bundle2 = createMessageHeaderBundle(createPatient("Jane", "Doe"));
		assertSearchContainsResources("/Bundle", bundle1, bundle2);
	}

	@Test
	public void testSearchBundles_withPermissionToViewOneBundle_onlyAllowsViewingOneBundle(){
		Bundle bundle1 = createMessageHeaderBundle(createPatient("John", "Smith"));
		Bundle bundle2 = createMessageHeaderBundle(createPatient("Jane", "Doe"));

		myServer.getRestfulServer().getInterceptorService().registerInterceptor(
			new ReadInCompartmentAuthorizationInterceptor("Bundle", bundle1.getIdElement())
		);

		assertSearchContainsResources("/Bundle?_id=" + bundle1.getIdPart(), bundle1);
		assertSearchFailsWith403Forbidden("/Bundle?_id=" + bundle2.getIdPart());
		assertSearchFailsWith403Forbidden("/Bundle");
	}

	@Test
	public void testSearchPatients_withPermissionToSearchAllBundles_returns403Forbidden(){
		myServer.getRestfulServer().registerInterceptor(myReadAllBundleInterceptor);

		createPatient("John", "Smith");
		createPatient("Jane", "Doe");
		assertSearchFailsWith403Forbidden("/Patient");
	}

	@Test
	public void testSearchPatients_withPermissionToSearchAllPatients_returnsAllPatients(){
		myServer.getRestfulServer().registerInterceptor(myReadAllPatientInterceptor);

		Patient patient1 = createPatient("John", "Smith");
		Patient patient2 = createPatient("Jane", "Doe");
		assertSearchContainsResources("/Patient", patient1, patient2);
	}

	@Test
	public void testSearchPatients_withPermissionToViewOnePatient_onlyAllowsViewingOnePatient(){
		Patient patient1 = createPatient("John", "Smith");
		Patient patient2 = createPatient("Jane", "Doe");

		myServer.getRestfulServer().getInterceptorService().registerInterceptor(
			new ReadInCompartmentAuthorizationInterceptor("Patient", patient1.getIdElement())
		);

		assertSearchContainsResources("/Patient?_id=" + patient1.getIdPart(), patient1);
		assertSearchFailsWith403Forbidden("/Patient?_id=" + patient2.getIdPart());
		assertSearchFailsWith403Forbidden("/Patient");
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingDocumentBundles_onlyRecursesOneLevelDeep() {
		Bundle bundle1 = createDocumentBundle(createPatient("John", "Smith"));
		Bundle bundle2 = createDocumentBundle(createPatient("John", "Smith"));
		Bundle searchSet = createSearchSet(bundle1, bundle2);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Bundle");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(requestDetails, searchSet, myFhirContext);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(bundle1));
		assertTrue(resources.contains(bundle2));
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingPatients_returnsPatients() {
		Patient patient1 = createPatient("John", "Smith");
		Patient patient2 = createPatient("Jane", "Doe");
		Bundle searchSet = createSearchSet(patient1, patient2);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Patient");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(requestDetails, searchSet, myFhirContext);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(patient1));
		assertTrue(resources.contains(patient2));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "COLLECTION", "MESSAGE"})
	public void testShouldExamineBundleResources_withBundleRequestAndStandAloneBundleType_returnsFalse(Bundle.BundleType theBundleType){
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Bundle");
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);

		assertFalse(AuthorizationInterceptor.shouldExamineBundleChildResources(requestDetails, myFhirContext, bundle));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "COLLECTION", "MESSAGE"}, mode= EnumSource.Mode.EXCLUDE)
	public void testShouldExamineBundleResources_withBundleRequestAndNonStandAloneBundleType_returnsTrue(Bundle.BundleType theBundleType){
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Bundle");
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);

		assertTrue(AuthorizationInterceptor.shouldExamineBundleChildResources(requestDetails, myFhirContext, bundle));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class)
	public void testShouldExamineBundleResources_withNonBundleRequests_returnsTrue(Bundle.BundleType theBundleType){
		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Patient");
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);

		assertTrue(AuthorizationInterceptor.shouldExamineBundleChildResources(requestDetails, myFhirContext, bundle));
	}

	@ParameterizedTest
	@ArgumentsSource(StandaloneBundleTypesArgumentsProvider.class)
	public void testPermissionsToPostTransaction_withStandaloneBundles_successfullyPostsTransactions(BundleTypeEnum theStandaloneBundleType){
		Bundle nestedBundle = new Bundle();
		nestedBundle.setId("some-bundle");
		BundleUtil.setBundleType(myFhirContext, nestedBundle, theStandaloneBundleType.getCode());

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(nestedBundle);
		IBaseBundle transaction = builder.getBundle();

		myServer.getRestfulServer().registerInterceptor(myWriteResourcesInTransactionAuthorizationInterceptor);

		myClient
			.transaction()
			.withBundle(transaction)
			.execute();

		List<IBaseResource> savedBundles = myBundleDao.search(SearchParameterMap.newSynchronous(), mySrd).getAllResources();
		assertEquals(1, savedBundles.size());

		Bundle savedNestedBundle = (Bundle) savedBundles.get(0);
		assertEquals(theStandaloneBundleType, BundleUtil.getBundleTypeEnum(myFhirContext, savedNestedBundle));
		assertEquals(nestedBundle.getIdPart(), savedNestedBundle.getIdPart());
	}

	@ParameterizedTest
	@ArgumentsSource(NonStandaloneBundleTypesArgumentsProvider.class)
	public void testPermissionsToPostTransaction_withNonStandaloneBundles_transactionsFails(BundleTypeEnum theStandaloneBundleType){
		Bundle nestedBundle = new Bundle();
		BundleUtil.setBundleType(myFhirContext, nestedBundle, theStandaloneBundleType.getCode());

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionCreateEntry(nestedBundle);
		IBaseBundle transaction = builder.getBundle();

		myServer.getRestfulServer().registerInterceptor(myWriteResourcesInTransactionAuthorizationInterceptor);

		try {
			myClient
				.transaction()
				.withBundle(transaction)
				.execute();
			fail();
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("HTTP 400 Bad Request"));
		}

		List<IBaseResource> savedBundles = myBundleDao.search(SearchParameterMap.newSynchronous(), mySrd).getAllResources();
		assertTrue(savedBundles.isEmpty());
	}

	@ParameterizedTest
	@ArgumentsSource(StandaloneBundleTypesArgumentsProvider.class)
	public void testPermissionsToPostTransactions_withStandaloneBundles_onlyProcessesTransactionsOneLevelDeep(BundleTypeEnum theStandaloneBundleType){
		// second level transaction
		Patient patient = new Patient();
		patient.setId("some-patient");
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionCreateEntry(patient);
		IBaseBundle secondLevelTransaction = builder.getBundle();

		// first level standalone Bundle
		builder = new BundleBuilder(myFhirContext);
		switch (theStandaloneBundleType) {
			case COLLECTION -> builder.addCollectionEntry(secondLevelTransaction);
			case DOCUMENT -> builder.addDocumentEntry(secondLevelTransaction);
			case MESSAGE -> builder.addMessageEntry(secondLevelTransaction);
			default -> throw new RuntimeException();
		}
		IBaseBundle firstLevelStandaloneBundle = builder.getBundle();

		// parent transaction
		builder = new BundleBuilder(myFhirContext);
		builder.addTransactionCreateEntry(firstLevelStandaloneBundle);
		IBaseBundle parentTransaction = builder.getBundle();

		myServer.getRestfulServer().registerInterceptor(myWriteResourcesInTransactionAuthorizationInterceptor);

		myClient
			.transaction()
			.withBundle(parentTransaction)
			.execute();

		// verify first level standalone Bundle was saved
		List<IBaseResource> allBundles = myBundleDao.search(SearchParameterMap.newSynchronous(), mySrd).getAllResources();
		assertEquals(1, allBundles.size());
		Bundle savedStandaloneBundle = (Bundle) allBundles.get(0);
		assertEquals(theStandaloneBundleType, BundleUtil.getBundleTypeEnum(myFhirContext, savedStandaloneBundle));

		// verify contents of first level standalone Bundle
		assertEquals(1, savedStandaloneBundle.getEntry().size());
		Bundle savedSecondLevelTransaction = (Bundle) savedStandaloneBundle.getEntry().get(0).getResource();
		assertEquals(Bundle.BundleType.TRANSACTION, savedSecondLevelTransaction.getType());
		assertEquals(1, savedSecondLevelTransaction.getEntry().size());
		Patient patientSavedInsideBundle = (Patient) savedSecondLevelTransaction.getEntry().get(0).getResource();
		assertEquals(patient.getIdPart(), patientSavedInsideBundle.getIdPart());

		// verify second level Patient transaction did NOT execute
		assertTrue(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd).isEmpty());
	}

	private Patient createPatient(String theFirstName, String theLastName){
		Patient patient = new Patient();
		patient.addName().addGiven(theFirstName).setFamily(theLastName);
		return (Patient) myPatientDao.create(patient, mySrd).getResource();
	}

	private Bundle createDocumentBundle(Patient thePatient){
		Composition composition = new Composition();
		composition.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://example.org").setCode("some-type")));
		composition.getSubject().setReference(thePatient.getIdElement().getValue());

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(thePatient);
		return (Bundle) myBundleDao.create(bundle, mySrd).getResource();
	}

	private Bundle createCollectionBundle(Patient thePatient) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.COLLECTION);
		bundle.addEntry().setResource(thePatient);
		return (Bundle) myBundleDao.create(bundle, mySrd).getResource();
	}

	private Bundle createMessageHeaderBundle(Patient thePatient) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.MESSAGE);

		MessageHeader messageHeader = new MessageHeader();
		Coding event = new Coding().setSystem("http://acme.com").setCode("some-event");
		messageHeader.setEvent(event);
		messageHeader.getFocusFirstRep().setReference(thePatient.getIdElement().getValue());
		bundle.addEntry().setResource(messageHeader);
		bundle.addEntry().setResource(thePatient);

		return (Bundle) myBundleDao.create(bundle, mySrd).getResource();
	}

	private void assertSearchContainsResources(String theUrl, Resource... theExpectedResources){
		List<String> expectedIds = Arrays.stream(theExpectedResources)
			.map(resource -> resource.getIdPart())
			.toList();

		Bundle searchResult = myClient
			.search()
			.byUrl(theUrl)
			.returnBundle(Bundle.class)
			.execute();

		List<String> actualIds = searchResult.getEntry().stream()
			.map(entry -> entry.getResource().getIdPart())
			.toList();

		assertEquals(expectedIds.size(), actualIds.size());
		assertTrue(expectedIds.containsAll(actualIds));
	}

	private void assertSearchFailsWith403Forbidden(String theUrl){
		try {
			myClient.search().byUrl(theUrl).execute();
			fail();
		} catch (Exception e){
			assertTrue(e.getMessage().contains("HTTP 403 Forbidden"));
		}
	}

	private Bundle createSearchSet(Resource... theResources){
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.SEARCHSET);
		Arrays.stream(theResources).forEach(resource -> bundle.addEntry().setResource(resource));
		return bundle;
	}

	static class ReadAllAuthorizationInterceptor extends AuthorizationInterceptor {

		private final String myResourceType;

		public ReadAllAuthorizationInterceptor(String theResourceType){
			super(PolicyEnum.DENY);
			myResourceType = theResourceType;
		}

		@Override
		public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
			return new RuleBuilder()
				.allow().read().resourcesOfType(myResourceType).withAnyId().andThen()
				.build();
		}
	}

	static class ReadInCompartmentAuthorizationInterceptor extends AuthorizationInterceptor {

		private final String myResourceType;
		private final IIdType myId;

		public ReadInCompartmentAuthorizationInterceptor(String theResourceType, IIdType theId){
			super(PolicyEnum.DENY);
			myResourceType = theResourceType;
			myId = theId;
		}

		@Override
		public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
			return new RuleBuilder()
				.allow().read().allResources().inCompartment(myResourceType, myId).andThen()
				.build();
		}
	}

	static class WriteResourcesInTransactionAuthorizationInterceptor extends AuthorizationInterceptor {

		public WriteResourcesInTransactionAuthorizationInterceptor(){
			super(PolicyEnum.DENY);
		}

		@Override
		public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
			return new RuleBuilder()
				.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
				.allow().write().allResources().withAnyId().andThen()
				.build();
		}
	}

	static class StandaloneBundleTypesArgumentsProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
			Set<Arguments> arguments = new HashSet<>();

			Arrays.stream(BundleTypeEnum.values())
				.filter(BundleUtil::isStandaloneBundleType)
				.forEach(type -> arguments.add(Arguments.of(type)));

			return arguments.stream();
		}
	}

	static class NonStandaloneBundleTypesArgumentsProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
			Set<Arguments> arguments = new HashSet<>();

			Arrays.stream(BundleTypeEnum.values())
				.filter(type -> !BundleUtil.isStandaloneBundleType(type))
				.forEach(type -> arguments.add(Arguments.of(type)));

			return arguments.stream();
		}
	}
}
