package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bundle {

	private String myAuthorDevice;
	private String myAuthorName;
	private List<BundleEntry> myEntries;
	private String myId;
	private String myLinkBase;
	private String myLinkFirst;
	private String myLinkLast;
	private String myLinkNext;
	private String myLinkPrevious;
	private String myLinkSelf;
	private Date myPublished;
	private String myTitle;
	private Integer myTotalResults;
	private Date myUpdated;

	public String getAuthorDevice() {
		return myAuthorDevice;
	}

	public String getAuthorName() {
		return myAuthorName;
	}

	public List<BundleEntry> getEntries() {
		if (myEntries == null) {
			myEntries = new ArrayList<BundleEntry>();
		}
		return myEntries;
	}

	public String getId() {
		return myId;
	}

	public String getLinkBase() {
		return myLinkBase;
	}

	public String getLinkFirst() {
		return myLinkFirst;
	}

	public String getLinkLast() {
		return myLinkLast;
	}

	public String getLinkNext() {
		return myLinkNext;
	}

	public String getLinkPrevious() {
		return myLinkPrevious;
	}

	public String getLinkSelf() {
		return myLinkSelf;
	}

	public Date getPublished() {
		return myPublished;
	}

	public String getTitle() {
		return myTitle;
	}

	public Integer getTotalResults() {
		return myTotalResults;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setAuthorDevice(String theAuthorDevice) {
		myAuthorDevice = theAuthorDevice;
	}

	public void setAuthorName(String theAuthorName) {
		myAuthorName = theAuthorName;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public void setLinkBase(String theLinkBase) {
		myLinkBase = theLinkBase;
	}

	public void setLinkFirst(String theLinkFirst) {
		myLinkFirst = theLinkFirst;
	}

	public void setLinkLast(String theLinkLast) {
		myLinkLast = theLinkLast;
	}

	public void setLinkNext(String theLinkNext) {
		myLinkNext = theLinkNext;
	}

	public void setLinkPrevious(String theLinkPrevious) {
		myLinkPrevious = theLinkPrevious;
	}

	public void setLinkSelf(String theLinkSelf) {
		myLinkSelf = theLinkSelf;
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public void setTitle(String theTitle) {
		myTitle = theTitle;
	}

	public void setTotalResults(Integer theTotalResults) {
		myTotalResults = theTotalResults;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

}
