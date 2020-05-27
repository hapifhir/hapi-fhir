package ca.uhn.fhir.jpa.subscription.match.registry;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveSubscription {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	private CanonicalSubscription mySubscription;
	private final String myChannelName;
	private final String myId;
	private boolean flagForDeletion;

	public ActiveSubscription(CanonicalSubscription theSubscription, String theChannelName) {
		mySubscription = theSubscription;
		myChannelName = theChannelName;
		myId = theSubscription.getIdPart();
	}

	public CanonicalSubscription getSubscription() {
		return mySubscription;
	}

	public String getChannelName() {
		return myChannelName;
	}

	public String getCriteriaString() {
		return mySubscription.getCriteriaString();
	}

	public void setSubscription(CanonicalSubscription theCanonicalizedSubscription) {
		mySubscription = theCanonicalizedSubscription;
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
}
