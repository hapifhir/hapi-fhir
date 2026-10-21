package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * AuthorizationInterceptor checks <code>_has</code> parameters on a plain RestfulServer, without JPA storage.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorReverseChainR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private final PatientProvider myPatientProvider = new PatientProvider();

	@RegisterExtension
	private final RestfulServerExtension myServer =
			new RestfulServerExtension(ourCtx).registerProvider(myPatientProvider);

	@Test
	void searchHas_readPatientOnly_forbiddenBeforeProviderIsCalled() {
		withAuthorizationInterceptor(new RuleBuilder()
				.allow().read().resourcesOfType("Patient").withAnyId().andThen()
				.build());

		assertThatThrownBy(() -> myServer.getFhirClient()
						.search()
						.byUrl("Patient?_has:Observation:subject:code=20447-9")
						.returnBundle(Bundle.class)
						.execute())
				.isInstanceOf(ForbiddenOperationException.class);
		assertThat(myPatientProvider.mySearchCount).isZero();
	}

	@Test
	void searchHas_readPatientAndObservationType_allowed() {
		withAuthorizationInterceptor(new RuleBuilder()
				.allow().read().resourcesOfType("Patient").withAnyId().andThen()
				.allow().read().resourcesOfType("Observation").withAnyId().andThen()
				.build());

		Bundle bundle = myServer.getFhirClient()
				.search()
				.byUrl("Patient?_has:Observation:subject:code=20447-9")
				.returnBundle(Bundle.class)
				.execute();

		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(myPatientProvider.mySearchCount).isEqualTo(1);
	}

	private void withAuthorizationInterceptor(List<IAuthRule> theRules) {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		};
		interceptor.setFlags(AuthorizationFlagsEnum.ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
		myServer.getInterceptorService().registerInterceptor(interceptor);
	}

	public static class PatientProvider implements IResourceProvider {

		private int mySearchCount;

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> search(@OptionalParam(name = "_has") HasAndListParam theHas) {
			mySearchCount++;
			Patient patient = new Patient();
			patient.setId("Patient/pA");
			return List.of(patient);
		}
	}
}
