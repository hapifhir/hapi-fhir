package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class EmpiLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLoader.class);

	@Autowired
	IEmpiSettings myEmpiProperties;
	@Autowired
	EmpiProviderLoader myEmpiProviderLoader;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Autowired
	EmpiSearchParameterLoader myEmpiSearchParameterLoader;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the EMPI subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void updateSubscriptions() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myEmpiProviderLoader.loadProvider();
		ourLog.info("EMPI provider registered");

		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
		ourLog.info("EMPI subscriptions updated");

		myEmpiSearchParameterLoader.daoUpdateEmpiSearchParameters();
		ourLog.info("EMPI search parameters updated");
	}
}
