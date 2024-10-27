package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicFilterUtilTest {

	private final ISubscriptionTopicFilterMatcher myFalseMatcher = (f, r) -> InMemoryMatchResult.noMatch();
	private final ISubscriptionTopicFilterMatcher myTrueMatcher = (f, r) -> InMemoryMatchResult.successfulMatch();
	private final AtomicInteger myCounter = new AtomicInteger();
	private final ISubscriptionTopicFilterMatcher myTrueFalseMatcher = (f, r) -> {
			if (myCounter.getAndIncrement() == 0) {
				return InMemoryMatchResult.successfulMatch();
			} else {
				return InMemoryMatchResult.noMatch();
			}
	};
	private final ISubscriptionTopicFilterMatcher myFalseTrueMatcher = (f, r) -> {
		if (myCounter.getAndIncrement() == 0) {
			return InMemoryMatchResult.noMatch();
		} else {
			return InMemoryMatchResult.successfulMatch();
		}
	};
	private Observation myObservation = new Observation();

	@Test
	void testFalseMatchNoFilters() {
		myObservation = new Observation();
		CanonicalTopicSubscription topicSubscription = new CanonicalTopicSubscription();
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myFalseMatcher, topicSubscription);
		assertTrue(result);
	}

	@Test
	void testFalseMatchOneFilter() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myFalseMatcher, topicSubscription);
		assertFalse(result);
	}

	@Test
	void testFalseMatchOneFilterTypeMismatch() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(new Encounter(), "Encounter", myFalseMatcher, topicSubscription);
		assertTrue(result);
	}
	@Test
	void testFalseMatchTwoFilters() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123", "Observation?code=456");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myFalseMatcher, topicSubscription);
		assertFalse(result);
	}

	@Test
	void testTrueMatchNoFilters() {
		myObservation = new Observation();
		CanonicalTopicSubscription topicSubscription = new CanonicalTopicSubscription();
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myTrueMatcher, topicSubscription);
		assertTrue(result);
	}

	@Test
	void testTrueMatchOneFilter() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myTrueMatcher, topicSubscription);
		assertTrue(result);
	}

	@Test
	void testTrueMatchTwoFilters() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123", "Observation?code=456");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myTrueMatcher, topicSubscription);
		assertTrue(result);
	}

	@Test
	void testTrueFalseMatchTwoFilters() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123", "Observation?code=456");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myTrueFalseMatcher, topicSubscription);
		assertFalse(result);
	}

	@Test
	void testFalseTrueMatchTwoFilters() {
		CanonicalTopicSubscription topicSubscription = buildTopicSubscriptionWithFilter("Observation?code=123", "Observation?code=456");
		boolean result = SubscriptionTopicFilterUtil.matchFilters(myObservation, "Observation", myFalseTrueMatcher, topicSubscription);
		assertFalse(result);
	}

	@Nonnull
	private static CanonicalTopicSubscription buildTopicSubscriptionWithFilter(String... theQueryUrls) {
		CanonicalTopicSubscription topicSubscription = new CanonicalTopicSubscription();
		for (String queryUrl : theQueryUrls) {
			List<CanonicalTopicSubscriptionFilter> filters = CanonicalTopicSubscriptionFilter.fromQueryUrl(queryUrl);
			filters.forEach(topicSubscription::addFilter);
		}
		return topicSubscription;
	}
}
