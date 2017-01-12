package ca.uhn.fhir.jpa.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandlerDstu2;
import ca.uhn.fhir.model.dstu2.resource.Subscription;

@Configuration
public class WebsocketDstu2DispatcherConfig {
	
	@Autowired
	private FhirContext myCtx;

	@Autowired
	private IFhirResourceDao<Subscription> mySubscriptionDao;

	@PostConstruct
	public void postConstruct() {
		SubscriptionWebsocketHandlerDstu2.setCtx(myCtx);
		SubscriptionWebsocketHandlerDstu2.setSubscriptionDao((IFhirResourceDaoSubscription<Subscription>) mySubscriptionDao);
	}
	
}
