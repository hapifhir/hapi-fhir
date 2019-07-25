package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
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
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class AuthorizationInterceptorResourceProviderDstu3Test extends BaseResourceProviderDstu3Test {

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
	public void testReadingObservationAccessRight() {
		Practitioner practitioner1 = new Practitioner();
		final IIdType practitionerId1 = ourClient.create().resource(practitioner1).execute().getId().toUnqualifiedVersionless();

		Practitioner practitioner2 = new Practitioner();
		final IIdType practitionerId2 = ourClient.create().resource(practitioner2).execute().getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setActive(true);
		final IIdType patientId = ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

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
		IIdType oid1 = ourClient.create().resource(obs1).execute().getId().toUnqualified();

		// Observation with practitioner1 and practitioner1 as the Performer -> should have the read access
		ourClient.read().resource(Observation.class).withId(oid1).execute();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		obs2.setSubject(new Reference(patientId));
		IIdType oid2 = ourClient.create().resource(obs2).execute().getId().toUnqualified();

		// Observation with patient as the subject -> read access should be blocked
		try {
			ourClient.read().resource(Observation.class).withId(oid2).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	/**
	 * See #667
	 */
	@Test
	public void testBlockUpdatingPatientUserDoesnNotHaveAccessTo() {
		Patient pt1 = new Patient();
		pt1.setActive(true);
		final IIdType pid1 = ourClient.create().resource(pt1).execute().getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		final IIdType pid2 = ourClient.create().resource(pt2).execute().getId().toUnqualifiedVersionless();

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
		IIdType oid = ourClient.create().resource(obs).execute().getId().toUnqualified();


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
			ourClient.update().resource(obs).execute();
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
		final MethodOutcome output1 = ourClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		ourRestServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/" + output1.getId().getIdPart())).andThen()
					.allow().updateConditional().allResources()
					.build();
			}
		});

		patient = new Patient();
		patient.setId(output1.getId().toUnqualifiedVersionless());
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		MethodOutcome output2 = ourClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|100").execute();

		assertEquals(output1.getId().getIdPart(), output2.getId().getIdPart());

		patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		try {
			ourClient.update().resource(patient).conditionalByUrl("Patient?identifier=http://uhn.ca/mrns|101").execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: Access denied by default policy (no applicable rules)", e.getMessage());
		}

		patient = new Patient();
		patient.setId("999");
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		try {
			ourClient.update().resource(patient).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("HTTP 403 Forbidden: Access denied by default policy (no applicable rules)", e.getMessage());
		}

	}

	/**
	 * See #503 #751
	 */
	@Test
	public void testDeleteIsAllowedForCompartment() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://uhn.ca/mrns").setValue("100");
		patient.addName().setFamily("Tester").addGiven("Raghad");
		final IIdType id = ourClient.create().resource(patient).execute().getId();

		Observation obsInCompartment = new Observation();
		obsInCompartment.setStatus(ObservationStatus.FINAL);
		obsInCompartment.getSubject().setReferenceElement(id.toUnqualifiedVersionless());
		IIdType obsInCompartmentId = ourClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		// create a 2nd observation to be deleted by url Observation?patient=id
		ourClient.create().resource(obsInCompartment).execute().getId().toUnqualifiedVersionless();

		Observation obsNotInCompartment = new Observation();
		obsNotInCompartment.setStatus(ObservationStatus.FINAL);
		IIdType obsNotInCompartmentId = ourClient.create().resource(obsNotInCompartment).execute().getId().toUnqualifiedVersionless();

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

		ourClient.delete().resourceById(obsInCompartmentId.toUnqualifiedVersionless()).execute();
		ourClient.delete().resourceConditionalByUrl("Observation?patient=" + id.toUnqualifiedVersionless()).execute();

		try {
			ourClient.delete().resourceById(obsNotInCompartmentId.toUnqualifiedVersionless()).execute();
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
		IIdType id = ourClient.create().resource(patient).execute().getId();

		try {
			ourClient.delete().resourceById(id.toUnqualifiedVersionless()).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}

		patient = ourClient.read().resource(Patient.class).withId(id.toUnqualifiedVersionless()).execute();
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
