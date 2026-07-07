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

import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers {@link TermValueSet} pre-expansion lifecycle transitions — how {@code expansionStatus},
 * {@code expansionError}, and {@code expansionTimestamp} change across failure/retry, success/breakage,
 * CodeSystem-content invalidation, and ValueSet activate/deactivate. {@link ValueSetExpansionR4Test} covers
 * {@code $expand} content/query behavior instead.
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

}
