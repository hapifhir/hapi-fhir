package ca.uhn.fhir.mdm.model;

public class MdmGoldenResourceCount {

	private String myResourceType;

	/**
	 * All golden resource count (including blocklisted ones)
	 */
	private long myGoldenResourceCount;

	/**
	 * Block list only resource count
	 */
	private long myBlockListedGoldenResourceCount;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public long getGoldenResourceCount() {
		return myGoldenResourceCount;
	}

	public void setGoldenResourceCount(long theGoldenResourceCount) {
		myGoldenResourceCount = theGoldenResourceCount;
	}

	public long getBlockListedGoldenResourceCount() {
		return myBlockListedGoldenResourceCount;
	}

	public void setBlockListedGoldenResourceCount(long theBlockListedGoldenResourceCount) {
		myBlockListedGoldenResourceCount = theBlockListedGoldenResourceCount;
	}
}
