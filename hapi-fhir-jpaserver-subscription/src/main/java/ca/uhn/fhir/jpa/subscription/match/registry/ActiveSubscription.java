package ca.uhn.fhir.jpa.subscription.match.registry;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;

public class ActiveSubscription {

	private SubscriptionCriteriaParser.SubscriptionCriteria myCriteria;

	private final String myChannelName;
	private final String myId;
	private CanonicalSubscription mySubscription;
	private boolean flagForDeletion;

	private ChannelRetryConfiguration myRetryConfigurationParameters;

	public ActiveSubscription(CanonicalSubscription theSubscription, String theChannelName) {
		myChannelName = theChannelName;
		myId = theSubscription.getIdPart();
		setSubscription(theSubscription);
	}

	public SubscriptionCriteriaParser.SubscriptionCriteria getCriteria() {
		return myCriteria;
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public final void setSubscription(CanonicalSubscription theSubscription) {
		mySubscription = theSubscription;
		myCriteria = SubscriptionCriteriaParser.parse(theSubscription.getCriteriaString());
	}

	public String getChannelName() {
		return myChannelName;
	}

	public boolean isFlagForDeletion() {
		return flagForDeletion;
	}

	public void setFlagForDeletion(boolean theFlagForDeletion) {
		flagForDeletion = theFlagForDeletion;
	}

	public String getId() {
		return myId;
	}

	public CanonicalSubscriptionChannelType getChannelType() {
		return mySubscription.getChannelType();
	}

	public void setRetryConfiguration(ChannelRetryConfiguration theParams) {
		myRetryConfigurationParameters = theParams;
	}

	public ChannelRetryConfiguration getRetryConfigurationParameters() {
		return myRetryConfigurationParameters;
	}
}
