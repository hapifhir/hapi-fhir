package ca.uhn.fhir.jpa.subscription.match.deliver.email;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.server.mail.EmailDetails;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionEmailDetails extends EmailDetails {

	private IIdType mySubscription;

	public String getSubscriptionId() {
		return mySubscription.toUnqualifiedVersionless().getValue();
	}

	public void setSubscription(IIdType theSubscription) {
		mySubscription = theSubscription;
	}

	@Override
	protected Map<String, List<String>> getHeaders(){
		HashMap<String, List<String>> retVal = new HashMap<>();
		retVal.put("X-FHIR-Subscription", Arrays.asList(getSubscriptionId()));

		return retVal;

	}

	@Override
	public String getDetails(){
		return String.format("for subscription %s from [%s] to recipients: [%s]",  getSubscriptionId(), getFrom(), getTo());
	}

}
