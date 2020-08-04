package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActiveSubscriptionCacheTest {
	static final String ID1 = "id1";
	static final String ID2 = "id2";

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
		assertEquals(0, idsToDelete.size());

		idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertThat(idsToDelete, containsInAnyOrder(ID1));
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
		assertEquals(0, idsToDelete.size());

		saveIds.add(ID1);
		idsToDelete = activeSubscriptionCache.markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(saveIds);
		assertFalse(activeSub1.isFlagForDeletion());
		assertEquals(0, idsToDelete.size());
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

		assertThat(idsToDelete, containsInAnyOrder(ID1));
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

}
