package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActiveSubscriptionTopicCacheTest {
	@Test
	public void testOperations() {
		var cache = new ActiveSubscriptionTopicCache();
		SubscriptionTopic topic1 = new SubscriptionTopic();
		topic1.setId("1");
		cache.add(topic1);
		assertThat(cache.getAll()).hasSize(1);
		assertEquals(1, cache.size());
		assertEquals("1", cache.getAll().iterator().next().getId());

		SubscriptionTopic topic2 = new SubscriptionTopic();
		topic2.setId("2");
		cache.add(topic2);

		SubscriptionTopic topic3 = new SubscriptionTopic();
		topic3.setId("3");
		cache.add(topic3);

		assertEquals(3, cache.size());

		Set<String> idsToKeep = Set.of("1", "3");
		int removed = cache.removeIdsNotInCollection(idsToKeep);
		assertEquals(1, removed);
		assertEquals(2, cache.size());
	}
}
