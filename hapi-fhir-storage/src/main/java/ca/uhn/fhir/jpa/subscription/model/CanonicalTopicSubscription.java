package ca.uhn.fhir.jpa.subscription.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.r5.model.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanonicalTopicSubscription {
	@JsonProperty("topic")
	private String myTopic;

	@JsonProperty("filters")
	private List<CanonicalTopicSubscriptionFilter> myFilters;

	@JsonProperty("parameters")
	private Map<String, String> myParameters;

	@JsonProperty("heartbeatPeriod")
	private Integer myHeartbeatPeriod;

	@JsonProperty("timeout")
	private Integer myTimeout;

	@JsonProperty("content")
	private Subscription.SubscriptionPayloadContent myContent;

	@JsonProperty("maxCount")
	private Integer myMaxCount;

	public String getTopic() {
		return myTopic;
	}

	public void setTopic(String theTopic) {
		myTopic = theTopic;
	}

	public List<CanonicalTopicSubscriptionFilter> getFilters() {
		if (myFilters == null) {
			myFilters = new ArrayList<>();
		}
		return myFilters;
	}

	public void addFilter(CanonicalTopicSubscriptionFilter theFilter) {
		getFilters().add(theFilter);
	}

	public void setFilters(List<CanonicalTopicSubscriptionFilter> theFilters) {
		myFilters = theFilters;
	}

	public Map<String, String> getParameters() {
		if (myParameters == null) {
			myParameters = new HashMap<>();
		}
		return myParameters;
	}

	public void setParameters(Map<String, String> theParameters) {
		myParameters = theParameters;
	}

	public Integer getHeartbeatPeriod() {
		return myHeartbeatPeriod;
	}

	public void setHeartbeatPeriod(Integer theHeartbeatPeriod) {
		myHeartbeatPeriod = theHeartbeatPeriod;
	}

	public Integer getTimeout() {
		return myTimeout;
	}

	public void setTimeout(Integer theTimeout) {
		myTimeout = theTimeout;
	}

	public Integer getMaxCount() {
		return myMaxCount;
	}

	public void setMaxCount(Integer theMaxCount) {
		myMaxCount = theMaxCount;
	}

	public Subscription.SubscriptionPayloadContent getContent() {
		return myContent;
	}

	public void setContent(Subscription.SubscriptionPayloadContent theContent) {
		myContent = theContent;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		CanonicalTopicSubscription that = (CanonicalTopicSubscription) theO;

		return new EqualsBuilder().append(myTopic, that.myTopic).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myTopic).toHashCode();
	}
}
