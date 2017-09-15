package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.TriggerDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CanonicalSubscription implements Serializable {

	private static final long serialVersionUID = 364269017L;

	private IIdType myIdElement;
	private String myCriteriaString;
	private String myEndpointUrl;
	private String myPayloadString;
	private List<String> myHeaders;
	private Subscription.SubscriptionChannelType myChannelType;
	private Subscription.SubscriptionStatus myStatus;
	private IBaseResource myBackingSubscription;
	private TriggerDefinition myTrigger;

	/**
	 * For now we're using the R4 TriggerDefinition, but this
	 * may change in the future when things stabilize
	 */
	public void addTrigger(TriggerDefinition theTrigger) {
		myTrigger = theTrigger;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		CanonicalSubscription that = (CanonicalSubscription) theO;

		return new EqualsBuilder()
			.append(getIdElement().getIdPart(), that.getIdElement().getIdPart())
			.isEquals();
	}

	public IBaseResource getBackingSubscription() {
		return myBackingSubscription;
	}

	public void setBackingSubscription(IBaseResource theBackingSubscription) {
		myBackingSubscription = theBackingSubscription;
	}

	public Subscription.SubscriptionChannelType getChannelType() {
		return myChannelType;
	}

	public void setChannelType(Subscription.SubscriptionChannelType theChannelType) {
		myChannelType = theChannelType;
	}

	public String getCriteriaString() {
		return myCriteriaString;
	}

	public void setCriteriaString(String theCriteriaString) {
		myCriteriaString = theCriteriaString;
	}

	public String getEndpointUrl() {
		return myEndpointUrl;
	}

	public void setEndpointUrl(String theEndpointUrl) {
		myEndpointUrl = theEndpointUrl;
	}

	public List<String> getHeaders() {
		return myHeaders;
	}

	public void setHeaders(List<? extends IPrimitiveType<String>> theHeader) {
		myHeaders = new ArrayList<>();
		for (IPrimitiveType<String> next : theHeader) {
			if (isNotBlank(next.getValueAsString())) {
				myHeaders.add(next.getValueAsString());
			}
		}
	}

	public IIdType getIdElement() {
		return myIdElement;
	}

	public void setIdElement(IIdType theIdElement) {
		myIdElement = theIdElement;
	}

	public String getPayloadString() {
		return myPayloadString;
	}

	public void setPayloadString(String thePayloadString) {
		myPayloadString = thePayloadString;
	}

	public Subscription.SubscriptionStatus getStatus() {
		return myStatus;
	}

	public void setStatus(Subscription.SubscriptionStatus theStatus) {
		myStatus = theStatus;
	}

	/**
	 * For now we're using the R4 triggerdefinition, but this
	 * may change in the future when things stabilize
	 */
	public TriggerDefinition getTrigger() {
		return myTrigger;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getIdElement().getIdPart())
			.toHashCode();
	}

	public void setHeaders(String theHeaders) {
		myHeaders = new ArrayList<>();
		if (isNotBlank(theHeaders)) {
			myHeaders.add(theHeaders);
		}
	}
}
