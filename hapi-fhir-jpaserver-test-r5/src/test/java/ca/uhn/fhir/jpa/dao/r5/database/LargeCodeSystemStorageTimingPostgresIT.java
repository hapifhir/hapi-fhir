package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import com.sun.management.GarbageCollectionNotificationInfo;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Times the storing of one large, deeply nested CodeSystem resource on Postgres, and logs the request time and an
 * upper bound of the heap the store needed. Full-text indexing is on;
 * {@link LargeCodeSystemStorageTimingPostgresNoFullTextIT} measures with it off.
 * It is R5 only because the Postgres test setup lives in this module; storage is the same for every FHIR version.
 * <p>
 * A performance check to run by hand, so it is disabled and does not run in CI. It needs Docker:
 * </p>
 * <pre>
 * mvn -pl hapi-fhir-jpaserver-test-r5 test -Dtest=LargeCodeSystemStorageTimingPostgresIT \
 *   '-Djunit.jupiter.conditions.deactivate=org.junit.*DisabledCondition'
 * </pre>
 * <p>
 * The figures are logged to this class's {@code -output.txt} file in {@code target/surefire-reports/}. Change the
 * size with {@code -DcodeSystemTiming.concepts=} and {@code -DcodeSystemTiming.depth=}.
 * </p>
 */
// Created by claude-fable-5-2
@Disabled("Performance test — run manually to time the storage of a large CodeSystem resource")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
		classes = {BaseDatabaseVerificationIT.TestConfig.class, EmbeddedDatabaseConfigurations.Postgres.class})
class LargeCodeSystemStorageTimingPostgresIT extends BaseJpaTest {

	private static final Logger ourLog = LoggerFactory.getLogger(LargeCodeSystemStorageTimingPostgresIT.class);

	private static final int CONCEPT_COUNT = Integer.getInteger("codeSystemTiming.concepts", 100_000);
	private static final int DEPTH = Integer.getInteger("codeSystemTiming.depth", 50);
	private static final int WARM_UP_CONCEPT_COUNT = Math.max(1, CONCEPT_COUNT / DEPTH / 2) * DEPTH;
	private static final long BYTES_PER_MB = 1024 * 1024;

	@Autowired
	private PlatformTransactionManager myTxManager;

	private int myCodeSystemCount;

	@Test
	void storeLargeNestedCodeSystem_logsTimeAndHeap() {
		assertThat(CONCEPT_COUNT % DEPTH)
				.as("the concept count has to be a multiple of the depth")
				.isZero();

		// Runs first at half the size, so that the measured run doesn't also pay for class loading and JIT
		storeAndMeasure(WARM_UP_CONCEPT_COUNT);

		Measurement measurement = storeAndMeasure(CONCEPT_COUNT);

		ourLog.info(
				"CodeSystem storage timing on Postgres: {} concepts, {} levels deep, full-text indexing {}, "
						+ "{} MB heap limit\n{}\n{}",
				CONCEPT_COUNT,
				DEPTH,
				isFullTextIndexingOn() ? "on" : "off",
				Runtime.getRuntime().maxMemory() / BYTES_PER_MB,
				Measurement.HEADER,
				measurement.toRow());
	}

	private Measurement storeAndMeasure(int theConceptCount) {
		IFhirResourceDao<CodeSystem> codeSystemDao = myDaoRegistry.getResourceDao(CodeSystem.class);
		CodeSystem codeSystem = newChainedCodeSystem(theConceptCount);
		long conceptsBefore = countConcepts();
		long linksBefore = countLinks();

		long requestMillis;
		long heapBeforeMb;
		long maxHeapAfterGcMb;
		try (RetainedHeapTracker heapTracker = new RetainedHeapTracker()) {
			StopWatch stopWatch = new StopWatch();
			codeSystemDao.create(codeSystem, new SystemRequestDetails());
			requestMillis = stopWatch.getMillis();

			heapBeforeMb = heapTracker.getHeapBeforeMb();
			maxHeapAfterGcMb = heapTracker.getMaxHeapAfterGcMb();
		}

		// Nothing saves deferred work in this test, so these are what the request itself has stored
		int chainCount = theConceptCount / DEPTH;
		assertThat(countConcepts() - conceptsBefore)
				.as("concepts stored by the request")
				.isEqualTo(theConceptCount);
		assertThat(countLinks() - linksBefore)
				.as("parent-child links stored by the request")
				.isEqualTo(theConceptCount - chainCount);
		assertThat(myTermDeferredStorageSvc.isStorageQueueEmpty(false))
				.as("nothing is left queued for deferred storage")
				.isTrue();

		Measurement measurement = new Measurement(requestMillis, heapBeforeMb, maxHeapAfterGcMb);
		ourLog.info("Stored {} concepts: {}", theConceptCount, measurement);
		return measurement;
	}

