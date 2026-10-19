package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.ENFORCE_REVERSE_CHAINED_AUTHORIZATION;
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

	private IIdType myPatientA;

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		createData();
	}

	private void createData() {
		myPatientA = createPatient(withTenant(TENANT_A), withActiveTrue()).toUnqualifiedVersionless();
		createObservation(withTenant(TENANT_A), withSubject(myPatientA), withObservationCode(LOINC, HIV_VIRAL_LOAD));

		IIdType patientB = createPatient(withTenant(TENANT_B), withActiveTrue()).toUnqualifiedVersionless();
		createObservation(withTenant(TENANT_B), withSubject(patientB), withObservationCode(LOINC, HIV_VIRAL_LOAD));
	}

	@Test
	void searchHas_typeReadsForRequestTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().forTenantIds(TENANT_A).andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void searchHas_readAllResourcesForRequestTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().allResources().withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void searchHas_observationReadNotForOtherTenant_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().notForTenantIds(TENANT_B).andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void searchHas_observationReadForOtherTenantOnly_forbidden() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.build());

		assertThatThrownBy(() -> searchInTenant(TENANT_B)).isInstanceOf(ForbiddenOperationException.class);
	}

	@Test
	void searchHas_observationDeniedForOtherTenantThenReadAll_allowed() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_B).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertThat(searchInTenant(TENANT_A)).containsExactly(myPatientA.getIdPart());
	}

	@Test
	void searchHas_observationDeniedForRequestTenantThenReadAll_forbidden() {
		setupAuthorizationInterceptorWithRules(() -> new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().forTenantIds(TENANT_A).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertThatThrownBy(() -> searchInTenant(TENANT_A)).isInstanceOf(ForbiddenOperationException.class);
	}

	@Override
	public void setupAuthorizationInterceptorWithRules(Supplier<List<IAuthRule>> theRuleSupplier) {
		super.setupAuthorizationInterceptorWithRules(theRuleSupplier);
		myAuthorizationInterceptor.setFlags(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
	}

	private List<String> searchInTenant(String theTenantId) {
		// byUrl() is an explicit URL, which the client's tenant interceptor leaves untouched
		String url = myClient.getServerBase() + "/" + theTenantId + "/Patient?_has:Observation:subject:code=20447-9";
		Bundle bundle = myClient.search().byUrl(url).returnBundle(Bundle.class).execute();
		return bundle.getEntry().stream()
			.map(entry -> entry.getResource().getIdElement().getIdPart())
			.toList();
	}
}
