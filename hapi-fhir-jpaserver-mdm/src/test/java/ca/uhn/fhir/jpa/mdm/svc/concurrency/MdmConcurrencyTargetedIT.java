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
import java.util.concurrent.atomic.AtomicBoolean;

import static ca.uhn.fhir.jpa.mdm.svc.concurrency.MdmConcurrencyDataPatterns.patient;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency cases T1-T5, which don't fit the sequence-by-data matrix in {@link MdmConcurrencyMatrixIT}. They share
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
	private ConcurrencyScheduler myScheduler;

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

		ConcurrencyScheduler.Run run = myScheduler.run(
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

			ConcurrencyScheduler.Run run = myScheduler.run(
					ThreadSequence.P2,
					List.of(() -> updateLinks(firstSource), () -> updateLinks(secondSource)),
					List.of("first", "second"));

			assertThat(run.workers()).as("%s", run).allSatisfy(worker -> assertThat(worker.getFailure())
					.isNull());
			// The shared EID claim made one thread wait for the other
			assertThat(run.anyStep(ConcurrencyScheduler.StepOutcomeEnum.BLOCKED)
							|| run.count(ConcurrencyScheduler.EventTypeEnum.CONFLICT) > 0)
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
