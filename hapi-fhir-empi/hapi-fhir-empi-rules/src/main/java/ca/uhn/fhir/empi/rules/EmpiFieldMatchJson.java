package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public class EmpiFieldMatchJson implements IModelJson, IEmpiMatcher<String> {
	@JsonProperty("name")
	String myName;
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("resourcePath")
	String myResourcePath;
	@JsonProperty("metric")
	DistanceMetricEnum myMetric;
	@JsonProperty("matchThreshold")
	double myMatchThreshold;

	public EmpiFieldMatchJson() {}

	public EmpiFieldMatchJson(@Nonnull String theName, String theResourceType, String theResourcePath, DistanceMetricEnum theMetric, double theMatchThreshold) {
		myName = theName;
		myResourceType = theResourceType;
		myResourcePath = theResourcePath;
		myMetric = theMetric;
		myMatchThreshold = theMatchThreshold;
	}

	public DistanceMetricEnum getMetric() {
		return myMetric;
	}

	public EmpiFieldMatchJson setMetric(DistanceMetricEnum theMetric) {
		myMetric = theMetric;
		return this;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiFieldMatchJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getResourcePath() {
		return myResourcePath;
	}

	public EmpiFieldMatchJson setResourcePath(String theResourcePath) {
		myResourcePath = theResourcePath;
		return this;
	}

	public double getMatchThreshold() {
		return myMatchThreshold;
	}

	public EmpiFieldMatchJson setMatchThreshold(double theMatchThreshold) {
		myMatchThreshold = theMatchThreshold;
		return this;
	}

	public String getName() {
		return myName;
	}

	public EmpiFieldMatchJson setName(@Nonnull String theName) {
		myName = theName;
		return this;
	}

	@Override
	public boolean match(String theLeftString, String theRightString) {
		return myMetric.similarity(theLeftString, theRightString) >= myMatchThreshold;
	}
}
