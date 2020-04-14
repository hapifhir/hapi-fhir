package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
// FIXME KHS use this to load empi subscriptions
public class EmpiSubscriptionLoader {
	@Autowired
	public SubscriptionLoader mySubscriptionLoader;
	@Autowired
	public SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	IChannelNamer myChannelNamer;

	public void registerEmpiSubscriptions() {
		// FIXME KHS works without ? ?
		IBaseResource patientSub = buildEmpiSubscription("empi-patient", "Patient");
		IBaseResource practitionerSub = buildEmpiSubscription("empi-practitioner", "Practitioner");
		myDaoConfig.addSupportedSubscriptionType(org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.MESSAGE);
		mySubscriptionLoader.addInternalSubscriptionSupplier(() -> Arrays.asList(patientSub, practitionerSub));
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(patientSub);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(practitionerSub);
	}

	private Subscription buildEmpiSubscription(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("EMPI");
		retval.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(Constants.SYSTEM_EMPI_MANAGED).setCode(Constants.CODE_HAPI_EMPI_MANAGED);
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("jms:queue:"+ myChannelNamer.getChannelName(IEmpiConfig.EMPI_MATCHING_CHANNEL_NAME));
		channel.setPayload("application/json");
		return retval;
	}
}
