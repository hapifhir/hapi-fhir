package ca.uhn.fhir.jpa.subscription;

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

	public void setHeaders(String theHeaders) {
		myHeaders = new ArrayList<>();
		if (isNotBlank(theHeaders)) {
			myHeaders.add(theHeaders);
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

	public void setHeaders(List<? extends IPrimitiveType<String>> theHeader) {
		myHeaders = new ArrayList<>();
		for (IPrimitiveType<String> next : theHeader) {
			if (isNotBlank(next.getValueAsString())) {
				myHeaders.add(next.getValueAsString());
			}
		}
	}
}