	private CodeSystem newChainedCodeSystem(int theConceptCount) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/code-system-storage-timing/" + myCodeSystemCount++);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(Enumerations.CodeSystemContentMode.COMPLETE);

		for (int chain = 0; chain < theConceptCount / DEPTH; chain++) {
			CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
			nameConcept(concept, chain, 1);
			for (int level = 2; level <= DEPTH; level++) {
				concept = concept.addConcept();
				nameConcept(concept, chain, level);
			}
		}
		return codeSystem;
	}

	private static void nameConcept(CodeSystem.ConceptDefinitionComponent theConcept, int theChain, int theLevel) {
		theConcept.setCode("chain" + theChain + "-level" + theLevel);
		theConcept.setDisplay("Chain " + theChain + " level " + theLevel);
	}

	private boolean isFullTextIndexingOn() {
		return myFulltestSearchSvc != null && !myFulltestSearchSvc.isDisabled();
	}

	private long countConcepts() {
		return runInTransaction(() -> myTermConceptDao.count());
	}

	private long countLinks() {
		return runInTransaction(() -> myTermConceptParentChildLinkDao.count());
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	private record Measurement(long requestMillis, long heapBeforeMb, long maxHeapAfterGcMb) {

		private static final String ROW_FORMAT = "%10s %13s %17s %13s";
		static final String HEADER =
				String.format(ROW_FORMAT, "requestMs", "heapBeforeMb", "maxHeapAfterGcMb", "heapGrowthMb");

		String toRow() {
			return String.format(
					ROW_FORMAT, requestMillis, heapBeforeMb, maxHeapAfterGcMb, maxHeapAfterGcMb - heapBeforeMb);
		}
	}

	/**
	 * Records the largest heap in use right after a garbage collection while it is open. That is the closest
	 * the JVM gets to reporting how much memory the store had to keep, as opposed to how much it allocated.
	 */
	private static final class RetainedHeapTracker implements NotificationListener, AutoCloseable {

		/**
		 * The collector also reports pauses that leave the young generation as it was, such as G1's remark and
		 * cleanup. Heap in use after those includes garbage, so only real collections are counted.
		 */
		private static final Set<String> COLLECTION_ACTIONS = Set.of("end of minor GC", "end of major GC");

		private final Set<String> myHeapPoolNames = ManagementFactory.getMemoryPoolMXBeans().stream()
				.filter(t -> t.getType() == MemoryType.HEAP)
				.map(MemoryPoolMXBean::getName)
				.collect(Collectors.toSet());
		private final long myHeapBefore;
		private final AtomicLong myMaxHeapAfterGc;

		RetainedHeapTracker() {
			System.gc();
			myHeapBefore = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
			myMaxHeapAfterGc = new AtomicLong(myHeapBefore);
			for (GarbageCollectorMXBean next : ManagementFactory.getGarbageCollectorMXBeans()) {
				((NotificationEmitter) next).addNotificationListener(this, null, null);
			}
		}

		@Override
		public void handleNotification(Notification theNotification, Object theHandback) {
			if (!GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION.equals(theNotification.getType())) {
				return;
			}
			GarbageCollectionNotificationInfo collection =
					GarbageCollectionNotificationInfo.from((CompositeData) theNotification.getUserData());
			if (!COLLECTION_ACTIONS.contains(collection.getGcAction())) {
				return;
			}
			long heapAfterGc = collection.getGcInfo().getMemoryUsageAfterGc().entrySet().stream()
					.filter(t -> myHeapPoolNames.contains(t.getKey()))
					.mapToLong(t -> t.getValue().getUsed())
					.sum();
			myMaxHeapAfterGc.accumulateAndGet(heapAfterGc, Math::max);
		}

		long getHeapBeforeMb() {
			return myHeapBefore / BYTES_PER_MB;
		}

		long getMaxHeapAfterGcMb() {
			return myMaxHeapAfterGc.get() / BYTES_PER_MB;
		}

		@Override
		public void close() {
			for (GarbageCollectorMXBean next : ManagementFactory.getGarbageCollectorMXBeans()) {
				try {
					((NotificationEmitter) next).removeNotificationListener(this);
				} catch (ListenerNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}
}
