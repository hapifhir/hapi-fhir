package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrent MDM processing of source resources that match each other must not produce duplicate golden
 * resources. Each test stores its sources without MDM processing them, then drives
 * {@link MdmMatchLinkSvc#updateMdmLinksForMdmSource} from several threads at once, using storage hooks to
 * force the interleaving that used to race.
 * <p>
 * Gates never wait longer than {@link #GATE_TIMEOUT_MILLIS}: once the fix is in place the competing
 * thread blocks on a match claim rather than reaching the gate, so the gate has to give up and let the
 * holder commit.
 */
@TestPropertySource(properties = {"module.mdm.config.script.file=classpath:mdm/mdm-rules-multi-eid-systems.json"})
// Created by claude-opus-5-5
public class MdmMatchLinkSvcConcurrencyIT extends BaseMdmR4Test {

	private static final long GATE_TIMEOUT_MILLIS = 1000;
	private ExecutorService myExecutor;
	@Autowired
	private IMdmMatchClaimJpaRepository myMdmMatchClaimJpaRepository;

	@BeforeEach
	void beforeCreateExecutor() {
		myExecutor = Executors.newFixedThreadPool(8);
	}

	@AfterEach
	void afterShutdownExecutor() {
		myExecutor.shutdownNow();
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
		runInTransaction(() -> myMdmMatchClaimJpaRepository.deleteAll());
	}

	/**
	 * Two new, mutually matching sources without EIDs. Each thread's search sees the other source with no
	 * MATCH link yet, so before the fix each created its own golden resource.
	 */
	@Test
	void twoMatchingNewSourcesWithoutEids_shareOneGoldenResource() throws Exception {
		Patient first = createPatient(buildJanePatient());
		Patient second = createPatient(buildJanePatient());
		assertLinkCount(0);
		holdFirstGoldenResourceCreationUntilSecondArrives();

		runConcurrently(first, second);

		assertThat(getAllGoldenPatients()).hasSize(1);
		mdmAssertThat(first).is_MATCH_to(second);
		assertLinksMatchResult(MATCH, MATCH);
	}

	/**
	 * Two new sources sharing an external EID but otherwise different.
	 */
	@Test
	void twoNewSourcesSharingAnEid_shareOneGoldenResource() throws Exception {
		Patient first = createPatient(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1"));
		Patient second = createPatient(addExternalEID(buildPaulPatient(), mrnSystem(), "mrn-1"));
		holdFirstGoldenResourceCreationUntilSecondArrives();

		runConcurrently(first, second);

		assertThat(getAllGoldenPatients()).hasSize(1);
		mdmAssertThat(first).is_MATCH_to(second);
	}

	/**
	 * The multi-EID scenario. GR_1 already carries an NPI. P2 (NPI + MRN) is linked to GR_1, adding the
	 * MRN, while P3 (MRN only) is processed. P3 must end up on GR_1 too.
	 */
	@Test
	void sourceMatchingByAnEidBeingAddedConcurrently_joinsThatGoldenResource() throws Exception {
		Patient p1 = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), npiSystem(), "npi-9"));
		Patient p2 = addExternalEID(buildPaulPatient(), npiSystem(), "npi-9");
		p2 = createPatient(addExternalEID(p2, mrnSystem(), "mrn-1"));
		Patient p3 = createPatient(addExternalEID(buildFrankPatient(), mrnSystem(), "mrn-1"));
		IIdType goldenId = getGoldenResourceFromTargetResource(p1).getIdElement().toUnqualifiedVersionless();

		// Hold P2's transaction while it updates GR_1 with the MRN, until P3 creates a golden resource
		CountDownLatch p2Holding = new CountDownLatch(1);
		CountDownLatch release = new CountDownLatch(1);
		AtomicBoolean holdOnce = new AtomicBoolean(true);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, (thePointcut, theArgs) -> {
			IBaseResource resource = theArgs.get(IBaseResource.class, 1);
			if (MdmResourceUtil.isGoldenRecord(resource) && holdOnce.getAndSet(false)) {
				p2Holding.countDown();
				awaitQuietly(release);
			}
		});
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
			if (MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class))) {
				release.countDown();
			}
		});

		Patient finalP2 = p2;
		Future<?> p2Future = myExecutor.submit(() -> updateLinks(finalP2));
		assertThat(p2Holding.await(10, TimeUnit.SECONDS)).isTrue();
		Future<?> p3Future = myExecutor.submit(() -> updateLinks(p3));
		p2Future.get(30, TimeUnit.SECONDS);
		p3Future.get(30, TimeUnit.SECONDS);

		assertThat(getAllGoldenPatients()).hasSize(1);
		assertThat(getGoldenResourceFromTargetResource(p2).getIdElement().toUnqualifiedVersionless()).isEqualTo(goldenId);
		assertThat(getGoldenResourceFromTargetResource(p3).getIdElement().toUnqualifiedVersionless()).isEqualTo(goldenId);
		assertLinksMatchResult(MATCH, MATCH, MATCH);
	}

	/**
	 * Sources that don't match must not wait on each other.
	 */
	@Test
	void nonMatchingSources_areProcessedInParallel() throws Exception {
		Patient jane = createPatient(buildJanePatient());
		Patient paul = createPatient(buildPaulPatient());
		CountDownLatch arrivals = new CountDownLatch(2);
		AtomicBoolean bothArrivedTogether = new AtomicBoolean();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
			if (MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class))) {
				arrivals.countDown();
				if (awaitQuietly(arrivals)) {
					bothArrivedTogether.set(true);
				}
			}
		});

		runConcurrently(jane, paul);

		assertThat(getAllGoldenPatients()).hasSize(2);
		assertThat(bothArrivedTogether).isTrue();
	}

	@RepeatedTest(3)
	void manyMatchingSourcesOnManyThreads_shareOneGoldenResource() throws Exception {
		List<Patient> sources = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			sources.add(createPatient(buildJanePatient()));
		}

		runConcurrently(sources.toArray(new Patient[0]));

		assertThat(getAllGoldenPatients()).hasSize(1);
		for (Patient source : sources) {
			mdmAssertThat(source).is_MATCH_to(sources.get(0));
		}
	}

	/**
	 * A retriable failure mid-attempt rolls the attempt back and retries it, without leaking the rolled-back
	 * attempt's link events into the context.
	 */
	@Test
	void retriableFailure_isRetriedWithoutDuplicatingContextState() {
		Patient jane = createPatient(buildJanePatient());
		AtomicBoolean failOnce = new AtomicBoolean(true);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
			if (MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class)) && failOnce.getAndSet(false)) {
				throw new ResourceVersionConflictException("simulated conflict");
			}
		});

		MdmTransactionContext context =
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(jane, createContextForCreate("Patient"));

		assertThat(failOnce).isFalse();
		assertThat(getAllGoldenPatients()).hasSize(1);
		assertLinksMatchResult(MATCH);
		assertThat(context.getMdmLinks()).hasSize(1);
		assertThat(getOnlyGoldenPatient().getIdentifier()).hasSize(1);
	}

	/**
	 * Reprocessing a source within the retention window takes over its own earlier claim.
	 */
	@Test
	void reprocessingASource_takesOverItsEarlierClaim() {
		Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1"));
		long claimsAfterFirstPass = runInTransaction(() -> myMdmMatchClaimJpaRepository.count());

		jane.getNameFirstRep().setFamily("Smith");
		updatePatientAndUpdateLinks(jane);

		assertThat(claimsAfterFirstPass).isEqualTo(2);
		assertThat(runInTransaction(() -> myMdmMatchClaimJpaRepository.count())).isEqualTo(2);
		assertThat(getAllGoldenPatients()).hasSize(1);
	}

	/**
	 * Callers already in a transaction (such as the link updater) join it.
	 */
	@Test
	void callerInATransaction_joinsIt() {
		Patient first = createPatient(buildJanePatient());
		Patient second = createPatient(buildJanePatient());

		runInTransaction(() -> {
			updateLinks(first);
			updateLinks(second);
		});

		assertThat(getAllGoldenPatients()).hasSize(1);
		mdmAssertThat(first).is_MATCH_to(second);
	}

	@Test
	void matchClaimsDisabled_writesNoClaims() {
		myMdmSettings.setMatchClaimsEnabled(false);
		try {
			createPatientAndUpdateLinks(buildJanePatient());
		} finally {
			myMdmSettings.setMatchClaimsEnabled(true);
		}

		assertThat(getAllGoldenPatients()).hasSize(1);
		assertThat(runInTransaction(() -> myMdmMatchClaimJpaRepository.count())).isZero();
	}

	private void holdFirstGoldenResourceCreationUntilSecondArrives() {
		CountDownLatch arrivals = new CountDownLatch(2);
		AtomicInteger gated = new AtomicInteger();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, (thePointcut, theArgs) -> {
			if (MdmResourceUtil.isGoldenRecord(theArgs.get(IBaseResource.class)) && gated.incrementAndGet() <= 2) {
				arrivals.countDown();
				awaitQuietly(arrivals);
			}
		});
	}

	private void runConcurrently(Patient... theSources) throws Exception {
		List<Future<?>> futures = new ArrayList<>();
		for (Patient source : theSources) {
			futures.add(myExecutor.submit(() -> updateLinks(source)));
		}
		for (Future<?> future : futures) {
			future.get(30, TimeUnit.SECONDS);
		}
	}

	private void updateLinks(Patient theSource) {
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(theSource, createContextForCreate("Patient"));
	}

	private static boolean awaitQuietly(CountDownLatch theLatch) {
		try {
			return theLatch.await(GATE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}
}
