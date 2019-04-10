package ca.uhn.fhir.jpa.subscription.module.cache;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ActiveSubscriptionCacheTest {
	@Test
	public void twoPhaseDelete() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = new ActiveSubscription(null, null);
		String id1 = "id1";
		activeSubscriptionCache.put(id1, activeSub1);
		assertFalse(activeSub1.isFlagForDeletion());
		List<String> saveIds = new ArrayList<>();

		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);
		assertTrue(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(id1));

		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);
		assertNull(activeSubscriptionCache.get(id1));
	}

	@Test
	public void secondPassUnflags() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = new ActiveSubscription(null, null);
		String id1 = "id1";
		List<String> saveIds = new ArrayList<>();
		activeSubscriptionCache.put(id1, activeSub1);

		assertFalse(activeSub1.isFlagForDeletion());

		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);
		assertTrue(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(id1));

		saveIds.add(id1);
		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);
		assertFalse(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(id1));
	}

	@Test
	public void onlyFlaggedDeleted() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = new ActiveSubscription(null, null);
		String id1 = "id1";
		ActiveSubscription activeSub2 = new ActiveSubscription(null, null);
		String id2 = "id2";
		activeSubscriptionCache.put(id1, activeSub1);
		activeSubscriptionCache.put(id2, activeSub2);

		activeSub1.setFlagForDeletion(true);
		List<String> saveIds = new ArrayList<>();

		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);

		assertNull(activeSubscriptionCache.get(id1));
		assertNotNull(activeSubscriptionCache.get(id2));
		assertTrue(activeSub2.isFlagForDeletion());
	}

	@Test
	public void onListSavesAndUnmarksFlag() {
		ActiveSubscriptionCache activeSubscriptionCache = new ActiveSubscriptionCache();
		ActiveSubscription activeSub1 = new ActiveSubscription(null, null);
		String id1 = "id1";
		ActiveSubscription activeSub2 = new ActiveSubscription(null, null);
		String id2 = "id2";
		activeSubscriptionCache.put(id1, activeSub1);
		activeSubscriptionCache.put(id2, activeSub2);

		activeSub1.setFlagForDeletion(true);
		List<String> saveIds = new ArrayList<>();
		saveIds.add(id1);
		saveIds.add(id2);

		activeSubscriptionCache.unregisterAllSubscriptionsNotInCollection(saveIds);

		assertNotNull(activeSubscriptionCache.get(id1));
		assertFalse(activeSub1.isFlagForDeletion());
		assertNotNull(activeSubscriptionCache.get(id2));
		assertFalse(activeSub2.isFlagForDeletion());
	}

}
