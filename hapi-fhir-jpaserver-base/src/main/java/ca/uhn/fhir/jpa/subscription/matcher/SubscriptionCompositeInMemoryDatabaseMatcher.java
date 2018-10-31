package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCompositeInMemoryDatabaseMatcher implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionCompositeInMemoryDatabaseMatcher.class);

	@Autowired
	SubscriptionMatcherDatabase mySubscriptionMatcherDatabase;
	@Autowired
	SubscriptionMatcherInMemory mySubscriptionMatcherInMemory;

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		SubscriptionMatchResult result;
		result = mySubscriptionMatcherInMemory.match(criteria, msg);
		if (!result.supported()) {
			ourLog.info("InMemoryMatcher doesn't support {} parameter.  Reverting to DatabaseMatcher", result.getUnsupportedParameter());
			result = mySubscriptionMatcherDatabase.match(criteria, msg);
		}
		return result;
	}
}
