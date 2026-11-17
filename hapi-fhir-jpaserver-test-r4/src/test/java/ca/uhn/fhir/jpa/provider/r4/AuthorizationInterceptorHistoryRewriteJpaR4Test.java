package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRuleBuilder;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Authorization of versioned updates (an {@literal If-Match} header) and genuine history rewrites
 * ({@literal X-Rewrite-History: true}) against {@code allow().updateHistoryRewrite().allRequests()}, on a JPA
 * server with history rewrite enabled. The history rewrite rule is an additional gate on top of ordinary write
 * authority, never a write grant of its own.
 * <p>
 * Covers both authorization points: {@code SERVER_INCOMING_REQUEST_PRE_HANDLED} and the storage-time re-check
 * at {@code STORAGE_PRESTORAGE_RESOURCE_UPDATED}.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorHistoryRewriteJpaR4Test extends BaseResourceProviderR4Test {

	private static final String ORIGINAL = "ORIGINAL";
	private static final String TAMPERED = "TAMPERED";

	private IIdType myPatientId;
	private IIdType myObservationId;

	@BeforeEach
	void beforeEach() {
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(true);

		myPatientId = createPatientWithTwoVersions();
		myObservationId = createObservationWithTwoVersions();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(new JpaStorageSettings().isUpdateWithHistoryRewriteEnabled());
		myServer.getRestfulServer()
				.getInterceptorService()
				.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
	}

	/**
	 * Write is scoped to Observation; the history rewrite rule must not widen it to other types.
	 */
	@Nested
	class TypeScopedWriteWithRewriteRule {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().resourcesOfType("Observation").withAnyId().andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_otherType_isDeniedAndNotStored() {
			request("/Patient/" + myPatientId.getIdPart())
					.put(patient(myPatientId.toVersionless(), TAMPERED))
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_ifMatchOnOtherType_isDeniedAndNotStored() {
			ifMatchUpdatePatient().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_rewriteOfOtherType_isDeniedAndHistoryIntact() {
			rewritePatientVersion1().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void patch_rewriteOfOtherType_isDeniedAndHistoryIntact() {
			request("/Patient/" + myPatientId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.patch(replaceFamilyPatch())
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_ifMatchOnGrantedType_createsNewVersion() {
			request("/Observation/" + myObservationId.getIdPart())
					.withHeader(Constants.HEADER_IF_MATCH, "W/\"2\"")
					.put(observation(myObservationId.toVersionless(), TAMPERED))
					.assertStatus(200);

			assertThat(observationHistoryCodes()).containsExactly(TAMPERED, ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_rewriteOfGrantedType_rewritesInPlace() {
			request("/Observation/" + myObservationId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.put(observation(myObservationId.withVersion("1"), TAMPERED))
					.assertStatus(200);

			assertThat(observationHistoryCodes()).containsExactly(ORIGINAL + "-2", TAMPERED);
		}
	}

	/**
	 * No write rule at all: the history rewrite rule alone grants nothing.
	 */
	@Nested
	class RewriteRuleOnly {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_ifMatch_isDeniedAndNotStored() {
			ifMatchUpdatePatient().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_rewrite_isDeniedAndHistoryIntact() {
			rewritePatientVersion1().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	@Nested
	class AllWriteWithoutRewriteRule {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().allResources().withAnyId().build());
		}

		/** A versioned update is an ordinary write and does not need the rewrite rule */
		@Test
		void update_ifMatch_createsNewVersion() {
			ifMatchUpdatePatient().assertStatus(200);

			assertThat(patientHistoryFamilies()).containsExactly(TAMPERED, ORIGINAL + "-2", ORIGINAL + "-1");
		}

		@Test
		void update_rewrite_isDeniedAndHistoryIntact() {
			rewritePatientVersion1().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	@Nested
	class AllWriteWithRewriteRule {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().allResources().withAnyId().andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_rewrite_rewritesInPlace() {
			rewritePatientVersion1().assertStatus(200);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", TAMPERED);
		}
	}

	/**
	 * A deny rule ahead of the write and rewrite rules still wins for the denied type.
	 */
	@Nested
	class DenyRuleBeforeRewriteRule {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.deny().write().resourcesOfType("Patient").withAnyId().andThen()
					.allow().write().allResources().withAnyId().andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_rewriteOfDeniedType_isDeniedAndHistoryIntact() {
			rewritePatientVersion1().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	/**
	 * Default policy ALLOW: the rewrite rule must not override an explicit deny of the type.
	 */
	@Nested
	class DefaultAllowWithDenyRuleAndRewriteRule {

		@BeforeEach
		void beforeEach() {
			registerRules(
					PolicyEnum.ALLOW,
					b -> b.deny().write().resourcesOfType("Patient").withAnyId().andThen()
							.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_rewriteOfDeniedType_isDeniedAndHistoryIntact() {
			rewritePatientVersion1().assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	/**
	 * Write is scoped to Observation, plus {@code allow().patch().allRequests()} (what CDR builds for
	 * {@code FHIR_PATCH}). The patch rule allows every PATCH before storage and relies on the storage-time
	 * re-check, which a rewrite of a non-current version never fires.
	 */
	@Nested
	class TypeScopedWriteWithPatchAndRewriteRules {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().resourcesOfType("Observation").withAnyId().andThen()
					.allow().patch().allRequests().andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void patch_rewriteOfNonCurrentVersionOfOtherType_isDeniedAndHistoryIntact() {
			request("/Patient/" + myPatientId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.patch(replaceFamilyPatch())
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		/** Control: rewriting the current version fires the storage-time re-check */
		@Test
		void patch_rewriteOfCurrentVersionOfOtherType_isDeniedAndHistoryIntact() {
			request("/Patient/" + myPatientId.getIdPart() + "/_history/2")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.patch(replaceFamilyPatch())
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		/** Control: an ordinary PATCH is re-checked at storage time */
		@Test
		void patch_ordinaryOfOtherType_isDeniedAndNotStored() {
			request("/Patient/" + myPatientId.getIdPart())
					.patch(replaceFamilyPatch())
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	/**
	 * Same as {@link TypeScopedWriteWithPatchAndRewriteRules} but with no history rewrite rule at all: the
	 * rewrite must not be possible without the rewrite permission.
	 */
	@Nested
	class TypeScopedWriteWithPatchRuleOnly {

		@BeforeEach
		void beforeEach() {
			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().resourcesOfType("Observation").withAnyId().andThen()
					.allow().patch().allRequests().build());
		}

		@Test
		void patch_rewriteOfNonCurrentVersionOfOtherType_isDeniedAndHistoryIntact() {
			request("/Patient/" + myPatientId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.patch(replaceFamilyPatch())
					.assertStatus(403);

			assertThat(patientHistoryFamilies()).containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}
	}

	/**
	 * Write is limited to one patient's compartment. Before storage only the incoming body is checked; the
	 * existing record is checked at storage time, which a rewrite of a non-current version never fires.
	 */
	@Nested
	class CompartmentScopedWriteWithRewriteRule {

		private IIdType myOtherPatientId;
		private IIdType myOtherPatientObservationId;
		private IIdType myOwnObservationId;

		@BeforeEach
		void beforeEach() {
			myOtherPatientId = myPatientDao.create(patient(null, "OTHER"), mySrd).getId().toUnqualifiedVersionless();
			myOtherPatientObservationId = createObservationWithTwoVersions(myOtherPatientId);
			myOwnObservationId = createObservationWithTwoVersions(myPatientId);

			registerRules(b -> b.allow().read().allResources().withAnyId().andThen()
					.allow().write().allResources().inCompartment("Patient", myPatientId).andThen()
					.allow().updateHistoryRewrite().allRequests().build());
		}

		@Test
		void update_rewriteOfNonCurrentVersionOfOtherPatientsObservation_isDeniedAndHistoryIntact() {
			request("/Observation/" + myOtherPatientObservationId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.put(observation(myOtherPatientObservationId.withVersion("1"), TAMPERED, myPatientId))
					.assertStatus(403);

			assertThat(observationHistoryCodes(myOtherPatientObservationId))
					.containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		/** Control: rewriting the current version fires the storage-time re-check of the existing record */
		@Test
		void update_rewriteOfCurrentVersionOfOtherPatientsObservation_isDeniedAndHistoryIntact() {
			request("/Observation/" + myOtherPatientObservationId.getIdPart() + "/_history/2")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.put(observation(myOtherPatientObservationId.withVersion("2"), TAMPERED, myPatientId))
					.assertStatus(403);

			assertThat(observationHistoryCodes(myOtherPatientObservationId))
					.containsExactly(ORIGINAL + "-2", ORIGINAL + "-1");
		}

		/** Control: the legitimate case still works */
		@Test
		void update_rewriteOfNonCurrentVersionOfOwnObservation_rewritesInPlace() {
			request("/Observation/" + myOwnObservationId.getIdPart() + "/_history/1")
					.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
					.put(observation(myOwnObservationId.withVersion("1"), TAMPERED, myPatientId))
					.assertStatus(200);

			assertThat(observationHistoryCodes(myOwnObservationId)).containsExactly(ORIGINAL + "-2", TAMPERED);
		}
	}

	private void registerRules(Function<IAuthRuleBuilder, List<IAuthRule>> theRules) {
		registerRules(PolicyEnum.DENY, theRules);
	}

	private void registerRules(PolicyEnum theDefaultPolicy, Function<IAuthRuleBuilder, List<IAuthRule>> theRules) {
		myServer.getRestfulServer()
				.getInterceptorService()
				.registerInterceptor(new AuthorizationInterceptor(theDefaultPolicy) {
					@Override
					public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
						return theRules.apply(new RuleBuilder());
					}
				});
	}

	private HttpTestRequest request(String thePath) {
		return myServer.fhirRequest(thePath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
	}

	/** Versioned update of the current patient version: creates version 3 if allowed */
	private HttpTestResponse ifMatchUpdatePatient() {
		return request("/Patient/" + myPatientId.getIdPart())
				.withHeader(Constants.HEADER_IF_MATCH, "W/\"2\"")
				.put(patient(myPatientId.toVersionless(), TAMPERED));
	}

	/** History rewrite of the non-current patient version 1 */
	private HttpTestResponse rewritePatientVersion1() {
		return request("/Patient/" + myPatientId.getIdPart() + "/_history/1")
				.withHeader(Constants.HEADER_REWRITE_HISTORY, "true")
				.put(patient(myPatientId.withVersion("1"), TAMPERED));
	}

	private static String replaceFamilyPatch() {
		return "[ { \"op\": \"replace\", \"path\": \"/name/0/family\", \"value\": \"" + TAMPERED + "\" } ]";
	}

	private IIdType createPatientWithTwoVersions() {
		IIdType id = myPatientDao.create(patient(null, ORIGINAL + "-1"), mySrd).getId().toUnqualifiedVersionless();
		myPatientDao.update(patient(id, ORIGINAL + "-2"), mySrd);
		return id;
	}

	private IIdType createObservationWithTwoVersions() {
		IIdType id = myObservationDao.create(observation(null, ORIGINAL + "-1"), mySrd)
				.getId()
				.toUnqualifiedVersionless();
		myObservationDao.update(observation(id, ORIGINAL + "-2"), mySrd);
		return id;
	}

	private IIdType createObservationWithTwoVersions(IIdType theSubject) {
		IIdType id = myObservationDao.create(observation(null, ORIGINAL + "-1", theSubject), mySrd)
				.getId()
				.toUnqualifiedVersionless();
		myObservationDao.update(observation(id, ORIGINAL + "-2", theSubject), mySrd);
		return id;
	}

	private static Patient patient(IIdType theId, String theFamily) {
		Patient patient = new Patient();
		patient.setId(theId);
		patient.setActive(true);
		patient.addName().setFamily(theFamily);
		return patient;
	}

	private static Observation observation(IIdType theId, String theCode) {
		Observation observation = new Observation();
		observation.setId(theId);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem("http://example.com").setCode(theCode);
		return observation;
	}

	private static Observation observation(IIdType theId, String theCode, IIdType theSubject) {
		Observation observation = observation(theId, theCode);
		observation.setSubject(new Reference(theSubject));
		return observation;
	}

	/** Family name of every stored version of the patient, highest version first */
	private List<String> patientHistoryFamilies() {
		return historyByVersion(myPatientDao, myPatientId, r -> ((Patient) r).getNameFirstRep().getFamily());
	}

	/** Code of every stored version of the observation, highest version first */
	private List<String> observationHistoryCodes() {
		return observationHistoryCodes(myObservationId);
	}

	private List<String> observationHistoryCodes(IIdType theId) {
		return historyByVersion(
				myObservationDao, theId, r -> ((Observation) r).getCode().getCodingFirstRep().getCode());
	}

	/** Sorted by version, not by history order: a rewrite moves the rewritten version's lastUpdated forward */
	private List<String> historyByVersion(
			IFhirResourceDao<?> theDao, IIdType theId, Function<IBaseResource, String> theMarker) {
		return theDao.history(theId, new HistorySearchDateRangeParam(), mySrd).getResources(0, 100).stream()
				.sorted(Comparator.comparing((IBaseResource r) -> r.getIdElement().getVersionIdPartAsLong())
						.reversed())
				.map(theMarker)
				.toList();
	}
}
