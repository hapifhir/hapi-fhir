package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers what happens when a transaction Bundle updates a Patient at the same time as an
 * interceptor firing on a different resource performs an update {@code dao.update()}
 * on the Patient updated by the Bundle.
 * <p>
 * Before fixing the re-entrant issue, both writes committed and {@code HFJ_RESOURCE.RES_VER} was left pointing at a
 * version that was not the highest one present in {@code HFJ_RES_VER}. The fix has two halves, and
 * the cases below are split along that seam:
 * </p>
 * <ul>
 * <li><b>Re-entrancy support.</b> Where the client sent no {@code If-Match}, the re-entrant write is allowed and
 *     the version state is kept consistent - the current-version pointer agrees with the history
 *     table and the history rows stay a gapless {@code 1..N}.</li>
 * <li><b>Rejecting.</b> Where the client did send {@code If-Match}, the precondition is re-checked at
 *     the point of the real write. The interceptor has by then moved the resource past the version
 *     the client demanded, so the transaction is refused with a 409 rather than silently discarding
 *     the interceptor's write.</li>
 * </ul>
 */
public class TransactionReentrantUpdateR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionReentrantUpdateR4Test.class);

	private static final String PATIENT_ID = "Patient/reentrant-update";
	private static final String FLAG_IDENTIFIER_SYSTEM = "http://tempuri.org";
	private static final String FLAG_IDENTIFIER_VALUE = "f1";
	private static final String REENTRANT_FAMILY_NAME = "REENTRANT";
	private static final String OBSERVATION_ID = "Observation/reentrant-delete-trigger";

	/**
	 * Re-entrancy support. Whatever writes are happening during the course of a transaction, the version state post
	 * transaction completion must be consistent:
	 * {@code HFJ_RESOURCE.RES_VER} points at the newest {@code HFJ_RES_VER} row, the history rows form a
	 * gapless {@code 1..N}, and the transaction response advertises a version the database actually
	 * holds.
	 * <p>
	 * Every case here asserts version state directly and expects the transaction to succeed deliberately omitting the
	 * {@code If-Match} covered by tests in {@link IfMatchRevalidatedAtTheWrite}. Adding a precondition here would
	 * replace this coverage with a duplicate of that group.
	 * </p>
	 */
	@Nested
	class CurrentVersionMatchesHistory {

		/**
		 * <p>
		 * Identical to
		 * {@link IfMatchRevalidatedAtTheWrite#testTransactionUpdateWithIfMatch_whenInterceptorUpdatesSameResource_throwsVersionConflict()}
		 * without the {@code If-Match} supplementing the Patient entry.
		 * </p>
		 */
		@Test
		void testTransactionUpdate_whenInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);

			// Execute - the Patient PUT takes v1 to v2, and the Flag create fires the interceptor, which
			// updates the same Patient again from inside the transaction
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildTransactionWithoutIfMatch());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("Two writes to one Patient in one transaction should leave it at v3")
					.isEqualTo(3L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L, 2L, 3L);
			});

			assertResponseEntryVersionMatchesDatabase(response, 0);
		}

		/**
		 * Control for
		 * {@link IfMatchRevalidatedAtTheWrite#testTransactionUpdateWithIfMatch_whenInterceptorUpdatesSameResource_throwsVersionConflict()}:
		 * the same Bundle with no interceptor registered.
		 */
		@Test
		public void testTransactionUpdate_withNoInterceptor_currentVersionMatchesHistory() {
			// Setup - Patient v1
			Patient patient = new Patient();
			patient.setId(PATIENT_ID);
			patient.setActive(true);
			myPatientDao.update(patient, new SystemRequestDetails());

			// build a transaction bundle with a versioned PUT of the Patient alongside a PUT of a Flag that references
			// it
			Bundle transactionBundle = buildTransaction();

			// Execute
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);

			// Verify
			assertPatientVersionsAreConsistent(response);
		}

		/**
		 * A re-entrant <b>delete</b>: the Bundle updates the Patient and an interceptor deletes that same
		 * Patient from inside the same transaction. This is update-then-delete on one resource, which
		 * matters because the delete's write goes through {@code updateEntityForDelete} rather than the
		 * ordinary update path, and it arrives with the version-updated flag from the Bundle's own write
		 * already set.
		 * <p>
		 * <b>Why an interceptor rather than two DAO calls.</b> A Bundle cannot express this on its own: entries are
		 * sorted by verb (DELETE=1, PUT=3), so a Bundle carrying both always runs the DELETE first, which is
		 * delete-then-update and is already covered by
		 * {@code FhirSystemDaoTransactionR5Test.testDeleteAndUpdateOnSameResource}. Nor does {@code $merge} with
		 * {@code deleteSource=true} reach it - that path skips the source update entirely, guarding it behind
		 * {@code if (!theIsDeleteSource)}. A customer interceptor calling {@code dao.delete()} on a resource the Bundle
		 * is updating is the realistic (but very twisted) way to get here, and it is the same re-entrancy as every
		 * other case in this class with {@code delete} in place of {@code update}.
		 * </p>
		 * <p>
		 * The trigger entry is an Observation rather than the Flag used elsewhere in this class, because
		 * those Flags reference the Patient and referential integrity would block the delete. The
		 * Observation carries no subject, so the Patient is unreferenced and can be deleted.
		 * </p>
		 * <p>
		 * Ordering was confirmed rather than assumed: when the hook fires the Patient already reports v2
		 * with its version bump still unflushed, so the delete is genuinely the second write and reaches
		 * {@code updateEntityForDelete} with the flag set.
		 * </p>
		 */
		@Test
		void testTransactionUpdate_whenInterceptorDeletesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1 and the Observation whose update fires the interceptor
			createPatientVersionOne();
			createObservationVersionOne();

			ReentrantDeleteInterceptor interceptor = new ReentrantDeleteInterceptor(myPatientDao);
			registerInterceptor(interceptor);

			// Execute - the Bundle's PUT takes the Patient to v2, then the Observation entry fires the
			// interceptor, which deletes the same Patient from inside the transaction
			mySystemDao.transaction(new SystemRequestDetails(), buildPatientPlusObservationTransaction());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("An update and a delete in one transaction should leave the Patient at v3")
					.isEqualTo(3L);

				List<ResourceHistoryTable> historyRows = findPatientHistoryRows(currentRow);
				assertThat(historyRows.get(historyRows.size() - 1).isDeleted())
					.as("The newest history row should be the deletion")
					.isTrue();
			});

			assertThatThrownBy(() -> myPatientDao.read(new IdType(PATIENT_ID), new SystemRequestDetails()))
				.as("A versionless read of a deleted resource should report it gone")
				.isInstanceOf(ResourceGoneException.class);
		}

		/**
		 * a POST entry fires the interceptor before the Patient's own PUT entry is ever walked since entries are sorted
		 * by verb (POST=2, PUT=3).
		 */
		@Test
		void testTransactionUpdate_whenPostEntryInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);

			// Execute
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildPostFlagTransaction(false));

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();
			assertResponseEntryVersionMatchesDatabase(response, 0);
		}

		@Test
		void testTransactionUpdate_whenSameVerbEntryInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);

			// Execute
			Bundle response =
				mySystemDao.transaction(new SystemRequestDetails(), buildFlagFirstPutOnlyTransaction());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();
			// The Patient is the second entry here, because the Flag has to come first in document order
			// for the interceptor to fire before the Patient's entry is walked.
			assertResponseEntryVersionMatchesDatabase(response, 1);
		}

		/**
		 * interceptor writing from STORAGE_PRESTORAGE_RESOURCE_UPDATED invoked from {@code updateInternal} immediately
		 * before pass 2's own call to {@code updateEntity}. Asserts that the If-Match re-check sit immediately before
		 * each individual write rather than once at the top of pass 2.
		 */
		@Test
		void testTransactionUpdate_whenPreStorageUpdatedInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			ReentrantPatientUpdateInterceptor interceptor = new ReentrantPatientUpdateInterceptor(myPatientDao);
			registerInterceptor(interceptor);

			// Execute
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildPatientOnlyTransaction());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();
			assertResponseEntryVersionMatchesDatabase(response, 0);
		}

		/**
		 * three writes to one Patient in one transaction: the Bundle's own PUT plus two interceptor
		 * writes, one per Flag. Proves the version repair generalises past N=2.
		 */
		@Test
		void testTransactionUpdate_whenInterceptorUpdatesSameResourceTwice_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);

			// Execute
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildTransactionWithTwoFlags());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor should have fired once per Flag")
				.isEqualTo(2);

			assertPatientStorageVersionsAreConsistent();

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("Three writes to one Patient in one transaction should leave it at v4")
					.isEqualTo(4L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L, 2L, 3L, 4L);
			});

			assertResponseEntryVersionMatchesDatabase(response, 0);
		}

		/**
		 * Inside a transaction all three STORAGE_PRECOMMIT_RESOURCE_* broadcasts are deferred and replayed after the
		 * Bundle's writes have been flushed, so an interceptor writing from there is safe today and must stay safe.
		 * <p>
		 * The transaction response is deliberately not asserted against the database here: the Bundle's own
		 * PUT legitimately advertises the version it wrote, and the interceptor moves the Patient past it
		 * afterwards.
		 * </p>
		 */
		@Test
		void testTransactionUpdate_whenPreCommitInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			PrecommitFlagInterceptor interceptor = new PrecommitFlagInterceptor(myPatientDao);
			registerInterceptor(interceptor);

			// Execute
			mySystemDao.transaction(new SystemRequestDetails(), buildTransaction());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();
		}

		/**
		 * the Bundle's own PUT is a genuine no-op while the interceptor still writes. assert the only new version
		 * should be the interceptor's.
		 */
		@Test
		void testTransactionNoOpUpdate_whenInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
			// Setup - Patient v1
			createPatientVersionOne();

			assertThat(myStorageSettings.isSuppressUpdatesWithNoChange())
				.as("This case only exercises the no-op path while no-change suppression is on")
				.isTrue();

			// The interceptor leaves the Patient byte-identical to the Bundle's own PUT body, so that the
			// Bundle's deferred write finds nothing to change.
			ConfigurableReentrantFlagInterceptor interceptor = new ConfigurableReentrantFlagInterceptor(
				myPatientDao, thePatient -> thePatient.setGender(Enumerations.AdministrativeGender.MALE));
			registerInterceptor(interceptor);

			// Execute
			mySystemDao.transaction(new SystemRequestDetails(), buildNoOpPatientTransaction());

			// Verify
			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			assertPatientStorageVersionsAreConsistent();

			runInTransaction(() -> {
				assertThat(historyVersionsOf(findPatientRow()))
					.as("Only the interceptor's write should have produced a new version")
					.containsExactly(1L, 2L);
			});
		}

		/**
		 * Pins the {@code @PostUpdate} half of the version fix: the "a version bump is pending"
		 * flag on {@link ResourceTable} must be cleared when Hibernate emits the {@code UPDATE} that
		 * carries it, not only when it emits the initial {@code INSERT}. With that callback removed the
		 * flag stays set for the rest of the transaction, so the second write's call to
		 * {@code markVersionUpdatedInCurrentTransaction()} silently does nothing.
		 * <p>
		 * <b>Why no other case in this class proves it.</b> At the default settings a stuck flag is
		 * invisible. {@code ResourceTable.toHistory(boolean)} carries a fallback that bumps the version
		 * itself whenever {@code getVersion()} still equals the version of the last history row written -
		 * which is exactly the state a stuck flag leaves behind. The fallback therefore stands in for the
		 * skipped bump and reproduces identical {@code RES_VER} and {@code HFJ_RES_VER} values. Deleting
		 * {@code @PostUpdate} leaves every other case in this class green.
		 * </p>
		 * <p>
		 * <b>Why this configuration exposes it.</b> With resource DB history disabled the fallback is not
		 * on the path at all: {@code createHistoryEntry} reuses the existing row rather than creating one,
		 * and finds it by looking up {@code getVersion() - 1}. A stuck flag makes that lookup miss by one,
		 * the row is not found, and the code drops through to {@code toHistory} - which creates a
		 * <em>second</em> {@code HFJ_RES_VER} row. The version the transaction superseded is then left in
		 * the database and stays readable by vread, which is precisely what
		 * {@code setResourceDbHistoryEnabled(false)} undertakes to prevent. Removing the annotation leaves
		 * history rows {@code [2, 3]} here instead of {@code [3]}.
		 * </p>
		 * <p>
		 * The Bundle and the interceptor are the same ones
		 * {@link #testTransactionUpdate_whenInterceptorUpdatesSameResource_currentVersionMatchesHistory()}
		 * drives; only the storage setting differs.
		 * </p>
		 */
		@Test
		void testTransactionUpdateWithDbHistoryDisabled_whenInterceptorUpdatesSameResource_previousVersionIsExpunged() {
			// Setup - Patient v1, with resource history storage turned off
			myStorageSettings.setResourceDbHistoryEnabled(false);
			try {
				createPatientVersionOne();

				ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
				registerInterceptor(interceptor);

				// Execute - the Patient PUT takes v1 to v2, and the Flag create fires the interceptor, which
				// updates the same Patient again from inside the transaction
				mySystemDao.transaction(new SystemRequestDetails(), buildTransactionWithoutIfMatch());

				// Verify
				assertThat(interceptor.getInvocationCount())
					.as("Interceptor never fired, so this test proves nothing")
					.isEqualTo(1);

				long currentVersion = runInTransaction(() -> {
					ResourceTable currentRow = findPatientRow();
					assertThat(historyVersionsOf(currentRow))
						.as("With resource DB history disabled, two writes to one Patient in one transaction "
							+ "should leave exactly one HFJ_RES_VER row, holding the current version")
						.containsExactly(currentRow.getVersion());
					return currentRow.getVersion();
				});

				assertThatThrownBy(() -> myPatientDao.read(
						new IdType(PATIENT_ID).withVersion(Long.toString(currentVersion - 1)),
						new SystemRequestDetails()))
					.as("The version the second write superseded should have been expunged, not left readable")
					.isInstanceOf(ResourceNotFoundException.class);
			} finally {
				myStorageSettings.setResourceDbHistoryEnabled(new JpaStorageSettings().isResourceDbHistoryEnabled());
			}
		}
	}

	/**
	 * This test class asserts re-validation of the {@code If-Match} condition at write time. A precondition carried by
	 * a Bundle entry is checked while pass 1 walks the entries but a re-entrant interceptor can move the resource past
	 * the demanded version between that check and the real write in pass 2. The following tests assert that an update
	 * is rejected when the write version and precondition version have drifted.
	 * <p>
	 * The three refusal cases cover routes (a), (b) and (c) - the three places a re-entrant write can
	 * land relative to the Bundle's own write - and each asserts {@code Msg.code(3021)}.
	 * </p>
	 */
	@Nested
	class IfMatchRevalidatedAtTheWrite {

		/**
		 * This scenario covers the case where:
		 * <ul>
		 *   <li>a transaction Bundle carries a versioned {@code PUT} ({@code If-Match}) of a Patient</li>
		 *   <li>the Bundle also carries a conditional {@code PUT} of a Flag that references the Patient</li>
		 *   <li>a customer interceptor calls {@code dao.update()} on that same Patient from inside the hook</li>
		 * </ul>
		 * <p>
		 * The Bundle's {@code PUT} carries {@code If-Match: W/"1"}, but that precondition is validated
		 * during a pass that stores nothing, and the interceptor moves the Patient past v1 before the real
		 * write happens. Completing the write would silently discard the interceptor's version while
		 * reporting success, so the precondition is re-checked at the write and the whole transaction is
		 * refused.
		 * </p>
		 * <p>
		 * {@link CurrentVersionMatchesHistory#testTransactionUpdate_whenInterceptorUpdatesSameResource_currentVersionMatchesHistory()}
		 * drives the very same Bundle shape without the {@code If-Match}. It asserts that the version state is repaired
		 * rather than refusing the write, which is what keeps storage consistent when no precondition is involved.
		 * </p>
		 */
		@Test
		void testTransactionUpdateWithIfMatch_whenInterceptorUpdatesSameResource_throwsVersionConflict() {
			// Setup - Patient v1
			createPatientVersionOne();

			ReentrantFlagInterceptor interceptor = new ReentrantFlagInterceptor(myPatientDao);
			registerInterceptor(interceptor);

			// build a transaction bundle with a versioned PUT of the Patient alongside a PUT of a Flag that references
			// it
			Bundle transactionBundle = buildTransaction();

			// Execute + Verify - the Flag create fires the interceptor, which takes the Patient to v2, so
			// the Bundle's own PUT can no longer honour If-Match: W/"1"
			try {
				mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);
				fail("The interceptor invalidated If-Match W/\"1\" before the Bundle's own PUT was written, "
						+ "so that PUT must not silently discard the interceptor's version");
			} catch (ResourceVersionConflictException e) {
				assertThat(e.getMessage()).contains(Msg.code(3021));
			}

			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("The whole transaction, including the interceptor's write, should have rolled back")
					.isEqualTo(1L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L);
			});
		}

		/**
		 * the companion to
		 * {@link CurrentVersionMatchesHistory#testTransactionUpdate_whenSameVerbEntryInterceptorUpdatesSameResource_currentVersionMatchesHistory()},
		 * driving the identical Bundle with a precondition added.
		 * <p>
		 * This case is worth having beside
		 * {@link IfMatchRejectedDuringPassOne#testTransactionUpdateWithIfMatch_whenPostEntryInterceptorUpdatesSameResource_throwsVersionConflict()},
		 * because the two look like the same scenario and are caught by two entirely different checks. That one makes
		 * the Flag a {@code POST}, so pass 1 crosses a verb boundary and flushes, which makes the interceptor's write
		 * visible to the Patient entry's own pass-1 precondition check - and it is rejected there, with
		 * {@code HAPI-0989}. Here both entries are {@code PUT}, there is no verb boundary and no flush, so the pass-1
		 * check reads a version the interceptor has already moved past and lets the entry through. Nothing catches it
		 * until the precondition is re-validated at the real write, which is why this one reports {@code HAPI-3021}.
		 * </p>
		 */
		@Test
		void testTransactionUpdateWithIfMatch_whenSameVerbEntryInterceptorUpdatesSameResource_throwsVersionConflict() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);

			// build a transaction bundle whose entries are both PUTs - a conditional PUT of the Flag
			// followed by a versioned PUT of the Patient - so no verb boundary separates them
			Bundle transactionBundle = buildFlagFirstPutOnlyTransaction(true);

			// Execute + Verify
			try {
				mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);
				fail("The interceptor moved the Patient past v1 while pass 1 was still walking entries, but "
						+ "with no verb boundary there is no flush for the pass-1 check to see, so the re-check "
						+ "at the real write is what has to catch it");
			} catch (ResourceVersionConflictException e) {
				assertThat(e.getMessage()).contains(Msg.code(3021));
			}

			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("The whole transaction, including the interceptor's write, should have rolled back")
					.isEqualTo(1L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L);
			});
		}

		/**
		 * the companion to
		 * {@link CurrentVersionMatchesHistory#testTransactionUpdate_whenPreStorageUpdatedInterceptorUpdatesSameResource_currentVersionMatchesHistory()},
		 * driving the identical Bundle with a precondition added.
		 * <p>
		 * This is the tightest of the three routes. The interceptor hooks
		 * {@code STORAGE_PRESTORAGE_RESOURCE_UPDATED}, which is broadcast from inside the write path for
		 * the very resource being written - so its write lands after pass 1 has finished and after the
		 * Bundle's own write has begun. There is no earlier point at which any check could have seen it,
		 * which makes this the case that proves the re-check has to sit immediately before the write rather
		 * than once at the top of the write pass.
		 * </p>
		 */
		@Test
		void testTransactionUpdateWithIfMatch_whenPreStorageUpdatedInterceptorUpdatesSameResource_throwsVersionConflict() {
			// Setup - Patient v1
			createPatientVersionOne();

			ReentrantPatientUpdateInterceptor interceptor = new ReentrantPatientUpdateInterceptor(myPatientDao);
			registerInterceptor(interceptor);

			// build a transaction bundle carrying only a versioned PUT of the Patient, so the interceptor
			// fires from inside that entry's own write rather than from another entry
			Bundle transactionBundle = buildPatientOnlyTransaction(true);

			// Execute + Verify
			try {
				mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);
				fail("The interceptor writes from inside the Bundle's own write path, so only a check made "
						+ "immediately before the write can see that If-Match W/\"1\" is no longer satisfiable");
			} catch (ResourceVersionConflictException e) {
				assertThat(e.getMessage()).contains(Msg.code(3021));
			}

			assertThat(interceptor.getInvocationCount())
				.as("Interceptor never fired, so this test proves nothing")
				.isEqualTo(1);

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("The whole transaction, including the interceptor's write, should have rolled back")
					.isEqualTo(1L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L);
			});
		}

		/**
		 * <p>
		 * A PUT carrying If-Match plus a PATCH of the same Patient in one Bundle, with no interceptor
		 * involved. This is the only interceptor-free shape that gets two write entries for one resource
		 * past the duplicate-id guard at {@code BaseTransactionProcessor} ({@code Msg.code(535)}): that
		 * guard keys off the entry body's id, and a PATCH body is a Parameters with no id. Note that the
		 * PATCH entry must therefore carry no fullUrl - setting one to the Patient id puts the id back in
		 * play and the Bundle is rejected.
		 * </p>
		 * <p>
		 * The re-check must not turn this into a 409.
		 * </p>
		 */
		@Test
		void testTransactionPutPlusPatchOnSamePatient_withNoInterceptor_succeedsWithConsistentVersions() {
			// Setup - Patient v1
			createPatientVersionOne();

			// build a transaction bundle writing the same Patient twice with no interceptor involved -
			// a versioned PUT followed by a PATCH
			Bundle transactionBundle = buildPutPlusPatchTransaction();

			// Execute
			Bundle response = mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);

			// Verify
			assertThat(response.getEntry())
				.as("Both entries should have been accepted")
				.hasSize(2);
			assertThat(response.getEntry().get(0).getResponse().getStatus()).startsWith("200");
			assertThat(response.getEntry().get(1).getResponse().getStatus()).startsWith("200");

			assertPatientStorageVersionsAreConsistent();
		}
	}

	/**
	 * Pre-existing behaviour, kept as the contrast that makes {@link IfMatchRevalidatedAtTheWrite}
	 * legible rather than as coverage of it.
	 * <p>
	 * Where a Bundle's entries cross a verb boundary, pass 1 flushes the session at the crossing. That flush makes a
	 * re-entrant interceptor's write visible to the pass-1 precondition check of a later entry, which refuses the
	 * Bundle there and then with {@code HAPI-0989} - long before the write-time re-check would run. The route is
	 * otherwise identical to
	 * {@link IfMatchRevalidatedAtTheWrite#testTransactionUpdateWithIfMatch_whenSameVerbEntryInterceptorUpdatesSameResource_throwsVersionConflict()};
	 * only the incidental flush differs, and it is what lands the two cases on two different checks.
	 * </p>
	 * <p>
	 * This case sits on its own rather than beside the write-time re-check because it proves nothing
	 * about that re-check, and counting it as {@code HAPI-3021} coverage would overstate that group by
	 * one.
	 * </p>
	 */
	@Nested
	class IfMatchRejectedDuringPassOne {

		/**
		 * The interceptor has already moved the Patient past v1 by the time
		 * the PUT entry is walked, so pass 1's precondition check rejects the Bundle. Here the desired
		 * behaviour genuinely is an error; the point of the case is to pin that deliberately, and to pin
		 * that the rejection takes the interceptor's write down with it.
		 */
		@Test
		void testTransactionUpdateWithIfMatch_whenPostEntryInterceptorUpdatesSameResource_throwsVersionConflict() {
			// Setup - Patient v1
			createPatientVersionOne();

			ConfigurableReentrantFlagInterceptor interceptor = newNameAppendingFlagInterceptor();
			registerInterceptor(interceptor);


			// build a transaction bundle whose Flag entry is a POST, so the verb boundary before the
			// Patient's versioned PUT makes pass 1 flush and see the interceptor's write
			Bundle transactionBundle = buildPostFlagTransaction(true);

			// Execute + Verify
			try {
				mySystemDao.transaction(new SystemRequestDetails(), transactionBundle);
				fail("The interceptor moved the Patient past v1 before its PUT entry was walked, so pass 1's "
						+ "own precondition check must reject the Bundle");
			} catch (ResourceVersionConflictException e) {
				assertThat(e.getMessage()).contains(Msg.code(989));
			}

			runInTransaction(() -> {
				ResourceTable currentRow = findPatientRow();
				assertThat(currentRow.getVersion())
					.as("No HFJ_RES_VER row should survive from the interceptor's write")
					.isEqualTo(1L);
				assertThat(historyVersionsOf(currentRow)).containsExactly(1L);
			});
		}
	}

	// ---------------------------------------------------------------------------------------------
	// Shared helpers - assertions, fixtures and Bundle builders reached by all three groups.
	//
	// The no-interceptor control, and the assertPatientVersionsAreConsistent(Bundle) helper it uses,
	// are the originally committed reproduction and are left in their JUnit-Assertions style. The
	// reported case beside them was rewritten when the ticket owner ruled that an If-Match must be
	// enforced at the real write; every case added since is newer and uses AssertJ, per the repo
	// conventions.
	// ---------------------------------------------------------------------------------------------

	private void assertPatientVersionsAreConsistent(Bundle theTransactionResponse) {
		String responseLocation =
			theTransactionResponse.getEntry().get(0).getResponse().getLocation();
		long responseVersion = Long.parseLong(new IdType(responseLocation).getVersionIdPart());

		runInTransaction(() -> {
			ResourceTable currentRow = myResourceTableDao.findAll().stream()
				.filter(t -> "Patient".equals(t.getResourceType()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No Patient row in HFJ_RESOURCE"));

			List<Long> historyVersions = myResourceHistoryTableDao
				.findAllVersionsForResourceIdInOrder(currentRow.getResourceId().toFk())
				.stream()
				.map(ResourceHistoryTable::getVersion)
				.toList();

			long currentVersion = currentRow.getVersion();
			long highestHistoryVersion = historyVersions.stream().mapToLong(Long::longValue).max().orElse(-1L);

			String state = String.format(
				"HFJ_RESOURCE.RES_VER=%d, HFJ_RES_VER rows=%s, transaction response advertised v%d",
				currentVersion,
				historyVersions.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]")),
				responseVersion);
			ourLog.info("Final state: {}", state);

			assertTrue(currentVersion >= 2, "Expected the Patient to have been updated - " + state);

			assertEquals(highestHistoryVersion, currentVersion,
				"HFJ_RESOURCE.RES_VER does not point at the newest HFJ_RES_VER row - " + state);

			List<Long> expectedVersions = LongStream.rangeClosed(1, currentVersion).boxed().toList();
			assertEquals(expectedVersions, historyVersions,
				"Version history is not a gapless 1..N sequence - " + state);

			assertEquals(currentVersion, responseVersion,
				"Transaction response advertises a version the database does not hold - " + state);
		});
	}

	/**
	 * The response-free core of the storage invariant that
	 * {@link #assertPatientVersionsAreConsistent(Bundle)} asserts: the current-version pointer agrees
	 * with the history table, and the history rows form a gapless 1..N sequence.
	 * <p>
	 * Duplicated rather than extracted from that helper because Gate 1 of the test plan requires the
	 * committed reproduction's assertions to stay byte-identical.
	 * </p>
	 */
	private void assertPatientStorageVersionsAreConsistent() {
		runInTransaction(() -> {
			ResourceTable currentRow = findPatientRow();
			List<Long> historyVersions = historyVersionsOf(currentRow);

			long currentVersion = currentRow.getVersion();
			long highestHistoryVersion = historyVersions.stream().mapToLong(Long::longValue).max().orElse(-1L);

			String state = String.format(
				"HFJ_RESOURCE.RES_VER=%d, HFJ_RES_VER rows=%s", currentVersion, historyVersions);
			ourLog.info("Final state: {}", state);

			assertThat(currentVersion)
				.as("HFJ_RESOURCE.RES_VER does not point at the newest HFJ_RES_VER row - " + state)
				.isEqualTo(highestHistoryVersion);

			assertThat(historyVersions)
				.as("Version history is not a gapless 1..N sequence - " + state)
				.containsExactlyElementsOf(LongStream.rangeClosed(1, currentVersion).boxed().toList());
		});
	}

	private void assertResponseEntryVersionMatchesDatabase(Bundle theTransactionResponse, int theEntryIndex) {
		String responseLocation =
			theTransactionResponse.getEntry().get(theEntryIndex).getResponse().getLocation();
		long responseVersion = Long.parseLong(new IdType(responseLocation).getVersionIdPart());
		long currentVersion = runInTransaction(() -> {
			return findPatientRow().getVersion();
		});

		assertThat(responseVersion)
			.as("Transaction response advertises a version the database does not hold")
			.isEqualTo(currentVersion);
	}

	private void createPatientVersionOne() {
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());
	}

	/**
	 * A resource that does not reference the Patient, so that updating it can fire a hook without
	 * referential integrity standing in the way of deleting the Patient.
	 */
	private void createObservationVersionOne() {
		Observation observation = new Observation();
		observation.setId(OBSERVATION_ID);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.update(observation, new SystemRequestDetails());
	}

	/**
	 * Must be called from inside {@code runInTransaction}. Assumes a single Patient row, as every case
	 * in this class writes exactly one.
	 */
	private ResourceTable findPatientRow() {
		return myResourceTableDao.findAll().stream()
			.filter(t -> "Patient".equals(t.getResourceType()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("No Patient row in HFJ_RESOURCE"));
	}

	private List<ResourceHistoryTable> findPatientHistoryRows(ResourceTable theResourceTable) {
		return myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(
			theResourceTable.getResourceId().toFk());
	}

	private List<Long> historyVersionsOf(ResourceTable theResourceTable) {
		return findPatientHistoryRows(theResourceTable).stream()
			.map(ResourceHistoryTable::getVersion)
			.toList();
	}

	/**
	 * Mirrors the reported Bundle: a versioned PUT of the Patient alongside a conditional PUT of
	 * a Flag that references it.
	 */
	private Bundle buildTransaction() {
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		Flag flag = new Flag();
		flag.addIdentifier().setSystem(FLAG_IDENTIFIER_SYSTEM).setValue(FLAG_IDENTIFIER_VALUE);
		flag.setSubject(new Reference(PATIENT_ID));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		bundle.addEntry()
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(PATIENT_ID)
			.setIfMatch("W/\"1\"");

		bundle.addEntry()
			.setResource(flag)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Flag?identifier=" + FLAG_IDENTIFIER_SYSTEM + "|" + FLAG_IDENTIFIER_VALUE);

		return bundle;
	}

	/**
	 * The reported Bundle shape - Patient PUT first, then a conditional Flag PUT that resolves to a
	 * create - but without the {@code If-Match} that {@link #buildTransaction()} carries.
	 */
	private Bundle buildTransactionWithoutIfMatch() {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, false);
		addConditionalFlagPut(bundle, FLAG_IDENTIFIER_VALUE);
		return bundle;
	}

	private Bundle buildPatientOnlyTransaction() {
		return buildPatientOnlyTransaction(false);
	}

	private Bundle buildPatientOnlyTransaction(boolean theWithIfMatch) {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, theWithIfMatch);
		return bundle;
	}

	/**
	 * The Patient followed by an unrelated Observation. Both entries are updates of existing resources,
	 * so both are written during pass 2 in entry order - which puts the Observation's write, and the
	 * hook it broadcasts, after the Patient's own write.
	 */
	private Bundle buildPatientPlusObservationTransaction() {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, false);

		Observation observation = new Observation();
		observation.setId(OBSERVATION_ID);
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		bundle.addEntry()
			.setResource(observation)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(OBSERVATION_ID);

		return bundle;
	}

	private Bundle buildPostFlagTransaction(boolean theWithIfMatch) {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, theWithIfMatch);
		bundle.addEntry()
			.setResource(newFlag(FLAG_IDENTIFIER_VALUE))
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Flag");
		return bundle;
	}

	/**
	 * Flag first and both entries on the same verb, so that pass 1 never crosses a verb boundary and
	 * never flushes. See the Javadoc on the T9 case for why that matters.
	 */
	private Bundle buildFlagFirstPutOnlyTransaction() {
		return buildFlagFirstPutOnlyTransaction(false);
	}

	private Bundle buildFlagFirstPutOnlyTransaction(boolean theWithIfMatch) {
		Bundle bundle = newTransactionBundle();
		addConditionalFlagPut(bundle, FLAG_IDENTIFIER_VALUE);
		addPatientPut(bundle, theWithIfMatch);
		return bundle;
	}

	private Bundle buildTransactionWithTwoFlags() {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, false);
		addConditionalFlagPut(bundle, FLAG_IDENTIFIER_VALUE);
		addConditionalFlagPut(bundle, "f2");
		return bundle;
	}

	private Bundle buildPutPlusPatchTransaction() {
		Bundle bundle = newTransactionBundle();
		addPatientPut(bundle, true);
		bundle.addEntry()
			.setResource(buildActiveFalsePatch())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PATCH)
			.setUrl(PATIENT_ID);
		return bundle;
	}

	private Bundle buildNoOpPatientTransaction() {
		Bundle bundle = newTransactionBundle();

		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setActive(true);
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		bundle.addEntry()
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(PATIENT_ID);

		addConditionalFlagPut(bundle, FLAG_IDENTIFIER_VALUE);
		return bundle;
	}

	private static Bundle newTransactionBundle() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		return bundle;
	}

	private static void addPatientPut(Bundle theBundle, boolean theWithIfMatch) {
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		Bundle.BundleEntryRequestComponent request = theBundle.addEntry()
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(PATIENT_ID);
		if (theWithIfMatch) {
			request.setIfMatch("W/\"1\"");
		}
	}

	private static void addConditionalFlagPut(Bundle theBundle, String theIdentifierValue) {
		theBundle.addEntry()
			.setResource(newFlag(theIdentifierValue))
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Flag?identifier=" + FLAG_IDENTIFIER_SYSTEM + "|" + theIdentifierValue);
	}

	private static Flag newFlag(String theIdentifierValue) {
		Flag flag = new Flag();
		flag.addIdentifier().setSystem(FLAG_IDENTIFIER_SYSTEM).setValue(theIdentifierValue);
		flag.setSubject(new Reference(PATIENT_ID));
		return flag;
	}

	private static Parameters buildActiveFalsePatch() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter().setName("operation");
		operation.addPart().setName("type").setValue(new CodeType("replace"));
		operation.addPart().setName("path").setValue(new CodeType("Patient.active"));
		operation.addPart().setName("value").setValue(new BooleanType(false));
		return patch;
	}

	// ---------------------------------------------------------------------------------------------
	// Interceptors. Each performs the re-entrant write from a different pointcut; a case picks the
	// one that lands its second write where that case needs it.
	// ---------------------------------------------------------------------------------------------

	private ConfigurableReentrantFlagInterceptor newNameAppendingFlagInterceptor() {
		// Appending rather than replacing, so that a second invocation is guaranteed to be a real
		// change and not swallowed by no-change suppression.
		return new ConfigurableReentrantFlagInterceptor(
			myPatientDao, thePatient -> thePatient.addName().setFamily(REENTRANT_FAMILY_NAME));
	}

	@Interceptor
	private static class ReentrantFlagInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		ReentrantFlagInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void resourceCreated(IBaseResource theResource, RequestDetails theRequestDetails) {
			if (!(theResource instanceof Flag flag)) {
				return;
			}

			IdType targetId = new IdType(flag.getSubject().getReference()).toUnqualifiedVersionless();
			ourLog.info("Interceptor updating {} from within the Flag pre-storage hook", targetId);

			Patient patient = myPatientDao.read(targetId, theRequestDetails);
			patient.setDeceased(new DateTimeType(new Date()));
			myPatientDao.update(patient, theRequestDetails);

			myInvocationCount++;
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/**
	 * Variant of {@link ReentrantFlagInterceptor} whose mutation of the Patient is supplied by the
	 * test, so a case can decide whether the re-entrant write changes the resource at all (T8 needs
	 * a re-entrant write that leaves the Patient identical to the Bundle's own PUT body).
	 */
	@Interceptor
	private static class ConfigurableReentrantFlagInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private final Consumer<Patient> myPatientMutator;
		private int myInvocationCount = 0;

		ConfigurableReentrantFlagInterceptor(IFhirResourceDao<Patient> thePatientDao, Consumer<Patient> thePatientMutator) {
			myPatientDao = thePatientDao;
			myPatientMutator = thePatientMutator;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void resourceCreated(IBaseResource theResource, RequestDetails theRequestDetails) {
			if (!(theResource instanceof Flag flag)) {
				return;
			}

			IdType targetId = new IdType(flag.getSubject().getReference()).toUnqualifiedVersionless();
			Patient patient = myPatientDao.read(targetId, theRequestDetails);
			myPatientMutator.accept(patient);
			myPatientDao.update(patient, theRequestDetails);

			myInvocationCount++;
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/**
	 * Route (c) of the execution plan: the interceptor hooks the pointcut that is broadcast from
	 * inside {@code updateInternal} for the resource currently being written, so its write lands in
	 * the middle of pass 2's own write path. It fires only once, because the nested
	 * {@code dao.update()} broadcasts the same pointcut again.
	 */
	@Interceptor
	private static class ReentrantPatientUpdateInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		ReentrantPatientUpdateInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
		public void resourcePreUpdate(RequestDetails theRequestDetails, IBaseResource theOldResource, IBaseResource theNewResource) {
			if (!(theNewResource instanceof Patient) || myInvocationCount > 0) {
				return;
			}
			myInvocationCount++;

			Patient patient = myPatientDao.read(new IdType(PATIENT_ID), theRequestDetails);
			patient.setDeceased(new DateTimeType(new Date()));
			myPatientDao.update(patient, theRequestDetails);
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/**
	 * Deletes the Patient re-entrantly from inside pass 2, triggered by the write of an unrelated
	 * resource rather than by the Patient's own. Hooking the Patient's own update broadcast would put
	 * the delete <em>before</em> the Bundle's write and produce delete-then-update, which is a different
	 * scenario; keying off the Observation instead means the Bundle has already written the Patient by
	 * the time the delete runs.
	 */
	@Interceptor
	private static class ReentrantDeleteInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		ReentrantDeleteInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
		public void resourcePreUpdate(RequestDetails theRequestDetails, IBaseResource theOldResource, IBaseResource theNewResource) {
			if (!(theNewResource instanceof Observation) || myInvocationCount > 0) {
				return;
			}
			myInvocationCount++;

			myPatientDao.delete(new IdType(PATIENT_ID), theRequestDetails);
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/**
	 * The supported "safe" pattern this bug must not break: a storage interceptor that writes from a
	 * PRECOMMIT pointcut, i.e. after every Bundle write has already been flushed. MdmStorageInterceptor
	 * writes from STORAGE_PRECOMMIT_RESOURCE_DELETED for the same reason. CREATED is hooked here rather
	 * than UPDATED because UPDATED is broadcast from inside updateInternal for the Patient itself and
	 * a hook writing that Patient would recurse.
	 */
	@Interceptor
	private static class PrecommitFlagInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		PrecommitFlagInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void resourceCreated(IBaseResource theResource, RequestDetails theRequestDetails) {
			if (!(theResource instanceof Flag flag)) {
				return;
			}

			IdType targetId = new IdType(flag.getSubject().getReference()).toUnqualifiedVersionless();
			Patient patient = myPatientDao.read(targetId, theRequestDetails);
			patient.addName().setFamily(REENTRANT_FAMILY_NAME);
			myPatientDao.update(patient, theRequestDetails);

			myInvocationCount++;
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}
}
