package ca.uhn.fhir.broker;

import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import jakarta.annotation.Nonnull;

public class SynchronousLinkedBlockingChannelFactory extends LinkedBlockingChannelFactory {

	public SynchronousLinkedBlockingChannelFactory(
			IChannelNamer theChannelNamer, RetryPolicyProvider theRetryPolicyProvider) {
		super(theChannelNamer, theRetryPolicyProvider);
	}

	@Override
	@Nonnull
	protected LinkedBlockingChannel buildLinkedBlockingChannel(int theConcurrentConsumers, String theChannelName) {
		return LinkedBlockingChannel.newSynchronous(theChannelName, myRetryPolicyProvider);
	}
}
