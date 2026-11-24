package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMatchClaimSvcJpaImpl;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyDataPatterns.patient;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency cases T1-T6, which don't fit the sequence-by-data matrix in {@link MdmConcurrencyMatrixIT}. They share
 * the matrix's rules and gating configuration.
 */
@ContextConfiguration(classes = MdmConcurrencyMatrixTestConfig.class)
@TestPropertySource(
		properties = {
			"module.mdm.config.script.file=classpath:mdm/mdm-rules-concurrency-matrix.json",
			"mdm.prevent_eid_updates=false"
		})
// Created by claude-opus-5-5
class MdmConcurrencyTargetedIT extends BaseMdmR4Test {

	@Autowired
	private ScriptedInterleavingTestHarness myHarness;

	@Autowired
	private IMdmMatchClaimSvc myGatingClaimSvc;

	@Autowired
	private MdmMatchClaimSvcJpaImpl myMdmMatchClaimSvc;

	@Autowired
	private IMdmMatchClaimJpaRepository myMdmMatchClaimJpaRepository;

	@AfterEach
	void afterCleanUp() {
		((GatingMdmMatchClaimSvc) myGatingClaimSvc).setBeforeClaimHook(null);
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
		runInTransaction(() -> myMdmMatchClaimJpaRepository.deleteAll());
	}

	/**
	 * T1: a caller that joins an existing transaction (as the REST link updater does) claims a matching source
	 * mid-transaction, while a consumer that restarted holds that source's claim and waits for the caller's. The
	 * database breaks the cycle; either way there must be exactly one golden resource, and the caller may only fail
	 * with a version conflict.
	 */
	@Test
	void joinedCallerRacingAConsumer_neverDuplicates() {
		// Created first, so the consumer's source sorts first and the consumer claims it before waiting
		Patient consumerSource = createPatient(patient("Wren", "Cole", "1980-01-01", null, null));
		Patient joinedSource = createPatient(patient("Wren", "Cole", "1980-01-01", null, null));

		ScriptedInterleavingTestHarness.Run run = myHarness.run(
				ThreadSequence.T1_JOINED,
				List.of(
						() -> runInTransaction(() -> updateLinks(joinedSource)),
						() -> updateLinks(consumerSource)),
				List.of("joined", "consumer"));

		Throwable joinedFailure = run.workers().get(0).getFailure();
		assertThat(run.workers().get(1).getFailure()).as("%s", run).isNull();
		if (joinedFailure != null) {
			assertThat(joinedFailure).as("%s", run).isInstanceOf(ResourceVersionConflictException.class);
		}
		assertThat(getAllGoldenPatients()).as("%s", run).hasSize(1);
	}

