package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.EventDefinition;
import org.hl7.fhir.r4.model.Subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CanonicalSubscription implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String myIdElement;
	@JsonProperty("criteria")
	private String myCriteriaString;
	@JsonProperty("endpointUrl")
	private String myEndpointUrl;
	@JsonProperty("payload")
	private String myPayloadString;
	@JsonProperty("headers")
	private List<String> myHeaders;
	@JsonProperty("channelType")
	private Subscription.SubscriptionChannelType myChannelType;
	@JsonProperty("status")
	private Subscription.SubscriptionStatus myStatus;
	@JsonProperty("triggerDefinition")
	private CanonicalEventDefinition myTrigger;
	@JsonProperty("emailDetails")
	private EmailDetails myEmailDetails;
	@JsonProperty("restHookDetails")
	private RestHookDetails myRestHookDetails;

	/**
	 * For now we're using the R4 TriggerDefinition, but this
	 * may change in the future when things stabilize
	 */
	public void addTrigger(CanonicalEventDefinition theTrigger) {
		myTrigger = theTrigger;
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

	public EmailDetails getEmailDetails() {
		if (myEmailDetails == null) {
			myEmailDetails = new EmailDetails();
		}
		return myEmailDetails;
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

	public void setHeaders(String theHeaders) {
		myHeaders = new ArrayList<>();
		if (isNotBlank(theHeaders)) {
			myHeaders.add(theHeaders);
		}
	}

	public IIdType getIdElement(FhirContext theContext) {
		IIdType retVal = null;
		if (isNotBlank(myIdElement)) {
			retVal = theContext.getVersion().newIdType().setValue(myIdElement);
		}
		return retVal;
	}

	String getIdElementString() {
		return myIdElement;
	}

	public String getPayloadString() {
		return myPayloadString;
	}

	public void setPayloadString(String thePayloadString) {
		myPayloadString = thePayloadString;
	}

	public RestHookDetails getRestHookDetails() {
		if (myRestHookDetails == null) {
			myRestHookDetails = new RestHookDetails();
		}
		return myRestHookDetails;
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
	public CanonicalEventDefinition getTrigger() {
		return myTrigger;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		CanonicalSubscription that = (CanonicalSubscription) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myIdElement, that.myIdElement);
		b.append(myCriteriaString, that.myCriteriaString);
		b.append(myEndpointUrl, that.myEndpointUrl);
		b.append(myPayloadString, that.myPayloadString);
		b.append(myHeaders, that.myHeaders);
		b.append(myChannelType, that.myChannelType);
		b.append(myStatus, that.myStatus);
		b.append(myTrigger, that.myTrigger);
		b.append(myEmailDetails, that.myEmailDetails);
		b.append(myRestHookDetails, that.myRestHookDetails);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myIdElement)
			.append(myCriteriaString)
			.append(myEndpointUrl)
			.append(myPayloadString)
			.append(myHeaders)
			.append(myChannelType)
			.append(myStatus)
			.append(myTrigger)
			.append(myEmailDetails)
			.append(myRestHookDetails)
			.toHashCode();
	}

	public void setIdElement(IIdType theIdElement) {
		myIdElement = null;
		if (theIdElement != null) {
			myIdElement = theIdElement.toUnqualifiedVersionless().getValue();
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class EmailDetails {
		@JsonProperty("from")
		private String myFrom;
		@JsonProperty("subjectTemplate")
		private String mySubjectTemplate;

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
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class RestHookDetails {
		@JsonProperty("stripVersionId")
		private boolean myStripVersionId;
		@JsonProperty("deliverLatestVersion")
		private boolean myDeliverLatestVersion;

		public boolean isDeliverLatestVersion() {
			return myDeliverLatestVersion;
		}

		public void setDeliverLatestVersion(boolean theDeliverLatestVersion) {
			myDeliverLatestVersion = theDeliverLatestVersion;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;

			if (theO == null || getClass() != theO.getClass()) return false;

			RestHookDetails that = (RestHookDetails) theO;

			return new EqualsBuilder()
				.append(myStripVersionId, that.myStripVersionId)
				.append(myDeliverLatestVersion, that.myDeliverLatestVersion)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(myStripVersionId)
				.append(myDeliverLatestVersion)
				.toHashCode();
		}

		public boolean isStripVersionId() {
			return myStripVersionId;
		}

		public void setStripVersionId(boolean theStripVersionId) {
			myStripVersionId = theStripVersionId;
		}

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	public static class CanonicalEventDefinition {

		public CanonicalEventDefinition(EventDefinition theDef) {
			// nothing yet
		}
	}

}
