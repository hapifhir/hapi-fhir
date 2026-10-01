package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * On a multitenant server, a type-level read limited to the request's tenant qualifies for a
 * <code>_has</code> search, since the search only reaches that tenant's resources.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorReverseChainMultitenantJpaR4Test extends BaseMultitenantResourceProviderR4Test {

	private static final String LOINC = "http://loinc.org";
	private static final String HIV_VIRAL_LOAD = "20447-9";
	private static final String HAS_HIV_OBSERVATION = "Patient?_has:Observation:subject:code=" + HIV_VIRAL_LOAD;

	private IIdType myPatientA;

	@BeforeEach
	void createData() {
		myPatientA = createPatient(withTenant(TENANT_A), withActiveTrue()).toUnqualifiedVersionless();
		createObservation(withTenant(TENANT_A), withSubject(myPatientA), withObservationCode(LOINC, HIV_VIRAL_LOAD));

		IIdType patientB = createPatient(withTenant(TENANT_B), withActiveTrue()).toUnqualifiedVersionless();
		createObservation(withTenant(TENANT_B), withSubject(patientB), withObservationCode(LOINC, HIV_VIRAL_LOAD));
	}

	@Test
	void testHas_typeReadsForRequestTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().forTenantIds(TENANT_A).andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void testHas_readAllResourcesForRequestTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void testHas_observationReadNotForOtherTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().notForTenantIds(TENANT_B).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void testHas_observationReadForOtherTenantOnly_forbidden() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThatThrownBy(() -> searchInTenant(TENANT_B)).isInstanceOf(ForbiddenOperationException.class);
	}

	@Test
	void testHas_observationDeniedForOtherTenantThenReadAll_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_B).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void testHas_observationDeniedForRequestTenantThenReadAll_forbidden() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertThatThrownBy(() -> searchInTenant(TENANT_A)).isInstanceOf(ForbiddenOperationException.class);
	}

	private List<String> searchInTenant(String theTenantId) {
		// byUrl() is an explicit URL, which the client's tenant interceptor leaves untouched
		String url = myClient.getServerBase() + "/" + theTenantId + "/" + HAS_HIV_OBSERVATION;
		Bundle bundle = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
		return bundle.getEntry().stream()
			.map(entry -> entry.getResource().getIdElement().getIdPart())
			.toList();
	}
}
