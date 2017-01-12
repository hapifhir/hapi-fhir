package ca.uhn.fhir.jpa.config.dstu3;

import javax.annotation.PostConstruct;

import org.hl7.fhir.dstu3.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandlerDstu3;

@Configuration
public class WebsocketDstu3DispatcherConfig {
	
	@Autowired
	private FhirContext myCtx;

	@Autowired
	private IFhirResourceDao<org.hl7.fhir.dstu3.model.Subscription> mySubscriptionDao;

	@PostConstruct
	public void postConstruct() {
		SubscriptionWebsocketHandlerDstu3.setCtx(myCtx);
		SubscriptionWebsocketHandlerDstu3.setSubscriptionDao((IFhirResourceDaoSubscription<Subscription>) mySubscriptionDao);
	}
	
}
