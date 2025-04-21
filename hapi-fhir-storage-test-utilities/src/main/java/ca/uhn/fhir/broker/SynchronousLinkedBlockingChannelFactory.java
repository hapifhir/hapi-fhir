/*-
 * #%L
 * hapi-fhir-storage-test-utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.broker;

import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import jakarta.annotation.Nonnull;

/**
 * An implementation of {@link LinkedBlockingChannelFactory} that forces
 * synchronous message processing. Used for testing.
 */
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
