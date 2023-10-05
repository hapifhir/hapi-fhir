package ca.uhn.fhir.mdm.model;

public class MdmResourceMetrics {

	/**
	 * The resource type to which these metrics apply.
	 */
	private String myResourceType;

	/**
	 * The number of golden resources.
	 */
	private long myGoldenResourcesCount;

	/**
	 * The number of source resources.
	 */
	private long mySourceResourcesCount;

	/**
	 * The number of excluded resources.
	 * These are necessarily a subset of both
	 * GoldenResources and SourceResources
	 * (as each Blocked resource will still generate
	 * a GoldenResource)
	 */
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
