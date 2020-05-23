package ca.uhn.fhir.jpa.subscription.match.deliver.email;

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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public class EmailDetails {
	private String mySubjectTemplate;
	private String myBodyTemplate;
	private List<String> myTo;
	private String myFrom;
	private IIdType mySubscription;

	public String getBodyTemplate() {
		return myBodyTemplate;
	}

	public void setBodyTemplate(String theBodyTemplate) {
		myBodyTemplate = theBodyTemplate;
	}

	public String getFrom() {
		return myFrom;
	}

	public void setFrom(String theFrom) {
		myFrom = theFrom;
	}

	public String getSubjectTemplate() {
		return mySubjectTemplate;
	}

	public void setSubjectTemplate(String theSubjectTemplate) {
		mySubjectTemplate = theSubjectTemplate;
	}

	public IIdType getSubscription() {
		return mySubscription;
	}

	public void setSubscription(IIdType theSubscription) {
		mySubscription = theSubscription;
	}

	public List<String> getTo() {
		return myTo;
	}

	public void setTo(List<String> theTo) {
		myTo = theTo;
	}

}
