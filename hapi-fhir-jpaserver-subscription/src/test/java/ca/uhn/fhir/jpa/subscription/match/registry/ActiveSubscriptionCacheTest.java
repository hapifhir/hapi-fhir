package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.model.primitive.IdDt;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActiveSubscriptionCacheTest {
	static final String ID1 = "id1";
	static final String ID2 = "id2";
	static final String ID3 = "id3";
	public static final String TEST_TOPIC_URL = "http://test.topic";
	public static final String TEST_TOPIC_URL_OTHER = "http://test.topic.other";

	@Test
	public void twoPhaseDelete() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = buildActiveSubscription(ID1);
		activeSubscriptionCache.put(ID1, activeSub1);
		assertFalse(activeSub1.isFlagForDeletion());
		List<String> saveIds = new ArrayList<>();

		List<String> idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertTrue(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(ID1));
		assertThat(idsToDelete).isEmpty();

		idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertThat(idsToDelete).containsExactlyInAnyOrder(ID1);
	}

	private ActiveSubscription buildActiveSubscription(String theId) {
		CanonicalSubscription canonicalSubscription = new CanonicalSubscription();
		canonicalSubscription.setIdElement(new IdDt(theId));
		return new ActiveSubscription(canonicalSubscription, null);
	}

	@Test
	public void secondPassUnflags() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = buildActiveSubscription(ID1);
		List<String> saveIds = new ArrayList<>();
		activeSubscriptionCache.put(ID1, activeSub1);

		assertFalse(activeSub1.isFlagForDeletion());

		List<String> idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertTrue(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(ID1));
		assertThat(idsToDelete).isEmpty();

		saveIds.add(ID1);
		idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertFalse(activeSub1.isFlagForDeletion());
		assertThat(idsToDelete).isEmpty();
	}

	@Test
	public void onlyFlaggedDeleted() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();

		ActiveSubscription activeSub1 = buildActiveSubscription(ID1);
		ActiveSubscription activeSub2 = buildActiveSubscription(ID2);
		activeSubscriptionCache.put(activeSub1.getId(), activeSub1);
		activeSubscriptionCache.put(activeSub2.getId(), activeSub2);

		activeSub1.setFlagForDeletion(true);
		List<String> saveIds = new ArrayList<>();

		List<String> idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);

		assertThat(idsToDelete).containsExactlyInAnyOrder(ID1);
		assertNotNull(activeSubscriptionCache.get(ID2));
		assertTrue(activeSub2.isFlagForDeletion());
	}

	@Test
	public void onListSavesAndUnmarksFlag() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = buildActiveSubscription(ID1);

		ActiveSubscription activeSub2 = buildActiveSubscription(ID2);

		activeSubscriptionCache.put(ID1, activeSub1);
		activeSubscriptionCache.put(ID2, activeSub2);

		activeSub1.setFlagForDeletion(true);
		List<String> saveIds = new ArrayList<>();
		saveIds.add(ID1);
		saveIds.add(ID2);

		activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);

		assertNotNull(activeSubscriptionCache.get(ID1));
		assertFalse(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(ID2));
		assertFalse(activeSub2.isFlagForDeletion());
	}

	@Test
	public void getTopicSubscriptionsForUrl() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = buildActiveSubscription(ID1);
		activeSubscriptionCache.put(ID1, activeSub1);
		assertThat(activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL)).hasSize(0);

		ActiveSubscription activeSub2 = buildTopicSubscription(ID2, TEST_TOPIC_URL);
		activeSubscriptionCache.put(ID2, activeSub2);
		assertThat(activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL)).hasSize(1);
		ActiveSubscription match = activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL).get(0);
		assertEquals(ID2, match.getId());

		ActiveSubscription activeSub3 = buildTopicSubscription(ID3, TEST_TOPIC_URL_OTHER);
		activeSubscriptionCache.put(ID3, activeSub3);
		assertThat(activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL)).hasSize(1);
		match = activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL).get(0);
		assertEquals(ID2, match.getId());

		assertThat(activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL_OTHER)).hasSize(1);
		match = activeSubscriptionCache.getTopicSubscriptionsForTopic(TEST_TOPIC_URL_OTHER).get(0);
		assertEquals(ID3, match.getId());
	}

	@Nonnull
	private ActiveSubscription buildTopicSubscription(String theId, String theTopicUrl) {
		ActiveSubscription activeSub2 = buildActiveSubscription(theId);
		activeSub2.getSubscription().setTopicSubscription(true);
		activeSub2.getSubscription().getTopicSubscription().setTopic(theTopicUrl);
		return activeSub2;
	}

}
