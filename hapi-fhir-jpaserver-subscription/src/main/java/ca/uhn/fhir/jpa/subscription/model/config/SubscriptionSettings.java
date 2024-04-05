package ca.uhn.fhir.jpa.subscription.model.config;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.dstu2.model.Subscription;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionSettings {
	private Set<Subscription.SubscriptionChannelType> mySupportedSubscriptionTypes = new HashSet<>();
	/**
	 * Since 6.4.0
	 */
	private boolean myQualifySubscriptionMatchingChannelName = true;

	public SubscriptionSettings() {}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public SubscriptionSettings addSupportedSubscriptionType(
			Subscription.SubscriptionChannelType theSubscriptionChannelType) {
		mySupportedSubscriptionTypes.add(theSubscriptionChannelType);
		return this;
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public Set<Subscription.SubscriptionChannelType> getSupportedSubscriptionTypes() {
		return Collections.unmodifiableSet(mySupportedSubscriptionTypes);
	}

	/**
	 * Indicate whether a subscription channel type is supported by this server.
	 *
	 * @return true if at least one subscription channel type is supported by this server false otherwise.
	 */
	public boolean hasSupportedSubscriptionTypes() {
		return CollectionUtils.isNotEmpty(mySupportedSubscriptionTypes);
	}

	@VisibleForTesting
	public void clearSupportedSubscriptionTypesForUnitTest() {
		mySupportedSubscriptionTypes.clear();
	}

	/**
	 * This setting controls whether the {@link  BaseChannelSettings#isQualifyChannelName}
	 * should be qualified or not.
	 * Default is true, ie, the channel name will be qualified.
	 *
	 * @since 6.4.0
	 */
	public void setQualifySubscriptionMatchingChannelName(boolean theQualifySubscriptionMatchingChannelName) {
		myQualifySubscriptionMatchingChannelName = theQualifySubscriptionMatchingChannelName;
	}

	/**
	 * This setting return whether the {@link BaseChannelSettings#isQualifyChannelName}
	 * should be qualified or not.
	 *
	 * @return whether the {@link BaseChannelSettings#isQualifyChannelName} is qualified or not
	 * @since 6.4.0
	 */
	public boolean isQualifySubscriptionMatchingChannelName() {
		return myQualifySubscriptionMatchingChannelName;
	}
}
