package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmResourceMetrics implements IModelJson {

	@JsonProperty("resourceType")
	private String myResourceType;

	@JsonProperty("goldenResources")
	private long myGoldenResourcesCount;

	@JsonProperty("sourceResources")
	private long mySourceResourcesCount;

	@JsonProperty("excludedResources")
	private long myExcludedResources;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public long getGoldenResourcesCount() {
		return myGoldenResourcesCount;
	}

	public void setGoldenResourcesCount(long theGoldenResourcesCount) {
		myGoldenResourcesCount = theGoldenResourcesCount;
	}

	public long getSourceResourcesCount() {
		return mySourceResourcesCount;
	}

	public void setSourceResourcesCount(long theSourceResourcesCount) {
		mySourceResourcesCount = theSourceResourcesCount;
	}

	public long getExcludedResources() {
		return myExcludedResources;
	}

	public void setExcludedResources(long theExcludedResources) {
		myExcludedResources = theExcludedResources;
	}
}
