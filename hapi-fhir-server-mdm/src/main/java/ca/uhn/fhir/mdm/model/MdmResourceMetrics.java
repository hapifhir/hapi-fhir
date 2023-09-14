package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmResourceMetrics implements IModelJson {

	/**
	 * The resource type to which these metrics apply.
	 */
	@JsonProperty("resourceType")
	private String myResourceType;

	/**
	 * The number of golden resources.
	 */
	@JsonProperty("goldenResources")
	private long myGoldenResourcesCount;

	/**
	 * The number of source resources.
	 */
	@JsonProperty("sourceResources")
	private long mySourceResourcesCount;

	/**
	 * The number of excluded resources.
	 * These are necessarily a subset of both
	 * GoldenResources and SourceResources
	 * (as each Blocked resource will still generate
	 * a GoldenResource)
	 */
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
