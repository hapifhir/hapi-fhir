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
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.Step1InitiateJob;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import org.awaitility.core.ConditionTimeoutException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
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
 * allowed to start at all. {@link ValueSetExpansionR4Test} covers {@code $expand} content/query
 * behavior instead.
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
	 * Covers https://github.com/hapifhir/hapi-fhir/issues/8321 on the resource storage path, which is
	 * what a package or IG upload uses. A CodeSystem big enough for its concept storage to be
	 * deferred, followed by a ValueSet that includes it, must not pre-expand until those deferred
	 * concepts have been processed, otherwise the expansion is written against a partially stored
	 * CodeSystem.
	 * <p>
	 * Scheduling is disabled in these tests, so nothing processes the deferred concepts except the
	 * pre-expansion job itself. Processing them before the await would empty the queue first and the
	 * test would pass with or without the readiness check.
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

		// the job processes the deferred concepts itself, so this should find nothing left
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
	 * @see <a href="https://github.com/hapifhir/hapi-fhir/issues/8321">GH-8321</a>
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

}