	/**
	 * T2: when HapiTransactionService itself retries an MDM attempt (it does so up to three times for a tag
	 * definition conflict), the retried attempt must not repeat the context state of the failed one.
	 */
	@Test
	void innerTransactionRetry_doesNotDuplicateContextState() {
		Patient source = createPatient(patient("Xena", "Dale", "1980-01-01", null, null));
		AtomicBoolean failOnce = new AtomicBoolean(true);
		myInterceptorRegistry.registerAnonymousInterceptor(
				Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
					if (MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class)) && failOnce.getAndSet(false)) {
						throw new ResourceVersionConflictException("Simulated concurrent insert into HFJ_TAG_DEF");
					}
				});
		MdmTransactionContext context = createContextForCreate("Patient");
		context.setTransactionLogMessages(TransactionLogMessages.createNew());

		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(source, context);

		assertThat(failOnce).isFalse();
		assertThat(getAllGoldenPatients()).hasSize(1);
		// The failed execution's HAPI EID must not leak: the source carries only the EID its golden resource has
		Patient golden = (Patient) getOnlyGoldenPatient();
		List<CanonicalEID> goldenHapiEids = myEIDHelper.getHapiEid(golden);
		assertThat(goldenHapiEids).hasSize(1);
		assertThat(myEIDHelper.getHapiEid(source)).containsExactlyElementsOf(goldenHapiEids);
		assertThat(context.getMdmLinks()).hasSize(1);
		assertThat(context.getTransactionLogMessages().getValues())
				.filteredOn(message -> message.contains("creating a new Patient Golden Resource"))
				.hasSize(1);

	}

	/**
	 * T3: a committed claim is purged between a thread's pre-read and its claim insert, so the insert doesn't
	 * conflict. Because claims are taken before the search, the thread's search still runs after the other thread
	 * committed and sees its golden resource. (Under REPEATABLE READ the search would use the snapshot taken by the
	 * pre-read and miss it, which is why the retention must exceed the longest MDM transaction.)
	 */
	@Test
	void claimPurgedWhileAThreadIsBetweenPreReadAndClaim_stillJoinsTheGoldenResource() throws Exception {
		Patient first = createPatient(patient("Yara", "Ellis", "1980-01-01", "npi-t3", null));
		Patient second = createPatient(patient("Zoe", "Frost", "1975-05-05", "npi-t3", null));
		String secondThreadName = "t3-second";
		CountDownLatch secondPreRead = new CountDownLatch(1);
		CountDownLatch release = new CountDownLatch(1);
		AtomicBoolean holdOnce = new AtomicBoolean(true);
		((GatingMdmMatchClaimSvc) myGatingClaimSvc).setBeforeClaimHook(() -> {
			if (Thread.currentThread().getName().equals(secondThreadName) && holdOnce.getAndSet(false)) {
				secondPreRead.countDown();
				awaitQuietly(release);
			}
		});

		ExecutorService executor = Executors.newSingleThreadExecutor(theRunnable -> new Thread(theRunnable, secondThreadName));
		try {
			Future<?> secondFuture = executor.submit(() -> updateLinks(second));
			assertThat(secondPreRead.await(10, TimeUnit.SECONDS)).isTrue();

			updateLinks(first);
			myMdmMatchClaimSvc.deleteEntriesOlderThan(new Date(System.currentTimeMillis() + 60_000));
			release.countDown();
			secondFuture.get(30, TimeUnit.SECONDS);
		} finally {
			executor.shutdownNow();
		}

		assertThat(getAllGoldenPatients()).hasSize(1);
		mdmAssertThat(first).is_MATCH_to(second);
	}

	/**
	 * T4: a source with more matching unprocessed sources than a single statement can take parameters for. The test
	 * datasource rejects statements with 1000 or more parameters; SQL Server rejects more than 2100.
	 */
	@Test
	void sourceWithOverAThousandUnlinkedMatches_isProcessed() {
		BundleBuilder bundle = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 1000; i++) {
			Patient match = patient("Abe", "Gale", "1980-01-01", null, null);
			match.setActive(true);
			bundle.addTransactionCreateEntry(match);
		}
		mySystemDao.transaction(mySrd, (Bundle) bundle.getBundle());
		Patient source = createPatient(patient("Abe", "Gale", "1980-01-01", null, null));

		updateLinks(source);

		assertThat(getGoldenResourceFromTargetResource(source)).isNotNull();
	}

	/**
	 * T5: claims are not scoped by partition, so two sources in different partitions that share an EID claim the same
	 * key and are forced into a serial order. The claim only orders them: each candidate search stays in its own
	 * partition, so each source still gets its own golden resource.
	 */
	@Test
	void sourcesInDifferentPartitionsSharingAnEid_waitForEachOtherButGetTheirOwnGoldenResources() {
		Integer defaultPartitionId = myPartitionSettings.getDefaultPartitionId();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		try {
			myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
			myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
			Patient firstSource = createActivePatientOnPartition(
					patient("Ada", "Hale", "1980-01-01", "npi-t5", null), RequestPartitionId.fromPartitionId(1));
			Patient secondSource = createActivePatientOnPartition(
					patient("Bea", "Ives", "1975-05-05", "npi-t5", null), RequestPartitionId.fromPartitionId(2));

			ScriptedInterleavingTestHarness.Run run = myHarness.run(
					ThreadSequence.P2,
					List.of(() -> updateLinks(firstSource), () -> updateLinks(secondSource)),
					List.of("first", "second"));

			assertThat(run.workers()).as("%s", run).allSatisfy(worker -> assertThat(worker.getFailure())
					.isNull());
			// The shared EID claim made one thread wait for the other
			assertThat(run.anyStep(ScriptedInterleavingTestHarness.StepOutcomeEnum.BLOCKED)
							|| run.count(ScriptedInterleavingTestHarness.EventTypeEnum.CONFLICT) > 0)
					.as("%s", run)
					.isTrue();
			assertThat(getAllGoldenPatients()).as("%s", run).hasSize(2);
			Patient firstGolden = getGoldenResourceFromTargetResource(firstSource);
			Patient secondGolden = getGoldenResourceFromTargetResource(secondSource);
			assertThat(firstGolden).isNotNull();
			assertThat(secondGolden).isNotNull();
			assertThat(firstGolden.getIdElement().toUnqualifiedVersionless())
					.isNotEqualTo(secondGolden.getIdElement().toUnqualifiedVersionless());
		} finally {
			myPartitionSettings.setPartitioningEnabled(false);
			myPartitionSettings.setDefaultPartitionId(defaultPartitionId);
		}
	}

	/**
	 * T6: a source keeps finding new matching sources with no MATCH link, one per attempt, until it runs out of
	 * restarts with the last one still unclaimed. That last source is then processed on another thread while the first
	 * source's attempt is still open. Its own pool doesn't contain the first source.
	 * <p>
	 * Running out of restarts must not let the first source decide without the last one. If the last source commits
	 * first, the first source must MATCH its golden resource, as a single consumer processing them in that order would.
	 */
	@Test
	void mdmSubmit_restartsRunOut_doNotLeaveADuplicateGoldenResource() throws Exception {
		// Each of these is in the first source's "Jo" pool and matches it, but none is in another's pool
		List<String> lateGivens = List.of("Johnathan", "Jonas", "Joel", "Josh", "Jordan", "Joseph");
		Patient first = createPatient(patient("Jo", "Quinn", "1980-01-01", null, null));
		String firstThreadName = "t6-first";
		String lastThreadName = "t6-last";
		ExecutorService firstExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, firstThreadName));
		ExecutorService lastExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, lastThreadName));
		ExecutorService creator = Executors.newSingleThreadExecutor();
		AtomicInteger attempts = new AtomicInteger();
		AtomicReference<Patient> lastLate = new AtomicReference<>();
		AtomicReference<Future<?>> lastProcessing = new AtomicReference<>();
		AtomicBoolean lastCommittedFirst = new AtomicBoolean();

		// Before each claim of the first source, store one more matching source, in its own committed transaction.
		// The attempts are the 5 restarts (MdmMatchLinkSvc.MAX_CLAIM_RESTARTS) plus the one that runs out.
		((GatingMdmMatchClaimSvc) myGatingClaimSvc).setBeforeClaimHook(() -> {
			if (Thread.currentThread().getName().equals(firstThreadName)) {
				int attempt = attempts.getAndIncrement();
				if (attempt < lateGivens.size()) {
					Patient late = patient(lateGivens.get(attempt), "Quinn", "1980-01-01", null, null);
					lastLate.set(getQuietly(creator.submit(() -> createPatient(late))));
				}
			}
		});

		// When the first source's attempt creates its golden resource, process the last source and let it commit first
		AtomicBoolean once = new AtomicBoolean(true);
		myInterceptorRegistry.registerAnonymousInterceptor(
				Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
					if (Thread.currentThread().getName().equals(firstThreadName)
							&& MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class))
							&& once.getAndSet(false)) {
						Future<?> processing = lastExecutor.submit(() -> updateLinks(lastLate.get()));
						lastProcessing.set(processing);
						try {
							// The last source finishes at once unless a Match Claim makes it wait for the first source
							processing.get(3, TimeUnit.SECONDS);
							lastCommittedFirst.set(true);
						} catch (TimeoutException e) {
							// It is waiting on the first source, so the first source commits first
						} catch (Exception e) {
							throw new IllegalStateException(e);
						}
					}
				});

		try {
			firstExecutor.submit(() -> updateLinks(first)).get(60, TimeUnit.SECONDS);
			assertThat(lastProcessing.get()).as("the last source was processed").isNotNull();
			lastProcessing.get().get(60, TimeUnit.SECONDS);
		} finally {
			firstExecutor.shutdownNow();
			lastExecutor.shutdownNow();
			creator.shutdownNow();
		}

		assertThat(attempts.get()).as("the first source ran out of restarts").isGreaterThanOrEqualTo(lateGivens.size());
		Patient firstGolden = getGoldenResourceFromTargetResource(first);
		Patient lastGolden = getGoldenResourceFromTargetResource(lastLate.get());
		assertThat(firstGolden).isNotNull();
		assertThat(lastGolden).isNotNull();
		if (lastCommittedFirst.get()) {
			assertThat(firstGolden.getIdElement().toUnqualifiedVersionless())
					.as("the last source committed first, so the first source must join its golden resource")
					.isEqualTo(lastGolden.getIdElement().toUnqualifiedVersionless());
		}
	}

	private static <T> T getQuietly(Future<T> theFuture) {
		try {
			return theFuture.get(30, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private Patient createActivePatientOnPartition(Patient thePatient, RequestPartitionId thePartitionId) {
		// The concurrency rules only match active patients
		thePatient.setActive(true);
		return createPatientOnPartition(thePatient, false, false, thePartitionId);
	}

	private void updateLinks(Patient theSource) {
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(theSource, createContextForCreate("Patient"));
	}

	private static void awaitQuietly(CountDownLatch theLatch) {
		try {
			theLatch.await(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
