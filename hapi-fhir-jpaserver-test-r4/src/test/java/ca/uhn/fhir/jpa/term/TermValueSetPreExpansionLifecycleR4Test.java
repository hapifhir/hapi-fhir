/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.Step1InitiateJob;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Covers {@link TermValueSet} pre-expansion lifecycle transitions — how {@code expansionStatus},
 * {@code expansionError}, and {@code expansionTimestamp} change across failure/retry, success/breakage,
 * CodeSystem-content invalidation, and ValueSet activate/deactivate, and when a pre-expansion job is
 * allowed to start at all - including the content a job produces when it runs against terminology
 * that is not in the state it needs. {@link ValueSetExpansionR4Test} covers {@code $expand}
 * content/query behavior generally.
 */
// Created by claude-sonnet-5
class TermValueSetPreExpansionLifecycleR4Test extends BaseTermR4Test {

	@Test
	void preExpansion_onExpansionFailure_persistsExpansionError() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given an active ValueSet that references a CodeSystem which cannot be resolved
		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/vs-failed-expansion");
		vs.setUrl("http://vs-failed-expansion");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://unknown-system");
		myValueSetDao.update(vs, newSrd());

		// When pre-expansion runs
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then the failure reason is persisted alongside the FAILED_TO_EXPAND status, as a short
		// message rather than a stack trace dump
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-failed-expansion")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError())
				.contains("Unable to expand ValueSet because CodeSystem could not be found: http://unknown-system")
				.doesNotContain("\n")
				.doesNotContain("\tat ");
		});
	}

	@Test
	void preExpansion_onFailureThenFixedRetry_updatesToExpansionSuccess() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a ValueSet whose include references a CodeSystem that doesn't exist yet
		ValueSet vs = new ValueSet();
		vs.setId("vs-fail-then-fix");
		vs.setUrl("http://vs-fail-then-fix");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://cs-fail-then-fix");
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then pre-expansion fails: status FAILED_TO_EXPAND, error set, timestamp null
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-fail-then-fix")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError())
				.contains("CodeSystem could not be found: http://cs-fail-then-fix");
			assertThat(termValueSet.getExpansionTimestamp()).isNull();
		});

		// When the missing CodeSystem is created and the ValueSet is re-saved to requeue pre-expansion
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://cs-fail-then-fix");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A").setDisplay("Code A");
		myCodeSystemDao.create(cs, mySrd);
		vs.setName("vs-fail-then-fix-retry");
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then the retry succeeds: status EXPANDED, error cleared, timestamp freshly set
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-fail-then-fix")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError()).isNull();
			assertThat(termValueSet.getExpansionTimestamp()).isNotNull();
		});
	}

	@Test
	void preExpansion_onSuccessThenBrokenInclude_updatesExpansionForFailure() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a ValueSet that pre-expands successfully
		CodeSystem cs = new CodeSystem();
		cs.setId("cs-success-then-broken");
		cs.setUrl("http://cs-success-then-broken");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A").setDisplay("Code A");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vs-success-then-broken");
		vs.setUrl("http://vs-success-then-broken");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://cs-success-then-broken");
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then it's EXPANDED with no error and a fresh timestamp
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-success-then-broken")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError()).isNull();
			assertThat(termValueSet.getExpansionTimestamp()).isNotNull();
		});

		// When the include is changed to point at a CodeSystem that doesn't exist, forcing a failing retry
		vs.getCompose().getIncludeFirstRep().setSystem("http://unknown-system-success-then-broken");
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then status flips to FAILED_TO_EXPAND, an error is recorded, and the earlier timestamp is not left stale
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-success-then-broken")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError())
				.contains("CodeSystem could not be found: http://unknown-system-success-then-broken");
			assertThat(termValueSet.getExpansionTimestamp()).isNull();
		});
	}

	@Test
	void preExpansion_onCodeSystemContentUpdate_updatesExpansionToForExpanded() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a ValueSet pre-expanded against a CodeSystem — this is a distinct trigger from the two
		// tests above: the CodeSystem itself still resolves, its *content* changed, which auto-invalidates
		// (not fails) any dependent EXPANDED ValueSets.
		CodeSystem cs = new CodeSystem();
		cs.setId("cs-invalidation-clears-timestamp");
		cs.setUrl("http://cs-invalidation-clears-timestamp");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A").setDisplay("Code A");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vs-invalidation-clears-timestamp");
		vs.setUrl("http://vs-invalidation-clears-timestamp");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://cs-invalidation-clears-timestamp");
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		runInTransaction(() -> assertThat(myTermValueSetDao
			.findTermValueSetByUrlAndNullVersion("http://vs-invalidation-clears-timestamp")
			.orElseThrow(IllegalStateException::new)
			.getExpansionTimestamp()).isNotNull());

		// When the underlying CodeSystem's content is updated, automatically invalidating the pre-calculated expansion
		cs.getConcept().clear();
		cs.addConcept().setCode("B").setDisplay("Code B");
		myCodeSystemDao.update(cs, mySrd);

		// Then the ValueSet's expansion is marked NOT_EXPANDED (not failed) and its timestamp is cleared
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-invalidation-clears-timestamp")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionTimestamp()).isNull();
		});
	}

	@Test
	void preExpansion_onStatusDraftThenActive_updatesExpansion() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a ValueSet that is in DRAFT status

		ValueSet valueSet = new ValueSet();
		valueSet.setId("vs");
		valueSet.setUrl("http://foo/vs");
		valueSet.setStatus(Enumerations.PublicationStatus.DRAFT);
		valueSet.getCompose().addInclude().setSystem(Enumerations.AdministrativeGender.MALE.getSystem());
		myValueSetDao.update(valueSet, newSrd());
		myBatch2JobHelper.awaitNoJobsRunning();

		// Validate we're not expanding this value set right now
		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findTermValueSetByUrlAndNullVersion("http://foo/vs");
			assertTrue(optionalValueSetByUrl.isPresent());
			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_ACTIVE, termValueSet.getExpansionStatus());
		});

		// Given that we switch it to active
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myValueSetDao.update(valueSet, newSrd());
		myBatch2JobHelper.awaitNoJobsRunning();

		// Validate that it's now expanded
		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findTermValueSetByUrlAndNullVersion("http://foo/vs");
			assertTrue(optionalValueSetByUrl.isPresent());
			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
		});
	}

	// Generated by claude-sonnet-5
	@Test
	void preExpansion_onStatusActiveThenRetiredThenActive_updatesExpansion() {
		myStorageSettings.setPreExpandValueSets(true);

		// Given a ValueSet that pre-expands successfully
		ValueSet vs = new ValueSet();
		vs.setId("vs-deactivate-then-reactivate");
		vs.setUrl("http://vs-deactivate-then-reactivate");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(Enumerations.AdministrativeGender.MALE.getSystem());
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-deactivate-then-reactivate")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionTimestamp()).isNotNull();
			assertThat(termValueSet.getExpansionError()).isNull();
		});

		// When the ValueSet is retired
		vs.setStatus(Enumerations.PublicationStatus.RETIRED);
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then its pre-calculated expansion is dropped: status NOT_ACTIVE, no error, no timestamp
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-deactivate-then-reactivate")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_ACTIVE, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionTimestamp()).isNull();
			assertThat(termValueSet.getExpansionError()).isNull();
		});

		// When the ValueSet is reactivated
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		myValueSetDao.update(vs, mySrd);
		myBatch2JobHelper.awaitNoJobsRunning();

		// Then it's pre-expanded again with a fresh timestamp
		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao
				.findTermValueSetByUrlAndNullVersion("http://vs-deactivate-then-reactivate")
				.orElseThrow(IllegalStateException::new);
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
			assertThat(termValueSet.getExpansionError()).isNull();
			assertThat(termValueSet.getExpansionTimestamp()).isNotNull();
		});
	}

	/**
	 * A CodeSystem large enough for its concept storage to be deferred, then a ValueSet that includes
	 * it: the pre-expansion job has to process the deferred concepts itself before it stages the
	 * expansion, otherwise the expansion is written against a partially stored CodeSystem. Covers the
	 * resource storage path rather than the terminology import job.
	 * <p>
	 * Scheduling is disabled in these tests, so the pre-expansion job is the only thing that can drain
	 * the deferred queue.
	 *
	 * @see <a href="https://github.com/hapifhir/hapi-fhir/issues/8321">Issue #8321</a>
	 */
	@Test
	void preExpansion_onCodeSystemConceptStorageDeferred_expandsAllConcepts() {
		myStorageSettings.setPreExpandValueSets(true);
		int conceptCount = myStorageSettings.getDeferIndexingForCodesystemsOfSize() + 50;

		myCodeSystemDao.create(newDeferredCodeSystem(conceptCount), newSrd());

		assertFalse(myTerminologyDeferredStorageSvc.isStorageQueueEmpty(false),
			"Test setup expects the CodeSystem to be big enough for its storage to be deferred");

		// storing the ValueSet starts the pre-expansion job on commit
		myValueSetDao.create(newValueSetIncludingWholeCodeSystem(), newSrd());
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(JOB_ID_PRE_EXPAND_VALUESET);

		// must stay after the await - draining the queue first would leave this passing whether or not
		// the job waits for the deferred concepts
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		assertThat(runInTransaction(() -> myTermConceptDao.count())).isEqualTo(conceptCount);

		TermValueSet termValueSet = runInTransaction(() -> myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URL).orElseThrow());
		assertThat(termValueSet.getTotalConcepts()).isEqualTo(conceptCount);
	}

	/**
	 * As above, but with deferred processing paused, so the pre-expansion job cannot drain the queue
	 * itself and has to fall through to {@link RetryChunkLaterException}. The work chunk must park in
	 * {@link WorkChunkStatusEnum#POLL_WAITING} with nothing expanded, and the expansion must only be
	 * written once processing resumes.
	 *
	 * @see <a href="https://github.com/hapifhir/hapi-fhir/issues/8321">Issue #8321</a>
	 */
	@Test
	void preExpansion_onDeferredStorageProcessingPaused_waitsForTheQueueThenExpandsAllConcepts() {
		myStorageSettings.setPreExpandValueSets(true);
		int conceptCount = myStorageSettings.getDeferIndexingForCodesystemsOfSize() + 50;

		// keep the poll short enough that this test doesn't run long, but long enough that the chunk is
		// reliably observed parked between the maintenance passes below
		Step1InitiateJob.setRetryDelay(Duration.of(1, ChronoUnit.SECONDS));
		try {
			myCodeSystemDao.create(newDeferredCodeSystem(conceptCount), newSrd());

			assertFalse(myTerminologyDeferredStorageSvc.isStorageQueueEmpty(false),
				"Test setup expects the CodeSystem to be big enough for its storage to be deferred");

			// pausing makes the job's own saveDeferred() a no-op, so it has no way to drain the queue
			myTerminologyDeferredStorageSvc.setProcessDeferred(false);

			// storing the ValueSet starts the pre-expansion job on commit
			myValueSetDao.create(newValueSetIncludingWholeCodeSystem(), newSrd());

			awaitPollWaitingPreExpansionWorkChunk();

			TermValueSet parkedValueSet = runInTransaction(() -> myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URL).orElseThrow());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, parkedValueSet.getExpansionStatus());
			assertThat(parkedValueSet.getTotalConcepts()).isZero();

			myTerminologyDeferredStorageSvc.setProcessDeferred(true);
			myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(JOB_ID_PRE_EXPAND_VALUESET);

			assertThat(runInTransaction(() -> myTermConceptDao.count())).isEqualTo(conceptCount);

			TermValueSet termValueSet = runInTransaction(() -> myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URL).orElseThrow());
			assertThat(termValueSet.getTotalConcepts()).isEqualTo(conceptCount);
		} finally {
			Step1InitiateJob.setRetryDelay(null);
			myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		}
	}

	/**
	 * Scheduling is disabled in these tests, so a job only advances when a maintenance pass is forced.
	 */
	private void awaitPollWaitingPreExpansionWorkChunk() {
		try {
			await().atMost(Batch2JobHelper.DEFAULT_WAIT_DURATION).until(() -> {
				myBatch2JobHelper.forceRunActiveJobMaintenancePass();
				return preExpansionWorkChunkStatuses().contains(WorkChunkStatusEnum.POLL_WAITING);
			});
		} catch (ConditionTimeoutException e) {
			fail("No pre-expansion work chunk reached POLL_WAITING. Chunk statuses: " + preExpansionWorkChunkStatuses());
		}
	}

	private List<WorkChunkStatusEnum> preExpansionWorkChunkStatuses() {
		Set<String> instanceIds = myBatch2JobHelper.findJobsByDefinition(JOB_ID_PRE_EXPAND_VALUESET).stream()
			.map(JobInstance::getInstanceId)
			.collect(Collectors.toSet());

		return runInTransaction(() -> myWorkChunkRepository.findAll().stream()
			.filter(chunk -> instanceIds.contains(chunk.getInstanceId()))
			.map(Batch2WorkChunkEntity::getStatus)
			.toList());
	}

	/**
	 * A hierarchy bigger than the deferred storage threshold. Top-level concepts are always persisted
	 * as the resource is stored, so only a hierarchy leaves anything on the deferred queue.
	 */
	private CodeSystem newDeferredCodeSystem(int theConceptCount) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(CS_URL);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		CodeSystem.ConceptDefinitionComponent root = cs.addConcept().setCode("root").setDisplay("Root");
		for (int i = 1; i < theConceptCount; i++) {
			root.addConcept().setCode("code-" + i).setDisplay("Code " + i);
		}
		return cs;
	}

	private ValueSet newValueSetIncludingWholeCodeSystem() {
		ValueSet vs = new ValueSet();
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(CS_URL);
		return vs;
	}

	/**
	 * What a pre-expansion stores, and what {@code validateCode} then answers from it, when the
	 * CodeSystem a compose section names cannot be resolved in the terminology tables. Sibling of
	 * {@link TermValueSetPreExpansionLifecycleR4Test#preExpansion_onCodeSystemConceptStorageDeferred_expandsAllConcepts}:
	 * there the job ran before the concepts were stored, here it runs when the version cannot be
	 * resolved at all.
	 * <p>
	 * Most of these tests pin behaviour the FHIR specification forbids. R5 {@code ValueSet/$expand},
	 * Out Parameters: <i>"When a server cannot correctly expand a value set because it does not fully
	 * understand the code systems (e.g. it has the wrong version, or incomplete definitions) then it
	 * SHALL return an error."</i> Because the sections here enumerate their concepts, the expansion
	 * instead succeeds, stores the codes, and serves them to every later {@code validateCode} - see
	 * <a href="https://github.com/hapifhir/hapi-fhir/issues/8415">#8415</a>. They assert the outcome the
	 * specification requires, so the ones covering an unresolvable version are red until that issue is
	 * fixed, and each docstring says which. The fix itself needs a version-aware
	 * {@code isCodeSystemSupported}, which is
	 * <a href="https://github.com/hapifhir/hapi-fhir/issues/8402">#8402</a>.
	 * <p>
	 * Each defect test is paired with a control differing in one step, so that a failure states
	 * something about the unresolved CodeSystem rather than about enumerated includes in general.
	 */
	@Nested
	// Created by Claude Opus 5
	class WhenTheCodeSystemVersionCannotBeResolved {

		/**
		 * When the include names a CodeSystem version that is not installed, the terminology tables cannot
		 * resolve it and {@code TermReadSvcImpl.expandValueSetHandleIncludeOrExclude} falls through to the
		 * in-memory expander, which copies every enumerated code across unchecked. The job reports success,
		 * the ValueSet is marked {@code EXPANDED}, and the stored rows then answer every later
		 * {@code validateCode} - so a code no installed CodeSystem version contains validates, attributed
		 * to a version that was never read. On this path nothing self-heals: re-expanding produces the same
		 * rows, because 2.0.0 is still absent.
		 * <p>
		 * R5 {@code ValueSet/$expand}, Out Parameters, requires an error instead, and that is what this
		 * asserts. Red until #8415 is fixed.
		 */
		@Test
		void preExpansion_includeNamesUninstalledCodeSystemVersion_failsAndStoresNothingToValidateAgainst() {
			myStorageSettings.setPreExpandValueSets(true);

			// Given a CodeSystem installed at version 1.0.0, holding "A" and not "NOT-STORED"
			givenCodeSystemVersionHoldingConceptA();

			// And an active ValueSet enumerating both codes, whose include names the uninstalled version 2.0.0
			givenValueSetEnumeratingCodes("2.0.0", "A", "NOT-STORED");
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the expansion fails and stores nothing, because no installed version backs the codes
			runInTransaction(() -> {
				TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndNullVersion(VS_URL)
					.orElseThrow(IllegalStateException::new);
				assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
				assertThat(preExpandedCodes()).isEmpty();
			});

			// And there is nothing to validate the code against
			IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(
				new CodeType(VS_URL), null, new CodeType("NOT-STORED"), new CodeType(CS_URL), null, null, null, mySrd);
			assertFalse(outcome.isOk());
		}

		/**
		 * Control for
		 * {@link #preExpansion_includeNamesUninstalledCodeSystemVersion_storesAndValidatesCodesNoCodeSystemContains}:
		 * the same data with an include that resolves. Keeps that test a statement about the unresolved
		 * version rather than about enumerated includes in general.
		 */
		@Test
		void preExpansion_includeResolvesToInstalledVersion_storesAndValidatesOnlyCodesTheCodeSystemContains() {
			myStorageSettings.setPreExpandValueSets(true);

			// Given the same CodeSystem and enumerated ValueSet, with an include that names no version
			givenCodeSystemVersionHoldingConceptA();
			givenValueSetEnumeratingCodes(null, "A", "NOT-STORED");
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the code the CodeSystem does not have is filtered out of the pre-expansion
			runInTransaction(() -> {
				TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndNullVersion(VS_URL)
					.orElseThrow(IllegalStateException::new);
				assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
				assertThat(preExpandedCodes()).containsExactly("A");
			});

			// And it is not accepted from the stored expansion either
			IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(
				new CodeType(VS_URL), null, new CodeType("NOT-STORED"), new CodeType(CS_URL), null, null, null, mySrd);
			assertFalse(outcome.isOk());
		}

		/**
		 * The same call as
		 * {@link #preExpansion_includeNamesUninstalledCodeSystemVersion_storesAndValidatesCodesNoCodeSystemContains}
		 * with pre-expansion turned off, so validation resolves the code live instead of reading stored
		 * rows. It rejects the code, which is what makes the pre-expanded answer wrong rather than merely
		 * lenient: the same server contradicts itself depending on whether a pre-expansion happens to
		 * exist.
		 */
		@Test
		void validateCode_preExpansionDisabled_rejectsCodeTheCodeSystemDoesNotContain() {
			myStorageSettings.setPreExpandValueSets(false);

			// Given the same CodeSystem and ValueSet, with no pre-expansion to answer from
			givenCodeSystemVersionHoldingConceptA();
			givenValueSetEnumeratingCodes("2.0.0", "A", "NOT-STORED");
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the enumerated code the CodeSystem does not contain is rejected
			IValidationSupport.CodeValidationResult unknownCode = myValueSetDao.validateCode(
				new CodeType(VS_URL), null, new CodeType("NOT-STORED"), new CodeType(CS_URL), null, null, null, mySrd);
			assertFalse(unknownCode.isOk());
			assertThat(unknownCode.getMessage()).contains("Unknown code");
		}

		/**
		 * The enumeration is what turns a loud failure into a silent one. With no concepts listed, an
		 * include naming a CodeSystem the server does not have fails the job - that is
		 * {@link TermValueSetPreExpansionLifecycleR4Test#preExpansion_onExpansionFailure_persistsExpansionError}.
		 * Listing concepts sends the same include down the in-memory fallback, which copies them across
		 * unchecked.
		 * <p>
		 * {@code validateCode} still accepts the code, and deliberately so: nothing here can look it up,
		 * so validation takes the enumeration at face value, which is the allowance HAPI documented for a
		 * code system that cannot be supplied. That allowance is out of scope. What this pins is that the
		 * stored rows carry no code system version, because none was read.
		 * </p>
		 */
		@Test
		void preExpansion_unknownCodeSystemWithEnumeratedConcepts_storesCodesWithoutClaimingAVersion() {
			myStorageSettings.setPreExpandValueSets(true);

			// Given an active ValueSet enumerating concepts from a CodeSystem the server does not have
			String unknownSystem = "http://unknown-system-enumerated";
			givenValueSetIncluding(unknownSystem, null, "A", "NOT-STORED");
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the enumerated codes are stored, attributed to no code system version
			runInTransaction(() -> {
				TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndNullVersion(VS_URL)
					.orElseThrow(IllegalStateException::new);
				assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());
				assertThat(preExpandedCodes()).containsExactly("A", "NOT-STORED");
				assertThat(preExpandedSystemVersions()).containsOnlyNulls();
			});

			// And the enumerated code is still accepted, which is the documented allowance rather than
			// anything this change touches
			IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(
				new CodeType(VS_URL), null, new CodeType("NOT-STORED"), new CodeType(unknownSystem), null, null, null, mySrd);
			assertTrue(outcome.isOk());
		}

		/**
		 * A filter cannot be applied to a CodeSystem that does not resolve, so there is nothing for the
		 * fallback to copy and the job fails. This is the behaviour the enumerated case should match.
		 */
		@Test
		void preExpansion_includeWithFilterNamesUninstalledCodeSystemVersion_failsTheExpansion() {
			myStorageSettings.setPreExpandValueSets(true);

			// Given a CodeSystem installed at 1.0.0, and a ValueSet filtering on the uninstalled 2.0.0
			givenCodeSystemVersionHoldingConceptA();
			ValueSet vs = new ValueSet();
			vs.setId("ValueSet/vs-filtered-include");
			vs.setUrl(VS_URL);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			vs.getCompose()
				.addInclude()
				.setSystem(CS_URL)
				.setVersion("2.0.0")
				.addFilter()
				.setProperty("concept")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("A");
			myValueSetDao.update(vs, mySrd);
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the expansion fails and stores nothing
			runInTransaction(() -> {
				TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndNullVersion(VS_URL)
					.orElseThrow(IllegalStateException::new);
				assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
				assertThat(preExpandedCodes()).isEmpty();
			});
		}

		/**
		 * Not confined to not-present CodeSystems: the same thing happens for one whose content the
		 * server holds in full, so this is not the documented compromise for a CodeSystem that cannot
		 * be supplied. Asserts the same outcome as
		 * {@link #preExpansion_includeNamesUninstalledCodeSystemVersion_failsAndStoresNothingToValidateAgainst},
		 * and is red for the same reason.
		 */
		@Test
		void preExpansion_completeCodeSystemAndIncludeNamesUninstalledVersion_failsAndStoresNothingToValidateAgainst() {
			myStorageSettings.setPreExpandValueSets(true);

			// Given the CodeSystem stored as COMPLETE rather than NOTPRESENT
			givenCodeSystemVersionHoldingConceptA(CodeSystem.CodeSystemContentMode.COMPLETE);
			givenValueSetEnumeratingCodes("2.0.0", "A", "NOT-STORED");
			myBatch2JobHelper.awaitNoJobsRunning();

			// Then the expansion fails here too, so this is not the allowance made for a CodeSystem
			// whose content cannot be supplied
			runInTransaction(() -> {
				TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndNullVersion(VS_URL)
					.orElseThrow(IllegalStateException::new);
				assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, termValueSet.getExpansionStatus());
				assertThat(preExpandedCodes()).isEmpty();
			});

			// And there is nothing to validate the code against
			IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(
				new CodeType(VS_URL), null, new CodeType("NOT-STORED"), new CodeType(CS_URL), null, null, null, mySrd);
			assertFalse(outcome.isOk());
		}

		private void givenCodeSystemVersionHoldingConceptA() {
			givenCodeSystemVersionHoldingConceptA(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		}

		/**
		 * Installs {@code CS_URL} version 1.0.0 in the terminology tables holding the single concept "A",
		 * so that 2.0.0 is a version this server does not have. not-present is how LOINC and SNOMED are
		 * stored; the tests pass COMPLETE to show the behaviour does not depend on it.
		 */
		private void givenCodeSystemVersionHoldingConceptA(CodeSystem.CodeSystemContentMode theContent) {
			CodeSystem cs = new CodeSystem();
			cs.setUrl(CS_URL);
			cs.setVersion("1.0.0");
			cs.setContent(theContent);
			cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			IIdType csId = myCodeSystemDao.create(cs, mySrd).getId().toUnqualified();

			runInTransaction(() -> {
				ResourceTable table = myResourceTableDao
					.findById(csId.getIdPartAsLong())
					.orElseThrow(IllegalArgumentException::new);
				TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
				codeSystemVersion.setResource(table);
				codeSystemVersion.getConcepts().add(new TermConcept(codeSystemVersion, "A"));
				myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(CS_URL, "CS NAME", "1.0.0", codeSystemVersion, table);
			});
		}

		private void givenValueSetEnumeratingCodes(String theIncludeVersion, String... theCodes) {
			givenValueSetIncluding(CS_URL, theIncludeVersion, theCodes);
		}

		/**
		 * An active ValueSet whose single include enumerates the given codes, naming a CodeSystem
		 * version only when one is given.
		 */
		private void givenValueSetIncluding(String theSystem, String theIncludeVersion, String... theCodes) {
			ValueSet vs = new ValueSet();
			vs.setId("ValueSet/vs-enumerated-concepts");
			vs.setUrl(VS_URL);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude().setSystem(theSystem);
			if (theIncludeVersion != null) {
				include.setVersion(theIncludeVersion);
			}
			for (String code : theCodes) {
				include.addConcept().setCode(code);
			}
			myValueSetDao.update(vs, mySrd);
		}

		/**
		 * The CodeSystem versions the pre-expansion of {@code VS_URL} records. Reads a lazy association,
		 * so call inside a transaction.
		 */
		private List<String> preExpandedSystemVersions() {
			return myTermValueSetConceptDao.findAll().stream()
				.filter(concept -> VS_URL.equals(concept.getValueSet().getUrl()))
				.map(TermValueSetConcept::getSystemVersion)
				.distinct()
				.toList();
		}

		/**
		 * The codes the pre-expansion of {@code VS_URL} holds. Reads a lazy association, so call inside a
		 * transaction.
		 */
		private List<String> preExpandedCodes() {
			return myTermValueSetConceptDao.findAll().stream()
				.filter(concept -> VS_URL.equals(concept.getValueSet().getUrl()))
				.map(TermValueSetConcept::getCode)
				.sorted()
				.toList();
		}
	}
}
