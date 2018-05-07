package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class AuthorizationInterceptorResourceProviderR4Test extends BaseResourceProviderR4Test {

	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setAllowMultipleDelete(true);
		unregisterInterceptors();
	}

	/**
	 * See #778
	 */
	@Test
	public void testReadingObservationAccessRight() throws IOException {
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

	/**
	 * See #667
	 */
	@Test
	public void testBlockUpdatingPatientUserDoesnNotHaveAccessTo() throws IOException {
		Patient pt1 = new Patient();
		pt1.setActive(true);
		final IIdType pid1 = myClient.create().resource(pt1).execute().getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		final IIdType pid2 = myClient.create().resource(pt2).execute().getId().toUnqualifiedVersionless();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().write().allResources().inCompartment("Patient", pid1).andThen()
					.build();
			}
		});

		Observation obs = new Observation();
		obs.setStatus(ObservationStatus.FINAL);
		obs.setSubject(new Reference(pid1));
		IIdType oid = myClient.create().resource(obs).execute().getId().toUnqualified();


		unregisterInterceptors();
		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
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
	public void testCreateConditional() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final MethodOutcome output1 = myClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/" + output1.getId().getIdPart())).andThen()
					.allow().updateConditional().allResources()
					.build();
				//@formatter:on
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
			assertEquals("HTTP 403 Forbidden: Access denied by default policy (no applicable rules)", e.getMessage());
		}

		patient = new Patient();
		patient.setId("999");
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		try {
			myClient.update().resource(patient).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: Access denied by default policy (no applicable rules)", e.getMessage());
		}

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
	public void testDeleteResourceConditional() throws IOException {
		String methodName = "testDeleteResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

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
		resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

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
		p.setId(IdType.newRandomUuid());
		request.addEntry().setResource(p).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl(p.getId());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setResource(p);
		request.addEntry().setResource(o).getRequest().setMethod(Bundle.HTTPVerb.POST);

		Bundle resp = myClient.transaction().withBundle(request).execute();
		assertEquals(2, resp.getEntry().size());


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
		encounter.addReason(reason);

		// add encounter to bundle so its created
		bundle.addEntry()
			.setResource(encounter)
			.getRequest()
			.setUrl("Encounter")
			.setIfNoneExist("identifier=" + encounterSystem + "|" + encounterId)
			.setMethod(Bundle.HTTPVerb.POST);


		Bundle resp = myClient.transaction().withBundle(bundle).execute();
		assertEquals(3, resp.getEntry().size());

	}

	private void unregisterInterceptors() {
		for (IServerInterceptor next : new ArrayList<IServerInterceptor>(ourRestServer.getInterceptors())) {
			if (next instanceof AuthorizationInterceptor) {
				ourRestServer.unregisterInterceptor(next);
			}
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
