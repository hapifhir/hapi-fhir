package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMatchClaimSvcJpaImpl;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-5-5
class MdmMatchClaimSvcJpaImplIT extends BaseMdmR4Test {

	private static final MdmMatchClaimKey PID_KEY = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(1L));
	private static final MdmMatchClaimKey EID_KEY =
		MdmMatchClaimKey.forEid("Patient", new CanonicalEID("http://mrn", "1", null));

	@Autowired
	private MdmMatchClaimSvcJpaImpl mySvc;
	@Autowired
	private IMdmMatchClaimJpaRepository myRepository;

	@AfterEach
	void afterDeleteClaims() {
		runInTransaction(() -> myRepository.deleteAll());
	}

	@Test
	void claim_persistsOneRowPerKey() {
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY, EID_KEY, PID_KEY), JpaPid.fromId(1L), Map.of()));

		assertThat(runInTransaction(() -> myRepository.count())).isEqualTo(2);
		assertThat(runInTransaction(() -> mySvc.findExistingClaims(List.of(PID_KEY, EID_KEY)))).containsOnlyKeys(PID_KEY, EID_KEY);
	}

	@Test
	void claim_whenAlreadyClaimed_conflicts() {
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, Map.of()));

		assertThatThrownBy(() -> runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, Map.of())))
			.isInstanceOf(ResourceVersionConflictException.class)
			.hasMessageContaining("HAPI-3062");
	}

	@Test
	void claim_withTheExistingToken_takesTheClaimOver() {
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, Map.of()));
		Long originalToken = runInTransaction(() -> mySvc.findExistingClaims(List.of(PID_KEY)).get(PID_KEY));

		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, mySvc.findExistingClaims(List.of(PID_KEY))));

		Long newToken = runInTransaction(() -> mySvc.findExistingClaims(List.of(PID_KEY)).get(PID_KEY));
		assertThat(newToken).isNotNull().isNotEqualTo(originalToken);
		assertThat(runInTransaction(() -> myRepository.count())).isEqualTo(1);
	}

	@Test
	void claim_withAStaleToken_conflicts() {
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, Map.of()));
		Map<MdmMatchClaimKey, Long> staleTokens = runInTransaction(() -> mySvc.findExistingClaims(List.of(PID_KEY)));
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, mySvc.findExistingClaims(List.of(PID_KEY))));

		assertThatThrownBy(() -> runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, staleTokens)))
			.isInstanceOf(ResourceVersionConflictException.class);
	}

	/**
	 * The second transaction must wait for the first and then fail, rather than succeed or fail early.
	 */
	@Test
	void claim_heldByAnUncommittedTransaction_failsOnceItCommits() throws Exception {
		CountDownLatch claimed = new CountDownLatch(1);
		CountDownLatch commit = new CountDownLatch(1);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<?> holder = executor.submit(() -> runInTransaction(() -> {
				mySvc.claim(List.of(EID_KEY), null, Map.of());
				claimed.countDown();
				awaitQuietly(commit);
			}));
			assertThat(claimed.await(10, TimeUnit.SECONDS)).isTrue();

			long start = System.currentTimeMillis();
			Thread releaser = new Thread(() -> {
				sleepQuietly(500);
				commit.countDown();
			});
			releaser.start();
			assertThatThrownBy(() -> runInTransaction(() -> mySvc.claim(List.of(EID_KEY), null, Map.of())))
				.isInstanceOf(ResourceVersionConflictException.class);
			assertThat(System.currentTimeMillis() - start).isGreaterThanOrEqualTo(400);
			holder.get(10, TimeUnit.SECONDS);
		} finally {
			executor.shutdownNow();
		}
	}

	@Test
	void deleteEntriesOlderThan_removesOnlyOlderClaims() {
		runInTransaction(() -> mySvc.claim(List.of(PID_KEY), null, Map.of()));
		Date cutoff = new Date(System.currentTimeMillis() + 1000);
		runInTransaction(() -> mySvc.claim(List.of(EID_KEY), null, Map.of()));
		runInTransaction(() -> myRepository.findAll().forEach(entity -> {
			if (entity.getClaimKey().equals(EID_KEY.canonicalKey())) {
				entity.setCreatedTime(new Date(System.currentTimeMillis() + 60_000));
			}
		}));

		mySvc.deleteEntriesOlderThan(cutoff);

		assertThat(runInTransaction(() -> mySvc.findExistingClaims(List.of(PID_KEY, EID_KEY)))).containsOnlyKeys(EID_KEY);
	}

	private static void awaitQuietly(CountDownLatch theLatch) {
		try {
			theLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private static void sleepQuietly(long theMillis) {
		try {
			Thread.sleep(theMillis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
