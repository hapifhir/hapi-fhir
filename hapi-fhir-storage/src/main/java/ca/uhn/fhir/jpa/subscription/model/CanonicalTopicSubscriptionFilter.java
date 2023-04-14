package ca.uhn.fhir.jpa.subscription.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SearchParameter;

public class CanonicalTopicSubscriptionFilter {
	@JsonProperty("resourceType")
	String myResourceType;

	@JsonProperty("filterParameter")
	String myFilterParameter;


	@JsonProperty("comparator")
	SearchParameter.SearchComparator myComparator;

	@JsonProperty("modifier")
	Enumerations.SubscriptionSearchModifier myModifier;

	@JsonProperty("value")
	String myValue;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getFilterParameter() {
		return myFilterParameter;
	}

	public void setFilterParameter(String theFilterParameter) {
		myFilterParameter = theFilterParameter;
	}

	public SearchParameter.SearchComparator getComparator() {
		return myComparator;
	}

	public void setComparator(SearchParameter.SearchComparator theComparator) {
		myComparator = theComparator;
	}

	public Enumerations.SubscriptionSearchModifier getModifier() {
		return myModifier;
	}

	public void setModifier(Enumerations.SubscriptionSearchModifier theModifier) {
		myModifier = theModifier;
	}

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}
}
