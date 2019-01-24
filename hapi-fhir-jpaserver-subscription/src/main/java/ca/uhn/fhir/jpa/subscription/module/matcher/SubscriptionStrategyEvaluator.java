package ca.uhn.fhir.jpa.subscription.module.matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionStrategyEvaluator {

	@Autowired
	private CriteriaResourceMatcher myCriteriaResourceMatcher;

	public SubscriptionMatchingStrategy determineStrategy(String theCriteria) {
		SubscriptionMatchResult result = myCriteriaResourceMatcher.match(theCriteria, null, null);
		if (result.supported()) {
			return SubscriptionMatchingStrategy.IN_MEMORY;
		}
		return SubscriptionMatchingStrategy.DATABASE;
	}
}
