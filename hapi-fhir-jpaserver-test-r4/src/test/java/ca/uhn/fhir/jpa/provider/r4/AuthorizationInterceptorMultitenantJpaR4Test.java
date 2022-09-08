package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("Duplicates")
public class AuthorizationInterceptorMultitenantJpaR4Test extends BaseMultitenantResourceProviderR4Test implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptorMultitenantJpaR4Test.class);

	@Test
	public void testCreateInTenant_Allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().create().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue());

		runInTransaction(() -> {
			Optional<ResourceTable> patient = myResourceTableDao.findById(idA.getIdPartAsLong());
			assertTrue(patient.isPresent());
		});
	}

	@Test
	public void testCreateInTenant_Blocked() {
		createPatient(withTenant(TENANT_A), withActiveTrue());
		IIdType idB = createPatient(withTenant(TENANT_B), withActiveFalse());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().create().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		try {
			myClient.read().resource(Patient.class).withId(idB).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testReadInTenant_Allowed() {
		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_B), withActiveFalse());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_A);
		Patient p = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(p.getActive());
	}

	@Test
	public void testReadInTenant_Blocked() {
		createPatient(withTenant(TENANT_A), withActiveTrue());
		IIdType idB = createPatient(withTenant(TENANT_B), withActiveFalse());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_B);
		try {
			myClient.read().resource(Patient.class).withId(idB).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testReadInDefaultTenant_Allowed() {
		IIdType idA = createPatient(withTenant("DEFAULT"), withActiveTrue());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds("DEFAULT")
			.build());

		myTenantClientInterceptor.setTenantId("DEFAULT");
		Patient p = myClient.read().resource(Patient.class).withId(idA).execute();
		assertTrue(p.getActive());
	}

	@Test
	public void testReadInDefaultTenant_Blocked() {
		IIdType idA = createPatient(withTenant(TENANT_A), withActiveTrue());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds("DEFAULT")
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_A);
		try {
			myClient.read().resource(Patient.class).withId(idA).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testReadAcrossTenants_Allowed() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		IIdType patientId = createPatient(withTenant(TENANT_A), withActiveTrue());
		createObservation(withTenant(TENANT_B), withSubject(patientId.toUnqualifiedVersionless()));

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A, TENANT_B)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_B);

		Bundle output = myClient
			.search()
			.forResource("Observation")
			.include(Observation.INCLUDE_ALL)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(2, output.getEntry().size());
	}

	@Test
	public void testReadAcrossTenants_Blocked() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		IIdType patientId = createPatient(withTenant(TENANT_A), withActiveTrue());
		createObservation(withTenant(TENANT_B), withSubject(patientId.toUnqualifiedVersionless()));

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_B);

		try {
			myClient
				.search()
				.forResource("Observation")
				.include(Observation.INCLUDE_ALL)
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testSearchPagingAcrossTenants_Blocked() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		// Create 9 Observations: 1-8 have no subject, 9 has a subject in a different tenant
		IIdType patientIdA = createPatient(withTenant(TENANT_A), withActiveTrue()).toUnqualifiedVersionless();
		IIdType patientIdB = createPatient(withTenant(TENANT_B), withActiveTrue()).toUnqualifiedVersionless();
		List<IIdType> observationIds = Lists.newArrayList();
		for (int i = 1; i <= 9; i++) {
			IIdType subject = i == 9 ? patientIdB : patientIdA;
			IIdType id = createObservation(withTenant(TENANT_A), withIdentifier("foo" + i, "val" + i), withStatus("final"), withSubject(subject)).toUnqualifiedVersionless();
			observationIds.add(id);
		}

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_A);

		// Search and fetch the first 3
		Bundle bundle = myClient
			.search()
			.forResource("Observation")
			.include(Observation.INCLUDE_ALL)
			.sort().ascending(Observation.IDENTIFIER)
			.returnBundle(Bundle.class)
			.count(3)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).setEncodeElements(Sets.newHashSet("Bundle.link")).encodeResourceToString(bundle));
		assertThat(toUnqualifiedVersionlessIds(bundle).toString(), toUnqualifiedVersionlessIds(bundle), contains(observationIds.get(0), observationIds.get(1), observationIds.get(2), patientIdA));

		// Fetch the next 3
		bundle = myClient
			.loadPage()
			.next(bundle)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).setEncodeElements(Sets.newHashSet("Bundle.link")).encodeResourceToString(bundle));
		assertThat(toUnqualifiedVersionlessIds(bundle).toString(), toUnqualifiedVersionlessIds(bundle), contains(observationIds.get(3), observationIds.get(4), observationIds.get(5), patientIdA));

		// Fetch the next 3 - This should fail as the last observation has a cross-partition reference
		try {
			bundle = myClient
				.loadPage()
				.next(bundle)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {
			// good
		}
	}

	@Test
	public void testPaginNextUrl_Blocked() {
		// We're going to create 4 patients, then request all patients, giving us two pages of results
		myPagingProvider.setMaximumPageSize(2);

		createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_A), withActiveTrue());
		createPatient(withTenant(TENANT_A), withActiveTrue());

		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A)
			.build());

		myTenantClientInterceptor.setTenantId(TENANT_A);

		Bundle patientBundle = myClient
			.search()
			.forResource("Patient")
			.include(Observation.INCLUDE_ALL)
			.returnBundle(Bundle.class)
			.execute();

		Assertions.assertTrue(patientBundle.hasLink());
		Assertions.assertTrue(patientBundle.getLink().stream().anyMatch(link -> link.hasRelation() && link.getRelation().equals("next")));
		String nextLink = patientBundle.getLink().stream().filter(link -> link.hasRelation() && link.getRelation().equals("next")).findFirst().get().getUrl();
		assertThat(nextLink, not(blankOrNullString()));

		// Now come in as an imposter from a diff tenant with a stolen next link
		// Request as a user with only access to TENANT_B
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_B)
			.build());

		try {
			Bundle resp2 = myClient.search().byUrl(nextLink).returnBundle(Bundle.class).execute();
			fail();
		} catch (ForbiddenOperationException e) {
			Assertions.assertEquals("HTTP 403 Forbidden: HAPI-0334: Access denied by default policy (no applicable rules)", e.getMessage());
		}
	}
}
