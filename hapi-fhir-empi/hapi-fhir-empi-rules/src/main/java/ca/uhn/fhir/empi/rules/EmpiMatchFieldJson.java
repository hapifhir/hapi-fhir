package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.IEmpiComparator;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmpiMatchFieldJson implements IModelJson, IEmpiComparator<String> {
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("resourcePath")
	String myResourcePath;
	@JsonProperty("metric")
	DistanceMetricEnum myMetric;

	public EmpiMatchFieldJson() {}

	public EmpiMatchFieldJson(String theResourceType, String theResourcePath, DistanceMetricEnum theMetric) {
		myResourceType = theResourceType;
		myResourcePath = theResourcePath;
		myMetric = theMetric;
	}

	public DistanceMetricEnum getMetric() {
		return myMetric;
	}

	public EmpiMatchFieldJson setMetric(DistanceMetricEnum theMetric) {
		myMetric = theMetric;
		return this;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiMatchFieldJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getResourcePath() {
		return myResourcePath;
	}

	public EmpiMatchFieldJson setResourcePath(String theResourcePath) {
		myResourcePath = theResourcePath;
		return this;
	}

	@Override
	public double compare(String theLeftString, String theRightString) {
		return myMetric.compare(theLeftString, theRightString);
	}
}
