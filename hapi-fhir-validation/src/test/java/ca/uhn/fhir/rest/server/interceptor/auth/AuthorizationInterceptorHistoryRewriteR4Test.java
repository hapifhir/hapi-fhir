package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Authorization of versioned updates (an {@literal If-Match} header) and genuine history
 * rewrites ({@literal X-Rewrite-History: true}) against
 * {@code allow().updateHistoryRewrite().allRequests()}. The history rewrite rule is an additional
 * gate on top of ordinary write authority, never a write grant of its own.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorHistoryRewriteR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static boolean ourHitMethod;

	@RegisterExtension
	static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
			.registerProvider(new PatientProvider())
			.registerProvider(new ObservationProvider())
			.setDefaultResponseEncoding(EncodingEnum.JSON);

	@BeforeEach
	void before() {
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourHitMethod = false;
	}

	@Test
	void update_typeScopedWriteWithRewriteRule_noVersion_isDenied() {
		registerRules(b -> b.allow().write().resourcesOfType("Observation").withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", false, false, 403);
	}

	@Test
	void update_typeScopedWriteWithRewriteRule_ifMatchOnOtherType_isDenied() {
		registerRules(b -> b.allow().write().resourcesOfType("Observation").withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, false, 403);
	}

	@Test
	void update_typeScopedWriteWithRewriteRule_rewriteOfOtherType_isDenied() {
		registerRules(b -> b.allow().write().resourcesOfType("Observation").withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, true, 403);
	}

	@Test
	void update_typeScopedWriteWithRewriteRule_ifMatchOnGrantedType_isAllowed() {
		registerRules(b -> b.allow().write().resourcesOfType("Observation").withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Observation", true, false, 200);
	}

	@Test
	void update_typeScopedWriteWithRewriteRule_rewriteOfGrantedType_isAllowed() {
		registerRules(b -> b.allow().write().resourcesOfType("Observation").withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Observation", true, true, 200);
	}

	@Test
	void update_rewriteRuleOnly_rewrite_isDenied() {
		registerRules(b -> b.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, true, 403);
	}

	@Test
	void update_rewriteRuleOnly_ifMatch_isDenied() {
		registerRules(b -> b.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, false, 403);
	}

	@Test
	void update_allWriteWithRewriteRule_rewrite_isAllowed() {
		registerRules(b -> b.allow().write().allResources().withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, true, 200);
	}

	@Test
	void update_allWriteWithoutRewriteRule_rewrite_isDenied() {
		registerRules(b -> b.allow().write().allResources().withAnyId().build());

		assertUpdateStatus("Patient", true, true, 403);
	}

	@Test
	void update_allWriteWithoutRewriteRule_ifMatch_isAllowed() {
		registerRules(b -> b.allow().write().allResources().withAnyId().build());

		assertUpdateStatus("Patient", true, false, 200);
	}

	@Test
	void update_denyRuleBeforeRewriteRule_rewriteOfDeniedType_isDenied() {
		registerRules(b -> b.deny().write().resourcesOfType("Patient").withAnyId().andThen()
				.allow().write().allResources().withAnyId().andThen()
				.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, true, 403);
	}

	@Test
	void update_defaultAllowWithDenyRuleAndRewriteRule_rewriteOfDeniedType_isDenied() {
		registerRules(
				PolicyEnum.ALLOW,
				b -> b.deny().write().resourcesOfType("Patient").withAnyId().andThen()
						.allow().updateHistoryRewrite().allRequests().build());

		assertUpdateStatus("Patient", true, true, 403);
	}

	private void registerRules(Function<RuleBuilder, List<IAuthRule>> theRules) {
		registerRules(PolicyEnum.DENY, theRules);
	}

	private void registerRules(PolicyEnum theDefaultPolicy, Function<RuleBuilder, List<IAuthRule>> theRules) {
		ourServer.registerInterceptor(new AuthorizationInterceptor(theDefaultPolicy) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules.apply(new RuleBuilder());
			}
		});
	}

	private void assertUpdateStatus(
			String theResourceType, boolean theIfMatch, boolean theRewriteHistory, int theExpectedStatus) {
		HttpTestRequest request = ourServer.fhirRequest("/" + theResourceType + "/1");
		if (theIfMatch) {
			request.withHeader(Constants.HEADER_IF_MATCH, "W/\"1\"");
		}
		if (theRewriteHistory) {
			request.withHeader(Constants.HEADER_REWRITE_HISTORY, "true");
		}
		IBaseResource body = "Observation".equals(theResourceType)
				? new Observation().setStatus(Observation.ObservationStatus.FINAL).setId("Observation/1")
				: new Patient().setActive(true).setId("Patient/1");

		request.put(body).assertStatus(theExpectedStatus);

		assertThat(ourHitMethod).as("update method invoked").isEqualTo(theExpectedStatus == 200);
	}

	public static class PatientProvider implements IResourceProvider {
		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient theResource) {
			ourHitMethod = true;
			return new MethodOutcome(theId.withVersion("2"));
		}
	}

	public static class ObservationProvider implements IResourceProvider {
		@Override
		public Class<Observation> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theResource) {
			ourHitMethod = true;
			return new MethodOutcome(theId.withVersion("2"));
		}
	}
}
