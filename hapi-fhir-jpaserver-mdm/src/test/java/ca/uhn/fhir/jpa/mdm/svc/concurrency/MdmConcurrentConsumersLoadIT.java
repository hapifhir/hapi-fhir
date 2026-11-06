package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.jpa.dao.mdm.MdmMatchClaimSvcJpaImpl;
import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.broker.MdmQueueConsumerLoader;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperConfig;
import ca.uhn.fhir.jpa.mdm.helper.MdmHelperR4;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// set MdmHelperConfig setMatchClaimsEnabled false to verify it fails
/**
 * Drives a batch of resources through the real MDM channel with several concurrent consumers, rather than calling
 * {@link ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc} directly, and checks that every cluster of matching sources ends
 * up on exactly one golden resource.
 * <p>
 * The defaults keep CI fast. For a heavier run, override them with system properties, for example
 * {@code -Dmdm.load.consumers=16 -Dmdm.load.clusters=500 -Dmdm.load.sourcesPerCluster=20}.
 */
@TestPropertySource(properties = {
	"mdm.concurrent_consumers=${mdm.load.consumers:8}"
})
@ContextConfiguration(classes = {MdmHelperConfig.class})
// Created by claude-opus-5-5
class MdmConcurrentConsumersLoadIT extends BaseMdmR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmConcurrentConsumersLoadIT.class);

	private static final int CLUSTERS = Integer.getInteger("mdm.load.clusters", 20);
	private static final int SOURCES_PER_CLUSTER = Integer.getInteger("mdm.load.sourcesPerCluster", 10);
	private static final int SUBMITTER_THREADS = Integer.getInteger("mdm.load.submitters", 4);
	private static final int TIMEOUT_SECONDS = Integer.getInteger("mdm.load.timeoutSeconds", 300);
	private static final LocalDate FIRST_BIRTH_DATE = LocalDate.of(1950, 1, 1);

	@RegisterExtension
	@Autowired
	MdmHelperR4 myMdmHelper;

	@RegisterExtension
	final LogbackTestExtension myMdmLog =
			new LogbackTestExtension(Logs.getMdmTroubleshootingLog().getName(), Level.DEBUG);

	@RegisterExtension
	final LogbackTestExtension myClaimLog = new LogbackTestExtension(MdmMatchClaimSvcJpaImpl.class, Level.DEBUG);

	@Autowired
	private MdmQueueConsumerLoader myMdmQueueConsumerLoader;

	@Autowired
	private IMdmMatchClaimJpaRepository myMdmMatchClaimJpaRepository;

	@AfterEach
	void afterDeleteClaims() {
		runInTransaction(() -> myMdmMatchClaimJpaRepository.deleteAll());
	}

	@Test
	void manyMatchingClustersOnConcurrentConsumers_eachClusterHasOneGoldenResource() throws Exception {
		// Setup
		int consumers = myMdmSettings.getConcurrentConsumers();
		assertThat(consumers).as("configured MDM consumers").isGreaterThan(1);
		assertThat(getMdmConsumerPoolSize()).as("MDM channel consumer threads").isEqualTo(consumers);

		// Each cluster's sources are submitted back to back, so the consumers race on the same cluster's first golden
		// resource rather than mostly working on different clusters
		List<Integer> toCreate = new ArrayList<>();
		for (int cluster = 0; cluster < CLUSTERS; cluster++) {
			for (int i = 0; i < SOURCES_PER_CLUSTER; i++) {
				toCreate.add(cluster);
			}
		}

		// Execute
		int total = toCreate.size();
		myMdmHelper.getAfterMdmLatch().setExpectedCount(total);
		long start = System.currentTimeMillis();
		Map<String, Integer> clusterBySourceId = createConcurrently(toCreate);
		myMdmHelper.getAfterMdmLatch().awaitExpectedWithTimeout(TIMEOUT_SECONDS);
		long elapsed = System.currentTimeMillis() - start;
		ourLog.info(
				"MDM processed {} sources in {} clusters on {} consumers in {}ms ({} sources/sec)",
				total,
				CLUSTERS,
				consumers,
				elapsed,
				String.format("%.1f", total * 1000.0d / Math.max(1, elapsed)));

		// Verify
		assertThat(getAllGoldenPatients()).as("golden resources").hasSize(CLUSTERS);

		List<MdmLink> links = runInTransaction(() -> myMdmLinkDao.findAll());
		assertThat(links)
				.as("links other than MATCH")
				.allMatch(link -> link.getMatchResult() == MdmMatchResultEnum.MATCH);
		assertThat(links).as("MATCH links").hasSize(total);

		Map<Integer, Set<JpaPid>> goldenByCluster = new HashMap<>();
		for (MdmLink link : links) {
			String sourceId = myPatientDao
					.readByPid(link.getSourcePersistenceId())
					.getIdElement()
					.toUnqualifiedVersionless()
					.getValue();
			Integer cluster = clusterBySourceId.get(sourceId);
			assertThat(cluster).as("cluster of %s", sourceId).isNotNull();
			goldenByCluster
					.computeIfAbsent(cluster, theCluster -> new HashSet<>())
					.add(link.getGoldenResourcePersistenceId());
		}
		assertThat(goldenByCluster).hasSize(CLUSTERS);
		goldenByCluster.forEach((theCluster, theGoldens) ->
				assertThat(theGoldens).as("golden resources of cluster %s", theCluster).hasSize(1));

		// The claims must actually have serialized competing sources, not just been lucky
		long restarts = countMessagesContaining(myMdmLog, "MDM processing of", "restarting to claim");
		long conflicts = countMessagesContaining(myClaimLog, "MDM match claim conflict");
		ourLog.info("MDM match claims: {} restarts, {} claim conflicts", restarts, conflicts);
		assertThat(restarts + conflicts).as("restarts and claim conflicts").isPositive();
		assertThat(countMessagesContaining(myMdmLog, "MDM processing of", "unclaimed matching source"))
				.as("sources processed after running out of restarts")
				.isZero();
	}

	/**
	 * Creates the sources from several threads, so that MDM messages arrive faster than one consumer could take them.
	 *
	 * @return the cluster of each created source, keyed by its unqualified versionless id
	 */
	private Map<String, Integer> createConcurrently(List<Integer> theClusters) throws Exception {
		ExecutorService submitters = Executors.newFixedThreadPool(SUBMITTER_THREADS);
		try {
			List<Future<String>> futures = new ArrayList<>();
			for (Integer cluster : theClusters) {
				futures.add(submitters.submit(() -> myMdmHelper
						.doCreateResource(buildClusterPatient(cluster), false)
						.getId()
						.toUnqualifiedVersionless()
						.getValue()));
			}
			Map<String, Integer> retVal = new HashMap<>();
			for (int i = 0; i < theClusters.size(); i++) {
				retVal.put(futures.get(i).get(TIMEOUT_SECONDS, TimeUnit.SECONDS), theClusters.get(i));
			}
			return retVal;
		} finally {
			submitters.shutdownNow();
		}
	}

	/**
	 * Sources in a cluster share a birth date, which is the candidate search parameter, and a given and family name,
	 * which together are a MATCH. Different clusters share neither.
	 */
	private static Patient buildClusterPatient(int theCluster) {
		Patient retVal = new Patient();
		retVal.setActive(true);
		retVal.addName().setFamily("Family" + toLetters(theCluster)).addGiven("Given" + toLetters(theCluster));
		retVal.setBirthDateElement(new DateType(FIRST_BIRTH_DATE.plusDays(theCluster).toString()));
		return retVal;
	}

	/**
	 * Letters rather than digits, so the string matchers can't treat two clusters' names as near-identical numbers.
	 */
	private static String toLetters(int theValue) {
		StringBuilder retVal = new StringBuilder();
		int remaining = theValue;
		do {
			retVal.append((char) ('a' + remaining % 26));
			remaining /= 26;
		} while (remaining > 0);
		return retVal.append("xyz").toString();
	}

	/**
	 * Counts the log lines containing every given phrase. Phrases rather than whole messages, so that rewording a line
	 * doesn't break the test, but specific enough to grep the logs of a real run for.
	 */
	private static long countMessagesContaining(LogbackTestExtension theLog, String... thePhrases) {
		return theLog.getLogEvents().stream()
				.map(ILoggingEvent::getFormattedMessage)
				.filter(theMessage -> Arrays.stream(thePhrases).allMatch(theMessage::contains))
				.count();
	}

	@SuppressWarnings("unchecked")
	private int getMdmConsumerPoolSize() {
		SpringMessagingReceiverAdapter<ResourceModifiedMessage> adapter =
				(SpringMessagingReceiverAdapter<ResourceModifiedMessage>)
						myMdmQueueConsumerLoader.getMdmChannelConsumerForUnitTest();
		LinkedBlockingChannel channel = (LinkedBlockingChannel) adapter.getSpringMessagingChannelReceiver();
		return ((ThreadPoolTaskExecutor) channel.getExecutor()).getMaxPoolSize();
	}
}
