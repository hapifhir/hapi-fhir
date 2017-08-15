package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSubscriptionSubscriber implements MessageHandler {
	static final String SUBSCRIPTION_STATUS = "status";
	static final String SUBSCRIPTION_TYPE = "channel.type";

	private final IFhirResourceDao mySubscriptionDao;
	private final ConcurrentHashMap<String, IBaseResource> myIdToSubscription;
	private final Subscription.SubscriptionChannelType myChannelType;
	private final SubscribableChannel myProcessingChannel;

	/**
	 * Constructor
	 */
	public BaseSubscriptionSubscriber(IFhirResourceDao<? extends IBaseResource> theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, SubscribableChannel theProcessingChannel) {
		mySubscriptionDao = theSubscriptionDao;
		myIdToSubscription = theIdToSubscription;
		myChannelType = theChannelType;
		myProcessingChannel = theProcessingChannel;
	}

	public Subscription.SubscriptionChannelType getChannelType() {
		return myChannelType;
	}

	public FhirContext getContext() {
		return getSubscriptionDao().getContext();
	}

	public ConcurrentHashMap<String, IBaseResource> getIdToSubscription() {
		return myIdToSubscription;
	}

	public SubscribableChannel getProcessingChannel() {
		return myProcessingChannel;
	}

	public IFhirResourceDao getSubscriptionDao() {
		return mySubscriptionDao;
	}

	/**
	 * Does this subscription type (e.g. rest hook, websocket, etc) apply to this interceptor?
	 */
	protected boolean subscriptionTypeApplies(ResourceModifiedMessage theMsg) {
		FhirContext ctx = mySubscriptionDao.getContext();
		IBaseResource subscription = theMsg.getNewPayload();
		return subscriptionTypeApplies(ctx, subscription);
	}

	/**
	 * Does this subscription type (e.g. rest hook, websocket, etc) apply to this interceptor?
	 */
	protected boolean subscriptionTypeApplies(FhirContext theCtx, IBaseResource theSubscription) {
		IPrimitiveType<?> status = theCtx.newTerser().getSingleValueOrNull(theSubscription, SUBSCRIPTION_TYPE, IPrimitiveType.class);
		boolean subscriptionTypeApplies = false;
		if (getChannelType().toCode().equals(status.getValueAsString())) {
			subscriptionTypeApplies = true;
		}
		return subscriptionTypeApplies;
	}

}
