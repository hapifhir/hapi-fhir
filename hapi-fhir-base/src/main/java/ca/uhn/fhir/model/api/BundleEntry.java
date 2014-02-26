package ca.uhn.fhir.model.api;

import java.util.Date;

public class BundleEntry {
	private String myTitle;
	private String myId;
	private Date myUpdated;
	private Date myPublished;
	private String myAuthorName;
	private String myAuthorUri;
	private IResource myResource;

	public String getTitle() {
		return myTitle;
	}

	public void setTitle(String theTitle) {
		myTitle = theTitle;
	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public Date getPublished() {
		return myPublished;
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public String getAuthorName() {
		return myAuthorName;
	}

	public void setAuthorName(String theAuthorName) {
		myAuthorName = theAuthorName;
	}

	public String getAuthorUri() {
		return myAuthorUri;
	}

	public void setAuthorUri(String theAuthorUri) {
		myAuthorUri = theAuthorUri;
	}

	public IResource getResource() {
		return myResource;
	}

	public void setResource(IResource theResource) {
		myResource = theResource;
	}

}
