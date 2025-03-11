package ca.uhn.fhir.jpa.subscription.model;

import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CanonicalTopicSubscriptionFilterTest {

	@Test
	void fromQueryUrl() {
		String queryUrl = "/Patient?family=smith&given=stevie,elisha&family=carpenter";
		List<CanonicalTopicSubscriptionFilter> filters = CanonicalTopicSubscriptionFilter.fromQueryUrl(queryUrl);
		assertThat(filters).hasSize(3);
		assertTrue(filters.stream().map(CanonicalTopicSubscriptionFilter::getComparator).allMatch(Enumerations.SearchComparator.EQ::equals));
		assertTrue(filters.stream().map(CanonicalTopicSubscriptionFilter::getModifier).allMatch(Objects::isNull));
		assertTrue(filters.stream().map(CanonicalTopicSubscriptionFilter::getResourceType).allMatch("Patient"::equals));
		assertThat(filters.stream().map(CanonicalTopicSubscriptionFilter::getFilterParameter).collect(Collectors.toSet())).containsExactlyInAnyOrder("family", "given");
		assertThat(filters.stream().map(CanonicalTopicSubscriptionFilter::getValue).collect(Collectors.toSet())).containsExactlyInAnyOrder("smith", "stevie,elisha", "carpenter");
	}
}
