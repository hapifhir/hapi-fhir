/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CanonicalSubscription implements Serializable, Cloneable, IModelJson {

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
	private CanonicalSubscriptionChannelType myChannelType;

	@JsonProperty("status")
	private Subscription.SubscriptionStatus myStatus;

	@JsonProperty("triggerDefinition")
	@Deprecated
	private CanonicalEventDefinition myTrigger;

	@JsonProperty("emailDetails")
	private EmailDetails myEmailDetails;

	@JsonProperty("restHookDetails")
	private RestHookDetails myRestHookDetails;

	@JsonProperty("extensions")
	private Map<String, List<String>> myChannelExtensions;

	@JsonProperty("tags")
	private Map<String, String> myTags;

	@JsonProperty("payloadSearchCriteria")
	private String myPayloadSearchCriteria;

	@JsonProperty("partitionId")
	private Integer myPartitionId;

	@JsonProperty("crossPartitionEnabled")
	private boolean myCrossPartitionEnabled;

	@JsonProperty("sendDeleteMessages")
	private boolean mySendDeleteMessages;

	@JsonProperty("isTopicSubscription")
	private boolean myIsTopicSubscription;

	@JsonProperty("myTopicSubscription")
	private CanonicalTopicSubscription myTopicSubscription;

	/**
	 * Constructor
	 */
	public CanonicalSubscription() {
		super();
	}

	public String getPayloadSearchCriteria() {
		return myPayloadSearchCriteria;
	}

	public void setPayloadSearchCriteria(String thePayloadSearchCriteria) {
		myPayloadSearchCriteria = thePayloadSearchCriteria;
	}

	@Deprecated
	public void addTrigger(CanonicalEventDefinition theTrigger) {
		myTrigger = theTrigger;
	}

	public CanonicalSubscriptionChannelType getChannelType() {
		return myChannelType;
	}

	public void setChannelType(CanonicalSubscriptionChannelType theChannelType) {
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

	@Nonnull
	public List<String> getHeaders() {
		return myHeaders != null ? Collections.unmodifiableList(myHeaders) : Collections.emptyList();
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

	public Map<String, String> getTags() {
		if (myTags == null) {
			myTags = new HashMap<>();
		}
		return myTags;
	}

	public void setTags(Map<String, String> theTags) {
		this.myTags = theTags;
	}

	public String getChannelExtension(String theUrl) {
		String retVal = null;
		List<String> channelExtensions = myChannelExtensions.get(theUrl);
		if (channelExtensions != null && !channelExtensions.isEmpty()) {
			retVal = channelExtensions.get(0);
		}
		return retVal;
	}

	@Nonnull
	public List<String> getChannelExtensions(String theUrl) {
		List<String> retVal = myChannelExtensions.get(theUrl);
		if (retVal == null) {
			retVal = Collections.emptyList();
		} else {
			retVal = Collections.unmodifiableList(retVal);
		}
		return retVal;
	}

	public void setChannelExtensions(Map<String, List<String>> theChannelExtensions) {
		myChannelExtensions = new HashMap<>();
		for (String url : theChannelExtensions.keySet()) {
			List<String> values = theChannelExtensions.get(url);
			if (isNotBlank(url) && values != null) {
				myChannelExtensions.put(url, values);
			}
		}
	}

	@Nullable
	public IIdType getIdElement(FhirContext theContext) {
		IIdType retVal = null;
		if (isNotBlank(myIdElement)) {
			retVal = theContext.getVersion().newIdType().setValue(myIdElement);
		}
		return retVal;
	}

	public String getIdPart() {
		return new IdType(getIdElementString()).getIdPart();
	}

	public String getIdElementString() {
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

	public Integer getRequestPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(Integer thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public boolean getCrossPartitionEnabled() {
		return myCrossPartitionEnabled;
	}

	public void setCrossPartitionEnabled(boolean myCrossPartitionEnabled) {
		this.myCrossPartitionEnabled = myCrossPartitionEnabled;
	}

	@Deprecated
	public CanonicalEventDefinition getTrigger() {
		return myTrigger;
	}

	public boolean getSendDeleteMessages() {
		return mySendDeleteMessages;
	}

	public void setSendDeleteMessages(boolean theSendDeleteMessages) {
		mySendDeleteMessages = theSendDeleteMessages;
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
		b.append(myChannelExtensions, that.myChannelExtensions);
		b.append(myCrossPartitionEnabled, that.myCrossPartitionEnabled);
		b.append(myChannelExtensions, that.myChannelExtensions);
		b.append(mySendDeleteMessages, that.mySendDeleteMessages);
		b.append(myPayloadSearchCriteria, that.myPayloadSearchCriteria);
		b.append(myTopicSubscription, that.myTopicSubscription);
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
				.append(myChannelExtensions)
				.toHashCode();
	}

	public void setIdElement(IIdType theIdElement) {
		myIdElement = null;
		if (theIdElement != null) {
			myIdElement = theIdElement.toUnqualifiedVersionless().getValue();
		}
	}

	/**
	 * Adds a header
	 *
	 * @param theHeader The header, e.g. "Authorization: Bearer AAAAA"
	 */
	public void addHeader(String theHeader) {
		if (isNotBlank(theHeader)) {
			initHeaders();
			myHeaders.add(theHeader);
		}
	}

	private void initHeaders() {
		if (myHeaders == null) {
			myHeaders = new ArrayList<>();
		}
	}

	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this)
				.append("myIdElement", myIdElement)
				.append("myStatus", myStatus)
				.append("myCriteriaString", myCriteriaString);
		//			.append("myEndpointUrl", myEndpointUrl)
		//			.append("myPayloadString", myPayloadString)
		//			.append("myHeaders", myHeaders)
		//			.append("myChannelType", myChannelType)
		//			.append("myTrigger", myTrigger)
		//			.append("myEmailDetails", myEmailDetails)
		//			.append("myRestHookDetails", myRestHookDetails)
		//			.append("myChannelExtensions", myChannelExtensions)
		if (isTopicSubscription()) {
			stringBuilder.append("topic", myTopicSubscription.getTopic());
		} else {
			stringBuilder.append("criteriaString", myCriteriaString);
		}

		return stringBuilder.toString();
	}

	public void setTopicSubscription(boolean theTopicSubscription) {
		myIsTopicSubscription = theTopicSubscription;
	}

	public boolean isTopicSubscription() {
		return myIsTopicSubscription;
	}

	// PayloadString is called ContentType in R5
	public String getContentType() {
		assert isTopicSubscription();
		return getPayloadString();
	}

	public CanonicalTopicSubscription getTopicSubscription() {
		assert isTopicSubscription();
		if (myTopicSubscription == null) {
			myTopicSubscription = new CanonicalTopicSubscription();
		}
		return myTopicSubscription;
	}

	public void setTopicSubscription(CanonicalTopicSubscription theTopicSubscription) {
		myTopicSubscription = theTopicSubscription;
	}

	public org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent getContent() {
		assert isTopicSubscription();
		return myTopicSubscription.getContent();
	}

	public String getTopic() {
		assert isTopicSubscription();
		return myTopicSubscription.getTopic();
	}

	public List<CanonicalTopicSubscriptionFilter> getFilters() {
		assert isTopicSubscription();
		return myTopicSubscription.getFilters();
	}

	public int getHeartbeatPeriod() {
		assert isTopicSubscription();
		return myTopicSubscription.getHeartbeatPeriod();
	}

	public int getTimeout() {
		assert isTopicSubscription();
		return myTopicSubscription.getTimeout();
	}

	public int getMaxCount() {
		assert isTopicSubscription();
		return myTopicSubscription.getMaxCount();
	}

	public static class EmailDetails implements IModelJson {

		@JsonProperty("from")
		private String myFrom;

		@JsonProperty("subjectTemplate")
		private String mySubjectTemplate;

		/**
		 * Constructor
		 */
		public EmailDetails() {
			super();
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

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;

			if (theO == null || getClass() != theO.getClass()) return false;

			EmailDetails that = (EmailDetails) theO;

			return new EqualsBuilder()
					.append(myFrom, that.myFrom)
					.append(mySubjectTemplate, that.mySubjectTemplate)
					.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					.append(myFrom)
					.append(mySubjectTemplate)
					.toHashCode();
		}
	}

	public static class RestHookDetails implements IModelJson {

		@JsonProperty("stripVersionId")
		private boolean myStripVersionId;

		@JsonProperty("deliverLatestVersion")
		private boolean myDeliverLatestVersion;

		/**
		 * Constructor
		 */
		public RestHookDetails() {
			super();
		}

		public boolean isDeliverLatestVersion() {
			return myDeliverLatestVersion;
		}

		public void setDeliverLatestVersion(boolean theDeliverLatestVersion) {
			myDeliverLatestVersion = theDeliverLatestVersion;
		}

		public boolean isStripVersionId() {
			return myStripVersionId;
		}

		public void setStripVersionId(boolean theStripVersionId) {
			myStripVersionId = theStripVersionId;
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
	}

	@Deprecated
	public static class CanonicalEventDefinition implements IModelJson {

		/**
		 * Constructor
		 */
		@Deprecated
		public CanonicalEventDefinition() {
			// nothing yet
		}
	}
}
