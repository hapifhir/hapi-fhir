package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ActiveSubscriptionTopicCacheTest {
	@Test
	public void testOperations() {
		var cache = new ActiveSubscriptionTopicCache();
		SubscriptionTopic topic1 = new SubscriptionTopic();
		topic1.setId("1");
		cache.add(topic1);
		assertThat(cache.getAll()).hasSize(1);
		assertThat(cache.size()).isEqualTo(1);
		assertThat(cache.getAll().iterator().next().getId()).isEqualTo("1");

		SubscriptionTopic topic2 = new SubscriptionTopic();
		topic2.setId("2");
		cache.add(topic2);

		SubscriptionTopic topic3 = new SubscriptionTopic();
		topic3.setId("3");
		cache.add(topic3);

		assertThat(cache.size()).isEqualTo(3);

		Set<String> idsToKeep = Set.of("1", "3");
		int removed = cache.removeIdsNotInCollection(idsToKeep);
		assertThat(removed).isEqualTo(1);
		assertThat(cache.size()).isEqualTo(2);
	}
}
