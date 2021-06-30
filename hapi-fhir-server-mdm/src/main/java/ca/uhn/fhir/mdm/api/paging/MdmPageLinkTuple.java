package ca.uhn.fhir.mdm.api.paging;

import java.util.Optional;

/**
 * Data clump class to keep the relevant paging URLs together for MDM.
 */
public class MdmPageLinkTuple {
	private String myPreviousLink = null;
	private String mySelfLink = null;
	private String myNextLink = null;

	MdmPageLinkTuple() {}

	public Optional<String> getPreviousLink() {
		return Optional.ofNullable(myPreviousLink);
	}

	public void setPreviousLink(String thePreviousLink) {
		this.myPreviousLink = thePreviousLink;
	}

	public String getSelfLink() {
		return mySelfLink;
	}

	public void setSelfLink(String theSelfLink) {
		this.mySelfLink = theSelfLink;
	}

	public Optional<String> getNextLink() {
		return Optional.ofNullable(myNextLink);
	}

	public void setNextLink(String theNextLink) {
		this.myNextLink = theNextLink;
	}
}
