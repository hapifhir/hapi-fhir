package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
// FIXME KHS use this to load empi subscriptions
public class EmpiSubscriptionLoader {
	@Autowired
	public DaoRegistry myDaoRegistry;
	@Autowired
	SubscriptionLoader mySubscriptionLoader;
	@Autowired
	IChannelNamer myChannelNamer;

	public void daoUpdateEmpiSubscriptions() {
		IBaseResource patientSub = buildEmpiSubscription("empi-patient", "Patient?");
		IBaseResource practitionerSub = buildEmpiSubscription("empi-practitioner", "Practitioner?");
		IFhirResourceDao<IBaseResource> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		subscriptionDao.update(patientSub);
		subscriptionDao.update(practitionerSub);

		// FIXME KHS remove these?
		mySubscriptionLoader.addInternalSubscriptionSupplier(() -> Arrays.asList(patientSub, practitionerSub));
		mySubscriptionLoader.syncSubscriptions();

// FIXME KHS
//		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(patientSub);
//		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(practitionerSub);
	}

	private Subscription buildEmpiSubscription(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("EMPI");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(ca.uhn.fhir.empi.api.Constants.SYSTEM_EMPI_MANAGED).setCode(Constants.CODE_HAPI_EMPI_MANAGED);
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("jms:queue:" + myChannelNamer.getChannelName(IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME));
		channel.setPayload("application/json");
		return retval;
	}
}
